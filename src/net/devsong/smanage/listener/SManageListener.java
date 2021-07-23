package net.devsong.smanage.listener;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import net.devsong.smanage.SLocation;
import net.devsong.smanage.SManage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.devsong.smanage.RegionDB;
import net.md_5.bungee.api.ChatColor;

public class SManageListener implements Listener {
    private Logger log;
    public static Material item = Material.APPLE;

    public SManageListener(Logger log) {
        this.log = log;
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
                                    event.setCancelled(true);
                                } else
                                    player.sendMessage(ChatColor.RED + "请在同一个世界标记！");
                            } else {
                                // 发送标记信息
                                signA(player, pos);
                                event.setCancelled(true);
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
                                    event.setCancelled(true);
                                } else
                                    player.sendMessage(ChatColor.RED + "请在同一个世界标记！");
                            } else {
                                // 发送标记信息
                                signB(player, pos);
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private void signA(Player player, Location pos) {
        int x = pos.getBlockX(), y = pos.getBlockY(), z = pos.getBlockZ();
        player.sendMessage(
                org.bukkit.ChatColor.YELLOW + "[SManage] 标记A点: " + x + ", " + y + ", " + z);
        SManage.regions.get(player.getName()).setALocation(new SLocation().setX(x).setY(y).setZ(z).setWorld(pos.getWorld().getName()));
        log.info(player.getName() + " 标记A点 " + x + ", " + y + ", " + z + " 于世界 "
                + pos.getWorld().getName());
    }

    private void signB(Player player, Location pos) {
        int x = pos.getBlockX(), y = pos.getBlockY(), z = pos.getBlockZ();
        player.sendMessage(
                org.bukkit.ChatColor.YELLOW + "[SManage] 标记B点: " + x + ", " + y + ", " + z);
        SManage.regions.get(player.getName()).setBLocation(new SLocation().setX(x).setY(y).setZ(z).setWorld(pos.getWorld().getName()));
        log.info(player.getName() + " 标记B点 " + x + ", " + y + ", " + z + " 于世界 "
                + pos.getWorld().getName());
    }

    private void manage(CreatureSpawnEvent event, Entity entity, boolean mode) {
        List<Integer> Speed;
        for (int index = 0; index < RegionDB.getSize(); index++) {
            Speed = RegionDB.retrieval(event.getLocation(), mode, index);
            if (Speed != null) {
                if (Speed.get(0) != -1) {
                    if (Speed.get(1) == 1) {
                        event.setCancelled(true);
                        return;
                    } else if (Speed.get(0) < Speed.get(1)) {
                        if (!event.isCancelled()) {
                            event.setCancelled(true);
                            if (mode)
                                RegionDB.setMonSpeed0(Speed.get(2), Speed.get(0) + 1);
                            else
                                RegionDB.setAniSpeed0(Speed.get(2), Speed.get(0) + 1);
                        }
                        index = Speed.get(2);
                    } else {
                        if (mode)
                            RegionDB.setMonSpeed0(Speed.get(2), 1);
                        else
                            RegionDB.setAniSpeed0(Speed.get(2), 1);
                        index = Speed.get(2);
                    }
                } else if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
                    for (int i = 1; i < Speed.get(1); i++) {
                        event.getLocation().getWorld().spawnEntity(event.getLocation(), entity.getType());
                    }
                    index = Speed.get(2);
                }
            } else
                return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster && !(entity instanceof Boss) || entity instanceof Slime) {
            manage(event, entity, true);
        } else if (entity instanceof Animals) {
            manage(event, entity, false);
        }
    }
}
