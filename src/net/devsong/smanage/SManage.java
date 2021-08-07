package net.devsong.smanage;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import net.devsong.smanage.language.SLanguages;
import net.devsong.smanage.metrics.Metrics;
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
    public static SLanguages info;

    private void save() {
        getConfig().set("language", info.lang);
        getConfig().set("Setter", SManageListener.item.name());
        getConfig().set("defaultMonMode", RegionDB.defaultMonMode);
        getConfig().set("defaultAniMode", RegionDB.defaultAniMode);
        getConfig().set("defaultMonSpeed", RegionDB.defaultMonSpeed);
        getConfig().set("defaultAniSpeed", RegionDB.defaultAniSpeed);
        getConfig().set("defaultUniversal", RegionDB.defaultUniversal);
        getConfig().set("RegionList", RegionDB.Regions);
        saveConfig();
    }

    private void load() {
        info = new SLanguages(getConfig().getBoolean("language", false));
        String Setter = getConfig().getString("Setter", "APPLE");
        RegionDB.defaultMonMode = getConfig().getBoolean("defaultMonMode", true);
        RegionDB.defaultAniMode = getConfig().getBoolean("defaultAniMode", true);
        RegionDB.defaultMonSpeed = getConfig().getInt("defaultMonSpeed", 3);
        RegionDB.defaultAniSpeed = getConfig().getInt("defaultAniSpeed", 3);
        RegionDB.defaultUniversal = SUniversal.getInstance(getConfig().getString("defaultUniversal", "COMMON")).name();
        List<Region> list = (List<Region>) getConfig().getList("RegionList");
        RegionDB.Load(Objects.requireNonNullElseGet(list, ArrayList::new), Objects.requireNonNullElse(Material.getMaterial(Setter), Material.getMaterial("APPLE")));
    }

    @Override
    public void onEnable() {
        // 注册自建类
        ConfigurationSerialization.registerClass(Region.class);
        ConfigurationSerialization.registerClass(SLocation.class);

        // 读入配置文件
        load();

        // bStats统计
        Metrics metrics = new Metrics(this, 12287);

        getCommand("smanage").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new SManageListener(getLogger()), this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // 保存配置
        save();

        super.onDisable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + info.get("Huh"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("reload")) {
            if (sender.hasPermission("smanage.edit")) {
                reloadConfig();
                load();
                sender.sendMessage(ChatColor.GREEN + info.get("reload"));
                getLogger().info("Reloaded by " + sender.getName());
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
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
                            sender.sendMessage(ChatColor.YELLOW + info.get("deli"));
                            getLogger()
                                    .info(sender.getName() + info.get("set_log") + regions.get(sender.getName()).getName());
                        } else
                            sender.sendMessage(ChatColor.RED + info.get("set_err"));
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("name"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("perm"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("not_play"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("save")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    if (regions.get(sender.getName()) != null) {
                        if (regions.get(sender.getName()).getALocation() != null
                                && regions.get(sender.getName()).getBLocation() != null) {
                            if (RegionDB.set(regions.get(sender.getName()), sender.getName(), regions.get(sender.getName()).getName()))
                                sender.sendMessage(ChatColor.GREEN + info.get("save"));
                            else
                                sender.sendMessage(ChatColor.GREEN + info.get("reset"));
                            regions.remove(sender.getName());
                        } else
                            sender.sendMessage(ChatColor.RED + info.get("save_err_poi"));
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("save_err_reg"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("perm"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("not_play"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("cancel")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    if (regions.get(sender.getName()) != null) {
                        regions.remove(sender.getName());
                        sender.sendMessage(ChatColor.GREEN + info.get("cancel"));
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("cancel_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("perm"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("not_play"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("remove")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length != 1 && !args[1].trim().equals("")) {
                    if (RegionDB.remove(args[1].trim())) {
                        sender.sendMessage(ChatColor.GREEN + info.get("remove"));
                    } else {
                        sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("rename")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length != 1 && !args[1].trim().equals("")) {
                    if (RegionDB.rename(args[1].trim(), args[2].trim())) {
                        sender.sendMessage(ChatColor.GREEN + info.get("rename"));
                    } else {
                        sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setMonSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    Integer MonSpeed = null;
                    try {
                        MonSpeed = Integer.parseInt(args[2].trim());
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                    }
                    if (MonSpeed != null) {
                        if (RegionDB.setMonSpeed1(args[1].trim(), MonSpeed, sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLMonSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    Integer MonSpeed = null;
                    try {
                        MonSpeed = Integer.parseInt(args[1].trim());
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                    }
                    if (MonSpeed != null) {
                        RegionDB.setALLMonSpeed1(MonSpeed, sender.getName());
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setDefaultMonSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    Integer MonSpeed = null;
                    try {
                        MonSpeed = Integer.parseInt(args[1].trim());
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                    }
                    if (MonSpeed != null) {
                        RegionDB.defaultMonSpeed = MonSpeed;
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setAniSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    Integer AniSpeed = null;
                    try {
                        AniSpeed = Integer.parseInt(args[2].trim());
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                    }
                    if (AniSpeed != null) {
                        if (RegionDB.setAniSpeed1(args[1].trim(), AniSpeed, sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLAniSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    Integer AniSpeed = null;
                    try {
                        AniSpeed = Integer.parseInt(args[1].trim());
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                    }
                    if (AniSpeed != null) {
                        RegionDB.setALLAniSpeed1(AniSpeed, sender.getName());
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setDefaultAniSpeed")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    Integer AniSpeed = null;
                    try {
                        AniSpeed = Integer.parseInt(args[1].trim());
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                    }
                    if (AniSpeed != null) {
                        RegionDB.defaultAniSpeed = AniSpeed;
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                    }
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setUniversal")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    if (args[2].trim().equalsIgnoreCase("common"))
                        if (RegionDB.setUniversal("COMMON", args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else if (args[2].trim().equalsIgnoreCase("world"))
                        if (RegionDB.setUniversal("WORLD", args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else if (args[2].trim().equalsIgnoreCase("server"))
                        if (RegionDB.setUniversal("SERVER", args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLUniversal")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("common")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLUniversal("COMMON", sender.getName());
                    } else if (args[1].trim().equalsIgnoreCase("world")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLUniversal("WORLD", sender.getName());
                    } else if (args[1].trim().equalsIgnoreCase("server")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLUniversal("SERVER", sender.getName());
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setDefaultUniversal")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("common")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultUniversal = "COMMON";
                    } else if (args[1].trim().equalsIgnoreCase("world")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultUniversal = "WORLD";
                    } else if (args[1].trim().equalsIgnoreCase("server")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultUniversal = "SERVER";
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("list")) {
            if (sender.hasPermission("smanage.read")) {
                List<String> list = RegionDB.getNames();
                if (!list.isEmpty())
                    for (String name : list)
                        sender.sendMessage(ChatColor.AQUA + name);
                else
                    sender.sendMessage(ChatColor.YELLOW + info.get("no_reg"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("detail")) {
            if (sender.hasPermission("smanage.read")) {
                if (args.length != 1 && !args[1].trim().equals("")) {
                    String detail = RegionDB.getDetail(args[1].trim());
                    if (detail != null) {
                        sender.sendMessage(ChatColor.AQUA + detail);
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setter")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("smanage.edit")) {
                    Material m = ((Player) sender).getItemInHand().getType();
                    SManageListener.item = m;
                    RegionDB.setSetter(m);
                    sender.sendMessage(ChatColor.GREEN + info.get("set"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("perm"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("not_play"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setMonMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    if (args[2].trim().equalsIgnoreCase("L"))
                        if (RegionDB.setMonMode(true, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else if (args[2].trim().equalsIgnoreCase("A"))
                        if (RegionDB.setMonMode(false, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLMonMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("L")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLMonMode(true, sender.getName());
                    } else if (args[1].trim().equalsIgnoreCase("A")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLMonMode(false, sender.getName());
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setDefaultMonMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("L")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultMonMode = true;
                    } else if (args[1].trim().equalsIgnoreCase("A")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultMonMode = false;
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setAniMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 3 && !args[1].trim().equals("") && !args[2].trim().equals("")) {
                    if (args[2].trim().equalsIgnoreCase("L"))
                        if (RegionDB.setAniMode(true, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else if (args[2].trim().equalsIgnoreCase("A"))
                        if (RegionDB.setAniMode(false, args[1], sender.getName()))
                            sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        else
                            sender.sendMessage(ChatColor.RED + info.get("reg_nf"));
                    else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setALLAniMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("L")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLAniMode(true, sender.getName());
                    } else if (args[1].trim().equalsIgnoreCase("A")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.setALLAniMode(false, sender.getName());
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("setDefaultAniMode")) {
            if (sender.hasPermission("smanage.edit")) {
                if (args.length == 2 && !args[1].trim().equals("")) {
                    if (args[1].trim().equalsIgnoreCase("L")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultAniMode = true;
                    } else if (args[1].trim().equalsIgnoreCase("A")) {
                        sender.sendMessage(ChatColor.GREEN + info.get("set"));
                        RegionDB.defaultAniMode = false;
                    } else
                        sender.sendMessage(ChatColor.RED + info.get("for_err"));
                } else
                    sender.sendMessage(ChatColor.RED + info.get("for_err"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        } else if (args[0].trim().equalsIgnoreCase("help")) {
            if (sender.hasPermission("smanage.read")) {
                sender.sendMessage(ChatColor.AQUA + info.get("h_lead")
                        + "\n- help " + info.get("h_help")
                        + "\n- [OP] reload " + info.get("h_reload")
                        + "\n- [OP] set [NAME] " + info.get("h_set")
                        + "\n- [OP] save " + info.get("h_save")
                        + "\n- [OP] cancel " + info.get("h_cancel")
                        + "\n- list " + info.get("h_list")
                        + "\n- detail [NAME] " + info.get("h_detail")
                        + "\n- [OP] remove [NAME] " + info.get("h_remove")
                        + "\n- [OP] rename [NAME] [NAME] " + info.get("h_rename")
                        + "\n- [OP] setMonSpeed [NAME] [NUMBER] " + info.get("h_setMonSpeed")
                        + "\n- [OP] setALLMonSpeed [NUMBER] " + info.get("h_setALLMonSpeed")
                        + "\n- [OP] setDefaultMonSpeed [NUMBER] " + info.get("h_setDefaultMonSpeed")
                        + "\n- [OP] setAniSpeed [NAME] [NUMBER] " + info.get("h_setAniSpeed")
                        + "\n- [OP] setALLAniSpeed [NUMBER] " + info.get("h_setALLAniSpeed")
                        + "\n- [OP] setDefaultAniSpeed [NUMBER] " + info.get("h_setDefaultAniSpeed")
                        + "\n- [OP] setMonMode [NAME] [L / A] " + info.get("h_setMonMode")
                        + "\n- [OP] setALLMonMode [L / A] " + info.get("h_setALLMonMode")
                        + "\n- [OP] setDefaultMonMode [L / A] " + info.get("h_setDefaultMonMode")
                        + "\n- [OP] setAniMode [NAME] [L / A] " + info.get("h_setAniMode")
                        + "\n- [OP] setALLAniMode [L / A] " + info.get("h_setALLAniMode")
                        + "\n- [OP] setDefaultAniMode [L / A] " + info.get("h_setDefaultAniMode")
                        + "\n- [OP] setUniversal [NAME] [common / world / server] " + info.get("h_setUniversal")
                        + "\n- [OP] setALLUniversal [common / world / server] " + info.get("h_setALLUniversal")
                        + "\n- [OP] setDefaultUniversal [common / world / server] " + info.get("h_setDefaultUniversal")
                        + "\n- [OP] setter " + info.get("h_setter")
                        + info.get("h_tip"));
            } else
                sender.sendMessage(ChatColor.RED + info.get("perm"));
            return true;
        }
        sender.sendMessage(ChatColor.RED + info.get("com_err"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String
            alias, @NotNull String[] args) {
        String[] subComsOP = {"reload", "set", "save", "cancel", "list", "detail", "remove", "rename", "setMonSpeed", "setALLMonSpeed", "setDefaultMonSpeed", "setAniSpeed", "setALLAniSpeed", "setDefaultAniSpeed", "setUniversal", "setALLUniversal", "setDefaultUniversal", "setter", "setMonMode", "setALLMonMode", "setDefaultMonMode", "setAniMode", "setALLAniMode", "setDefaultAniMode", "help"};
        String[] subComs = {"list", "detail", "help"};
        String[] modes = {"L", "A"};
        String[] universal = {"common", "world", "server"};
        if (sender.hasPermission("smanage.edit"))
            switch (args.length) {
                case 1:
                    return Arrays.stream(subComsOP).filter(s -> s.toLowerCase().contains(args[0].toLowerCase())).collect(Collectors.toList());
                case 2:
                    if (args[0].trim().equalsIgnoreCase("set") || args[0].trim().equalsIgnoreCase("detail") || args[0].trim().equalsIgnoreCase("remove") || args[0].trim().equalsIgnoreCase("rename") || args[0].trim().equalsIgnoreCase("setMonSpeed") || args[0].trim().equalsIgnoreCase("setAniSpeed") || args[0].trim().equalsIgnoreCase("setMonMode") || args[0].trim().equalsIgnoreCase("setAniMode") || args[0].trim().equalsIgnoreCase("setUniversal"))
                        return Arrays.stream(RegionDB.getNames().toArray(new String[0])).filter(s -> s.contains(args[1])).collect(Collectors.toList());
                    if (args[0].trim().equalsIgnoreCase("setALLMonMode") || args[0].trim().equalsIgnoreCase("setALLAniMode") || args[0].trim().equalsIgnoreCase("setDefaultMonMode") || args[0].trim().equalsIgnoreCase("setDefaultAniMode"))
                        return Arrays.stream(modes).filter(s -> s.toLowerCase().contains(args[1].toLowerCase())).collect(Collectors.toList());
                    if (args[0].trim().equalsIgnoreCase("setALLUniversal") || args[0].trim().equalsIgnoreCase("setDefaultUniversal"))
                        return Arrays.stream(universal).filter(s -> s.toLowerCase().contains(args[1].toLowerCase())).collect(Collectors.toList());
                    return new ArrayList<>();
                case 3:
                    if (args[0].trim().equalsIgnoreCase("setMonMode") || args[0].trim().equalsIgnoreCase("setAniMode"))
                        return Arrays.stream(modes).filter(s -> s.toLowerCase().contains(args[2].toLowerCase())).collect(Collectors.toList());
                    if (args[0].trim().equalsIgnoreCase("setUniversal"))
                        return Arrays.stream(universal).filter(s -> s.toLowerCase().contains(args[2].toLowerCase())).collect(Collectors.toList());
                    return new ArrayList<>();
                default:
                    return new ArrayList<>();
            }
        else if (sender.hasPermission("smanage.read"))
            switch (args.length) {
                case 1:
                    return Arrays.stream(subComs).filter(s -> s.toLowerCase().contains(args[0].toLowerCase())).collect(Collectors.toList());
                case 2:
                    if (args[0].trim().equalsIgnoreCase("detail"))
                        return Arrays.stream(RegionDB.getNames().toArray(new String[0])).filter(s -> s.contains(args[1])).collect(Collectors.toList());
                default:
                    return new ArrayList<>();
            }
        else
            return new ArrayList<>();
    }
}