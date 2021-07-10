package net.devsong.slimit.listener;

import java.util.List;
import java.util.logging.Logger;

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
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.devsong.slimit.RegionDB;
import net.devsong.slimit.SLimit;
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
						if (SLimit.regions.get(player.getName()) != null) {
							// 获取点击位置
							Location pos = event.getClickedBlock().getLocation();
							if (SLimit.regions.get(player.getName()).getBLocation() != null) {
								// 如果世界相同
								if (SLimit.regions.get(player.getName()).getBLocation().getWorld().getName()
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
						if (SLimit.regions.get(player.getName()) != null) {
							// 获取点击位置
							Location pos = event.getClickedBlock().getLocation();
							if (SLimit.regions.get(player.getName()).getALocation() != null) {
								// 如果世界相同
								if (SLimit.regions.get(player.getName()).getALocation().getWorld().getName()
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
			}
		}
	}

	private void signA(Player player, Location pos) {
		player.sendMessage(
				org.bukkit.ChatColor.YELLOW + "[SLimit] 标记A点: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
		SLimit.regions.get(player.getName()).setALocation(pos);
		log.info(player.getName() + " 标记A点 " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " 于世界 "
				+ pos.getWorld().getName());
	}

	private void signB(Player player, Location pos) {
		player.sendMessage(
				org.bukkit.ChatColor.YELLOW + "[SLimit] 标记B点: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
		SLimit.regions.get(player.getName()).setBLocation(pos);
		log.info(player.getName() + " 标记B点 " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " 于世界 "
				+ pos.getWorld().getName());
	}

	@EventHandler
	public void onEntitySpawnEvent(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		List<Integer> Speed;
		if (entity instanceof Monster && !(entity instanceof Boss) && !(entity instanceof ElderGuardian)) {
			Speed = RegionDB.retrieval(event.getLocation(), true);
			if (Speed != null) {
				if (Speed.get(1) == 1) {
					event.setCancelled(true);
				} else if (Speed.get(0) < Speed.get(1)) {
					event.setCancelled(true);
					RegionDB.setMonSpeed0(Speed.get(2), Speed.get(0) + 1);
				} else {
					RegionDB.setMonSpeed0(Speed.get(2), 1);
				}
			}
		} else if (entity instanceof Animals) {
			Speed = RegionDB.retrieval(event.getLocation(), false);
			if (Speed != null) {
				if (Speed.get(1) == 1) {
					event.setCancelled(true);
				} else if (Speed.get(0) < Speed.get(1)) {
					event.setCancelled(true);
					RegionDB.setAniSpeed0(Speed.get(2), Speed.get(0) + 1);
				} else {
					RegionDB.setAniSpeed0(Speed.get(2), 1);
				}
			}
		}
	}
}
