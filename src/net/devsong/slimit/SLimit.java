package net.devsong.slimit;

import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import net.devsong.slimit.listener.LimitListener;

public class SLimit extends JavaPlugin {
	public static HashMap<String, Region> regions = new HashMap<>();

	@Override
	public void onEnable() {
		ConfigurationSerialization.registerClass(Region.class);
		ArrayList<Region> list = (ArrayList<Region>) getConfig().getList("RegionList");
		String m = getConfig().getString("Setter", "APPLE");
		RegionDB.Load(Objects.requireNonNullElseGet(list, ArrayList::new), Material.getMaterial(m), this);
		getCommand("slimit").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(new LimitListener(getLogger()), this);
		super.onEnable();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "哈? 你输入这个干嘛? ");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("reload")) {
				reloadConfig();
				ArrayList<Region> list = (ArrayList<Region>) getConfig().getList("RegionList");
				String m = getConfig().getString("Setter", "APPLE");
				RegionDB.Load(Objects.requireNonNullElseGet(list, ArrayList::new), Material.getMaterial(m), this);
				sender.sendMessage("已重新加载配置");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("set")) {
				if (args.length != 1 && !args[1].trim().equals("")) {
					if (regions.get(sender.getName()) == null) {
						if (RegionDB.getRegion(args[1].trim()) != null) {
							regions.put(sender.getName(), RegionDB.getRegion(args[1].trim()));
							sender.sendMessage(ChatColor.YELLOW + "找到该区域，加载完成，请开始划定区域");
							getLogger()
									.info("玩家" + sender.getName() + "修改区域:" + regions.get(sender.getName()).getName());
						} else {
							Date now = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							String dateStr = sdf.format(now);
							regions.put(sender.getName(), new Region(args[1].trim(), sender.getName(), dateStr));
							sender.sendMessage(ChatColor.YELLOW + "未找到该区域，新建完成，请开始划定区域");
							getLogger()
									.info("玩家" + sender.getName() + "新建区域:" + regions.get(sender.getName()).getName());
						}
					} else
						sender.sendMessage(ChatColor.RED + "目前有正在进行的区域划定任务，如要继续设置，请先save或cancel当前的任务！");
				} else
					sender.sendMessage(ChatColor.RED + "请输入名称！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("save")) {
				if (regions.get(sender.getName()) != null) {
					if (regions.get(sender.getName()).getALocation() != null
							&& regions.get(sender.getName()).getBLocation() != null) {
						RegionDB.set(regions.get(sender.getName()), sender.getName(), regions.get(sender.getName()).getName());
						sender.sendMessage(ChatColor.YELLOW + "保存完毕！");
						regions.remove(sender.getName());
					} else
						sender.sendMessage(ChatColor.RED + "请设置A或B点！");
				} else
					sender.sendMessage(ChatColor.RED + "请先set一个区域！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("cancel")) {
				if (regions.get(sender.getName()) != null) {
					regions.remove(sender.getName());
					sender.sendMessage(ChatColor.YELLOW + "已取消");
				} else {
					sender.sendMessage(ChatColor.RED + "没有区域可以取消！");
				}
				return true;
			} else if (args[0].trim().equalsIgnoreCase("remove")) {
				if (args.length != 1 && !args[1].trim().equals("")) {
					if (RegionDB.remove(args[1].trim())) {
						sender.sendMessage(ChatColor.YELLOW + "删除成功！");
					} else {
						sender.sendMessage(ChatColor.RED + "未找到该区域！");
					}
				} else
					sender.sendMessage(ChatColor.RED + "请输入要删除的区域名称！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("setMonSpeed")) {
				if (args.length != 1 && !args[1].trim().equals("") && args[2] != null && !args[2].trim().equals("")) {
					Integer MonSpeed = null;
					try {
						MonSpeed = Integer.parseInt(args[2].trim());
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "速度请输入正确的数字！");
					}
					if (MonSpeed != null) {
						if (MonSpeed > -1) {
							if (RegionDB.setMonSpeed1(args[1].trim(), MonSpeed, sender.getName()))
								sender.sendMessage(ChatColor.YELLOW + "设置成功！");
							else
								sender.sendMessage(ChatColor.RED + "未找到该区域！");
						} else
							sender.sendMessage(ChatColor.RED + "速度请输入0及以上的数字！");
					}
				} else
					sender.sendMessage(ChatColor.RED + "请输入要设置的区域名称或速度！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("setALLMonSpeed")) {
				if (args[1] != null && !args[1].trim().equals("")) {
					Integer MonSpeed = null;
					try {
						MonSpeed = Integer.parseInt(args[1].trim());
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "速度请输入正确的数字！");
					}
					if (MonSpeed != null) {
						if (MonSpeed > -1) {
							RegionDB.setALLMonSpeed1(MonSpeed, sender.getName());
							sender.sendMessage(ChatColor.YELLOW + "设置成功！");
						} else
							sender.sendMessage(ChatColor.RED + "速度请输入0及以上的数字！");
					}
				} else
					sender.sendMessage(ChatColor.RED + "请输入要设置的速度！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("setAniSpeed")) {
				if (args.length != 1 && !args[1].trim().equals("") && args[2] != null && !args[2].trim().equals("")) {
					Integer AniSpeed = null;
					try {
						AniSpeed = Integer.parseInt(args[2].trim());
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "速度请输入正确的数字！");
					}
					if (AniSpeed != null) {
						if (AniSpeed > -1) {
							if (RegionDB.setAniSpeed1(args[1].trim(), AniSpeed, sender.getName()))
								sender.sendMessage(ChatColor.YELLOW + "设置成功！");
							else
								sender.sendMessage(ChatColor.RED + "未找到该区域！");
						} else
							sender.sendMessage(ChatColor.RED + "速度请输入0及以上的数字！");
					}
				} else
					sender.sendMessage(ChatColor.RED + "请输入要设置的区域名称或速度！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("setALLAniSpeed")) {
				if (args[1] != null && !args[1].trim().equals("")) {
					Integer AniSpeed = null;
					try {
						AniSpeed = Integer.parseInt(args[1].trim());
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "速度请输入正确的数字！");
					}
					if (AniSpeed != null) {
						if (AniSpeed > -1) {
							RegionDB.setALLAniSpeed1(AniSpeed, sender.getName());
							sender.sendMessage(ChatColor.YELLOW + "设置成功！");
						} else
							sender.sendMessage(ChatColor.RED + "速度请输入0及以上的数字！");
					}
				} else
					sender.sendMessage(ChatColor.RED + "请输入要设置的速度！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("list")) {
				List<String> list = RegionDB.getNames();
				if (!list.isEmpty()) {
					for (String name : list) {
						sender.sendMessage(ChatColor.AQUA + name);
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "暂时还没有区域！");
				}
				return true;
			} else if (args[0].trim().equalsIgnoreCase("detail")) {
				if (args.length != 1 && !args[1].trim().equals("")) {
					String detail = RegionDB.getDetail(args[1].trim());
					if (detail != null) {
						sender.sendMessage(ChatColor.AQUA + detail);
					} else
						sender.sendMessage(ChatColor.RED + "未找到该区域！");
				} else
					sender.sendMessage(ChatColor.RED + "请输入要查看的区域名称！");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("setter")) {
				Material m =((Player) sender).getItemInHand().getType();
				LimitListener.item = m;
				RegionDB.setSetter(m);
				sender.sendMessage(ChatColor.YELLOW + "设置完成! ");
				return true;
			} else if (args[0].trim().equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "\n所有指令的介绍(忽略大小写, 前面均加slimit)："
						+ "\n- help 获取帮助"
						+ "\n- [OP] reload 重新加载插件"
						+ "\n- [OP] set [NAME] 设置一个区域，若没有此区域将新建"
						+ "\n- [OP] save 保存已更改的区域"
						+ "\n- [OP] cancel 取消本次新建/修改区域"
						+ "\n- list 查看已保存的区域"
						+ "\n- detail [NAME] 查看某个区域的详情"
						+ "\n- [OP] remove [NAME] 删除某个区域"
						+ "\n- [OP] setMonSpeed [NAME] [NUMBER] 设置某个区域的怪物生成速度，规则为：降到原速的1/x，x为[NUMBER]，永不生成请输入1，暂不限制请输入0"
						+ "\n- [OP] setALLMonSpeed [NUMBER] 设置所有区域的怪物生成速度，规则同前"
						+ "\n- [OP] setAniSpeed [NAME] [NUMBER] 设置某个区域的动物生成速度，规则同前"
						+ "\n- [OP] setALLAniSpeed [NUMBER] 设置所有区域的动物生成速度，规则同前"
						+ "\n- [OP] setter 重设划定区域时手持的物品为当前手持物品，注意不要双持"
						+ "\n注: 1. 末影龙、凋零、老守卫者 不受限制; 2. [NAME]为区域名, [NUMBER]为一个数字");
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "位置指令! ");
		return true;
	}
}