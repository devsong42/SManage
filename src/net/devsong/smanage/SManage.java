package net.devsong.smanage;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.devsong.smanage.listener.SManageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SManage extends JavaPlugin {
    public static HashMap<String, Region> regions = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Region.class);
        ConfigurationSerialization.registerClass(SLocation.class);
        ArrayList<Region> list = (ArrayList<Region>) getConfig().getList("RegionList");
        String m = getConfig().getString("Setter", "APPLE");
        RegionDB.Load(Objects.requireNonNullElseGet(list, ArrayList::new), Material.getMaterial(m), this);
        Objects.requireNonNull(getCommand("smanage")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new SManageListener(getLogger()), this);
        super.onEnable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "哈? 你输入这个干嘛? ");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("reload")) {
            if (sender.hasPermission("smanage.edit")) {
                reloadConfig();
                ArrayList<Region> list = (ArrayList<Region>) getConfig().getList("RegionList");
                String m = getConfig().getString("Setter", "APPLE");
                RegionDB.Load(Objects.requireNonNullElseGet(list, ArrayList::new), Material.getMaterial(m), this);
                sender.sendMessage(ChatColor.YELLOW + "已重新加载配置");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("set")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    if (args.length != 1 && !args[1].trim().equals("")) {
                        if (regions.get(sender.getName()) == null) {
                            Date now = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String dateStr = sdf.format(now);
                            regions.put(sender.getName(), new Region(args[1].trim(), sender.getName(), dateStr));
                            sender.sendMessage(ChatColor.YELLOW + "请开始划定区域");
                            getLogger()
                                    .info("玩家" + sender.getName() + "设置区域:" + regions.get(sender.getName()).getName());
                        } else
                            sender.sendMessage(ChatColor.RED + "目前有正在进行的区域划定任务，如要继续设置，请先save或cancel当前的任务！");
                    } else
                        sender.sendMessage(ChatColor.RED + "请输入名称！");
                } else
                    sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎不是一个玩家...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("save")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    if (regions.get(sender.getName()) != null) {
                        if (regions.get(sender.getName()).getALocation() != null
                                && regions.get(sender.getName()).getBLocation() != null) {
                            if (RegionDB.set(regions.get(sender.getName()), sender.getName(), regions.get(sender.getName()).getName()))
                                sender.sendMessage(ChatColor.YELLOW + "保存新的区域完毕！");
                            else
                                sender.sendMessage(ChatColor.YELLOW + "重设区域完毕！");
                            regions.remove(sender.getName());
                        } else
                            sender.sendMessage(ChatColor.RED + "请设置A或B点！");
                    } else
                        sender.sendMessage(ChatColor.RED + "请先set一个区域！");
                } else
                    sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎不是一个玩家...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("cancel")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    if (regions.get(sender.getName()) != null) {
                        regions.remove(sender.getName());
                        sender.sendMessage(ChatColor.YELLOW + "已取消");
                    } else
                        sender.sendMessage(ChatColor.RED + "没有区域可以取消！");
                } else
                    sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎不是一个玩家...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("remove")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length != 1 && !args[1].trim().equals("")) {
                    if (RegionDB.remove(args[1].trim())) {
                        sender.sendMessage(ChatColor.YELLOW + "删除成功！");
                    } else {
                        sender.sendMessage(ChatColor.RED + "未找到该区域！");
                    }
                } else
                    sender.sendMessage(ChatColor.RED + "请输入要删除的区域名称！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setMonSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
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
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLMonSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
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
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setAniSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
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
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLAniSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
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
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("list")) {
            if (sender.hasPermission("smanage.read")) {
                List<String> list = RegionDB.getNames();
                if (!list.isEmpty())
                    for (String name : list)
                        sender.sendMessage(ChatColor.AQUA + name);
                else
                    sender.sendMessage(ChatColor.YELLOW + "暂时还没有区域！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("detail")) {
            if (sender.hasPermission("smanage.read")) {
                if (args.length != 1 && !args[1].trim().equals("")) {
                    String detail = RegionDB.getDetail(args[1].trim());
                    if (detail != null) {
                        sender.sendMessage(ChatColor.AQUA + detail);
                    } else
                        sender.sendMessage(ChatColor.RED + "未找到该区域！");
                } else
                    sender.sendMessage(ChatColor.RED + "请输入要查看的区域名称！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setter")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    Material m = ((Player) sender).getItemInHand().getType();
                    SManageListener.item = m;
                    RegionDB.setSetter(m);
                    sender.sendMessage(ChatColor.YELLOW + "设置完成! ");
                } else
                    sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎不是一个玩家...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setMonMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    if (args[2].trim().equalsIgnoreCase("L"))
                        if (RegionDB.setMonMode(true, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        else
                            sender.sendMessage(ChatColor.RED + "未找到该区域！");
                    else if (args[2].trim().equalsIgnoreCase("A"))
                        if (RegionDB.setMonMode(false, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        else
                            sender.sendMessage(ChatColor.RED + "未找到该区域！");
                    else
                        sender.sendMessage(ChatColor.RED + "请输入正确的模式代号(L 限制模式 / A 加速模式)！");
                } else
                    sender.sendMessage(ChatColor.RED + "格式错误！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLMonMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("L")) {
                        sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        RegionDB.setALLMonMode(true, sender.getName());
                    } else if (args[1].trim().equalsIgnoreCase("A")) {
                        sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        RegionDB.setALLMonMode(false, sender.getName());
                    } else
                        sender.sendMessage(ChatColor.RED + "请输入正确的模式代号(L 限制模式 / A 加速模式)！");
                } else
                    sender.sendMessage(ChatColor.RED + "格式错误！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setAniMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    if (args[2].trim().equalsIgnoreCase("L"))
                        if (RegionDB.setAniMode(true, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        else
                            sender.sendMessage(ChatColor.RED + "未找到该区域！");
                    else if (args[2].trim().equalsIgnoreCase("A"))
                        if (RegionDB.setAniMode(false, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        else
                            sender.sendMessage(ChatColor.RED + "未找到该区域！");
                    else
                        sender.sendMessage(ChatColor.RED + "请输入正确的模式代号(L 限制模式 / A 加速模式)！");
                } else
                    sender.sendMessage(ChatColor.RED + "格式错误！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLAniMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("L")) {
                        sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        RegionDB.setALLAniMode(true, sender.getName());
                    } else if (args[1].trim().equalsIgnoreCase("A")) {
                        sender.sendMessage(ChatColor.YELLOW + "设置完成！");
                        RegionDB.setALLAniMode(false, sender.getName());
                    } else
                        sender.sendMessage(ChatColor.RED + "请输入正确的模式代号(L 限制模式 / A 加速模式)！");
                } else
                    sender.sendMessage(ChatColor.RED + "格式错误！");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        } else if (args[0].trim().equalsIgnoreCase("help")) {
            if (sender.hasPermission("smanage.read")) {
            sender.sendMessage(ChatColor.AQUA + "\n所有指令的介绍(忽略大小写, 前面均加sm)："
                    + "\n- help 获取帮助"
                    + "\n- [OP] reload 重新加载插件"
                    + "\n- [OP] set [NAME] 设置一个区域，若没有此区域将新建"
                    + "\n- [OP] save 保存已更改的区域"
                    + "\n- [OP] cancel 取消本次新建/修改区域"
                    + "\n- list 查看已保存的区域"
                    + "\n- detail [NAME] 查看某个区域的详情"
                    + "\n- [OP] remove [NAME] 删除某个区域"
                    + "\n- [OP] setMonSpeed [NAME] [NUMBER] 设置某个区域的怪物生成速度：当模式为限制模式时，降到原速的x分之一（默认x=3），当x=1时，永不生成，x=0时，暂不限制；为加速模式时，加速到原速的x倍（默认3），x=0或1时，暂不加速"
                    + "\n- [OP] setALLMonSpeed [NUMBER] 设置所有区域的怪物生成速度，规则同前"
                    + "\n- [OP] setAniSpeed [NAME] [NUMBER] 设置某个区域的动物生成速度，规则同前"
                    + "\n- [OP] setALLAniSpeed [NUMBER] 设置所有区域的动物生成速度，规则同前"
                    + "\n- [OP] setMonMode [NAME] [L / A] 设置某个区域的怪物生成的管理模式，L为限制模式，A为加速模式，默认为L"
                    + "\n- [OP] setALLMonMode [L / A] 设置所有区域的怪物生成速度，规则同前"
                    + "\n- [OP] setAniMode [NAME] [L / A] 设置某个区域的动物生成速度，规则同前"
                    + "\n- [OP] setALLAniMode [L / A] 设置所有区域的动物生成速度，规则同前"
                    + "\n- [OP] setter 重设划定区域时手持的物品为当前手持物品，注意不要双持"
                    + "\n注: 1. 末影龙、凋零不受管理; 2. [NAME]为区域名, [NUMBER]为一个数字");
            } else
                sender.sendMessage(ChatColor.RED + "你似乎没有足够的权限...");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "未知指令! ");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String
            alias, @NotNull String[] args) {
        String[] subComsOP = {"reload", "set", "save", "cancel", "list", "detail", "remove", "setmonspeed", "setallmonspeed", "setanispeed", "setallanispeed", "setter", "setmonmode", "setallmonmode", "setanimode", "setallanimode", "help"};
        String[] subComs = {"list", "detail", "help"};
        if (sender.hasPermission("smanage.edit"))
            switch (args.length) {
                case 1:
                    return Arrays.stream(subComsOP).filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
                case 2:
                    if (args[0].trim().equalsIgnoreCase("set") || args[0].trim().equalsIgnoreCase("detail") || args[0].trim().equalsIgnoreCase("remove") || args[0].trim().equalsIgnoreCase("setMonSpeed") || args[0].trim().equalsIgnoreCase("setAniSpeed") || args[0].trim().equalsIgnoreCase("setMonMode") || args[0].trim().equalsIgnoreCase("setAniMode"))
                        return Arrays.stream(RegionDB.getNames().toArray(new String[0])).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                    else if (args[0].trim().equalsIgnoreCase("setALLMonMode") || args[0].trim().equalsIgnoreCase("setALLAniMode"))
                        return Arrays.stream(new String[]{"L", "A"}).filter(s -> s.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                case 3:
                    if (args[0].trim().equalsIgnoreCase("setMonMode") || args[0].trim().equalsIgnoreCase("setAniMode"))
                        return Arrays.stream(new String[]{"L", "A"}).filter(s -> s.startsWith(args[2].toLowerCase())).collect(Collectors.toList());
                default:
                    return new ArrayList<>();
            }
        else if (sender.hasPermission("smanage.read"))
            switch (args.length) {
                case 1:
                    return Arrays.stream(subComs).filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
                case 2:
                    if (args[0].trim().equalsIgnoreCase("detail"))
                        return Arrays.stream(RegionDB.getNames().toArray(new String[0])).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                default:
                    return new ArrayList<>();
            }
        else
            return new ArrayList<>();
    }
}