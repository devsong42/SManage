package net.devsong.smanage.listener;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import net.devsong.smanage.SLocation;
import net.devsong.smanage.SManage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boss;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.devsong.smanage.RegionDB;
import net.md_5.bungee.api.ChatColor;

public class LimitListener implements Listener {
	private Logger log;
	public static Material item = Material.APPLE;

	public LimitListener(Logger log) {
		this.log = log;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND)) {
			if (event.getMaterial() == item) {
				Action action = event.getAction();
				Player player = event.getPlayer();
				if (player.isOp()) {
					// 左键方块
					if (action.equals(Action.LEFT_CLICK_BLOCK)) {
						if (SManage.regions.get(player.getName()) != null) {
							// 获取点击位置
							Location pos = event.getClickedBlock().getLocation();
							if (SManage.regions.get(player.getName()).getBLocation() != null) {
								// 如果世界相同
								if (SManage.regions.get(player.getName()).getBLocation().getWorld()
										.equals(pos.getWorld().getName())) {
									// 发送标记信息
									signA(player, pos);
								} else
									player.sendMessage(ChatColor.RED + "请在同一个世界标记！");
							} else {
								// 发送标记信息
								signA(player, pos);
							}
						}
					}
					// 右键方块
					if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
						if (SManage.regions.get(player.getName()) != null) {
							// 获取点击位置
							Location pos = event.getClickedBlock().getLocation();
							if (SManage.regions.get(player.getName()).getALocation() != null) {
								// 如果世界相同
								if (SManage.regions.get(player.getName()).getALocation().getWorld()
										.equals(pos.getWorld().getName())) {
									// 发送标记信息
									signB(player, pos);
								} else
									player.sendMessage(ChatColor.RED + "请在同一个世界标记！");
							} else {
								// 发送标记信息
								signB(player, pos);
							}
						}
					}
				}
				event.setCancelled(true);
			}
		}
	}

	private void signA(Player player, Location pos) {
		player.sendMessage(
				org.bukkit.ChatColor.YELLOW + "[SManage] 标记A点: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
		SManage.regions.get(player.getName()).setALocation(new SLocation().setX(pos.getX()).setY(pos.getY()).setZ(pos.getZ()).setWorld(pos.getWorld().getName()));
		log.info(player.getName() + " 标记A点 " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " 于世界 "
				+ pos.getWorld().getName());
	}

	private void signB(Player player, Location pos) {
		player.sendMessage(
				org.bukkit.ChatColor.YELLOW + "[SManage] 标记B点: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
		SManage.regions.get(player.getName()).setBLocation(new SLocation().setX(pos.getX()).setY(pos.getY()).setZ(pos.getZ()).setWorld(pos.getWorld().getName()));
		log.info(player.getName() + " 标记B点 " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " 于世界 "
				+ pos.getWorld().getName());
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
			Entity entity = event.getEntity();
			List<Integer> Speed;
			if (entity instanceof Monster && !(entity instanceof Boss) && !(entity instanceof ElderGuardian)) {
				Speed = RegionDB.retrieval(event.getLocation(), true);
				if (Speed != null) {
					if (Speed.get(0) != -1) {
						if (Speed.get(1) == 1) {
							event.setCancelled(true);
						} else if (Speed.get(0) < Speed.get(1)) {
							event.setCancelled(true);
							RegionDB.setMonSpeed0(Speed.get(2), Speed.get(0) + 1);
						} else
							RegionDB.setMonSpeed0(Speed.get(2), 1);
					} else
						for (int i = 1; i < Speed.get(1); i++)
							Objects.requireNonNull(event.getLocation().getWorld()).spawnEntity(event.getLocation(), entity.getType());
				}
			} else if (entity instanceof Animals) {
				Speed = RegionDB.retrieval(event.getLocation(), false);
				if (Speed != null)
					if (Speed.get(0) != -1) {
						if (Speed.get(1) == 1) {
							event.setCancelled(true);
						} else if (Speed.get(0) < Speed.get(1)) {
							event.setCancelled(true);
							RegionDB.setAniSpeed0(Speed.get(2), Speed.get(0) + 1);
						} else
							RegionDB.setAniSpeed0(Speed.get(2), 1);
					} else
						for (int i = 1; i < Speed.get(1); i++)
							Objects.requireNonNull(event.getLocation().getWorld()).spawnEntity(event.getLocation(), entity.getType());
			}
		}
	}
}
