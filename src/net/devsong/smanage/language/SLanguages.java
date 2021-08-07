package net.devsong.smanage.language;

import java.util.HashMap;

public class SLanguages extends HashMap<String, String> {
    public boolean lang;

    @Override
    public String put(String key, String value) {
        if (!key.startsWith("h_") && !key.startsWith("d_") && !key.startsWith("ws_") && !key.startsWith("si_"))
            return super.put(key, "[SManage] " + value);
        else
            return super.put(key, value);
    }

    @Override
    public String get(Object key) {
        if (key.toString().startsWith("d_"))
            return "\n" + super.get(key);
        else
            return super.get(key);
    }

    public SLanguages(boolean lang) {
        this.lang = lang;
        this.put("Huh", lang ? "哈? 你输入这个干嘛? " : "Huh? Why did you enter this? ");
        this.put("for_err", lang ? "格式错误！请看help" : "Format error! See help. ");
        this.put("reload", lang ? "重新加载完成" : "Reloaded config. ");
        this.put("deli", lang ? "请开始划定区域" : "Please start delimiting the region. ");
        this.put("set_log", lang ? "设置区域: " : "Set region: ");
        this.put("set_err", lang ? "目前有正在进行的区域划定任务，如要继续设置，请先save或cancel当前的任务！" : "There is an ongoing region delineation task. To continue setting, please save or cancel the current task first! ");
        this.put("name", lang ? "请输入名称！" : "Please enter a name! ");
        this.put("perm", lang ? "你似乎没有足够的权限..." : "You don't seem to have enough permissions... ");
        this.put("not_play", lang ? "你似乎不是一个玩家..." : "You don't seem to be a player... ");
        this.put("save", lang ? "保存新的区域完毕！" : "Save complete. ");
        this.put("reset", lang ? "重设区域完毕！" : "Reset complete");
        this.put("save_err_poi", lang ? "请设置A或B点！" : "Please set point A or B!");
        this.put("save_err_reg", lang ? "请先set一个区域！" : "Please set a region first!");
        this.put("cancel", lang ? "已取消" : "Canceled. ");
        this.put("cancel_err", lang ? "没有区域可以取消！" : "No region to cancel! ");
        this.put("remove", lang ? "删除成功！" : "Remove succeeded. ");
        this.put("reg_nf", lang ? "未找到该区域！" : "The region was not found! ");
        this.put("rename", lang ? "重命名成功！" : "Rename succeeded. ");
        this.put("set", lang ? "设置成功！" : "Set succeeded. ");
        this.put("no_reg", lang ? "暂时还没有区域！" : "No region yet! ");
        this.put("com_err", lang ? "未知指令！" : "Unknown command! ");
        this.put("h_lead", lang ? "\n所有指令的介绍(忽略大小写, 前面均加sm)：" : "\nIntroduction to all instructions (ignore case and add sm in front): ");
        this.put("h_help", lang ? "获取帮助" : "Get help. ");
        this.put("h_reload", lang ? "重新加载配置文件" : "Reload config file. ");
        this.put("h_set", lang ? "设置一个区域，若没有此区域将新建" : "Set a region. If there is no specified region, it will be created. ");
        this.put("h_save", lang ? "保存已更改的区域" : "Save changed region. ");
        this.put("h_cancel", lang ? "取消本次新建/修改区域任务" : "Cancel the new / modify region task. ");
        this.put("h_list", lang ? "查看区域列表" : "View region list. ");
        this.put("h_detail", lang ? "查看某个区域的详情" : "View the details of a region. ");
        this.put("h_remove", lang ? "删除某个区域" : "Remove a region. ");
        this.put("h_rename", lang ? "重命名某个区域" : "Rename a region. ");
        this.put("h_setMonSpeed", lang ? "设置某个区域的怪物生成速度：当模式为限制模式时，降到原速的x分之一（默认值见配置文件），当x=1时，永不生成，x<=0时，暂不限制；为加速模式时，加速到原速的x倍（），x<=1时，暂不加速" : "Set the monsters' spawning speed of a region: when the mode is limited mode, it will be reduced to 1/x of the original speed (see config file for default values), when x = 1, it will never be generated, and when x <= 0, it will not be limited temporarily; When in acceleration mode, accelerate to x times the original speed (see config file for default values). When x <= 1, do not accelerate temporarily. ");
        this.put("h_setALLMonSpeed", lang ? "设置所有区域的怪物生成速度，规则同前" : "Set the monsters' spawning speed in all regions. The rules are the same as before. ");
        this.put("h_setDefaultMonSpeed", lang ? "设置创建区域时默认的的怪物生成速度，规则同前" : "Set the default monsters' spawning speed when creating a region. The rules are the same as before. ");
        this.put("h_setAniSpeed", lang ? "设置某个区域的动物生成速度，规则同前" : "Set the animals' spawning speed in a certain region. The rules are the same as before. ");
        this.put("h_setALLAniSpeed", lang ? "设置所有区域的动物生成速度，规则同前" : "Set the animals' spawning speed in all regions. The rules are the same as before. ");
        this.put("h_setDefaultAniSpeed", lang ? "设置创建区域时默认的的动物生成速度，规则同前" : "Set the default animals' spawning speed when creating a region. The rules are the same as before. ");
        this.put("h_setMonMode", lang ? "设置某个区域的怪物生成速度的管理模式，L为限制模式，A为加速模式，默认值见配置文件" : "Set the management mode of monsters' spawning speed in a region. L is the limit mode and A is the acceleration mode. See the configuration file for the default value. ");
        this.put("h_setALLMonMode", lang ? "设置所有区域的怪物生成速度的管理模式，规则同前" : "Set the management mode of monsters' spawning speed in all regions. The rules are the same as before. ");
        this.put("h_setDefaultMonMode", lang ? "设置创建区域时默认的怪物生成速度的管理模式，规则同前" : "Set the management mode of the default monsters' spawning speed when creating a region. The rules are the same as before. ");
        this.put("h_setAniMode", lang ? "设置某个区域的动物生成速度的管理模式，规则同前" : "Set the management mode of animals' spawning speed in a certain region. The rules are the same as before. ");
        this.put("h_setALLAniMode", lang ? "设置所有区域的动物生成速度的管理模式，规则同前" : "Set the management mode of animals' spawning speed in all regions, and the rules are the same as before. ");
        this.put("h_setDefaultAniMode", lang ? "设置创建区域时默认的动物生成速度的管理模式，规则同前" : "Set the default management mode of animals' spawning speed when creating a region. The rules are the same as before. ");
        this.put("h_setUniversal", lang ? "设置某个区域的通用性，common为普通(划定的区域的AB点XY内，较低点Y以上的范围内生效)，world为区域所在世界生效，server为全服生效，默认值见配置文件" : "Set the universality of a region. common is common (within the AB point XY of the delimited region, effective within the range above the lower point y), world is the world where the region is located, and server is the full service effective. See the configuration file for the default value. ");
        this.put("h_setALLUniversal", lang ? "设置所有区域的通用性，规则同前" : "Set the universality of all regions, and the rules are the same as before. ");
        this.put("h_setDefaultUniversal", lang ? "设置创建区域时默认的通用性，规则同前" : "Set the default universality when creating a region. The rules are the same as before. ");
        this.put("h_setter", lang ? "划定区域时手持的物品为当前手持物品，注意不要双持" : "The item on your hand when delimiting the region. Be careful not to hold them both. ");
        this.put("h_tip", lang ? "\n注: 1. 末影龙、凋零不受管理; 2. [NAME]为区域名, [NUMBER]为一个数字" : "Note: 1. The End Dragon and withering are not managed; 2. [name] is the region's name and [NUMBER] is a number. ");
        this.put("d_name", lang ? "名称：" : "Name: ");
        this.put("d_player", lang ? "创建者：" : "Creator: ");
        this.put("d_time", lang ? "创建时间：" : "Creation time: ");
        this.put("d_modifier", lang ? "最近修改者：" : "Last modified by: ");
        this.put("d_modifyTime", lang ? "最近修改时间：" : "Last modified: ");
        this.put("d_world", lang ? "世界：" : "World: ");
        this.put("d_universal", lang ? "通用性：" : "Universality: ");
        this.put("d_A", lang ? "A点：" : "A Point: ");
        this.put("d_B", lang ? "B点：" : "B Point: ");
        this.put("d_mon", lang ? "怪物生成速度：" : "Monsters' spawning speed: ");
        this.put("d_ani", lang ? "动物生成速度：" : "Animals' spawning speed: ");
        this.put("ws_uni_common", lang ? "普通" : "common");
        this.put("ws_uni_world", lang ? "世界" : "world");
        this.put("ws_uni_server", lang ? "服务器" : "server");
        this.put("ws_world", lang ? "主世界" : "World");
        this.put("ws_end", lang ? "末路之地" : "The End");
        this.put("ws_nether", lang ? "下界地狱" : "The Nether");
        this.put("ws_noLi", lang ? "暂不限制" : "Not limited temporarily");
        this.put("ws_noSp", lang ? "永不生成" : "Never spawn");
        this.put("ws_li", lang ? "原速度的1/" : " of the original speed");
        this.put("ws_noAc", lang ? "暂不加速" : "Not accelerated temporarily");
        this.put("ws_ac", lang ? "原速度的" : " times the original speed");
        this.put("sign_err", lang ? "请在同一个世界标记！" : "Please sign in the same world! ");
        this.put("si_A", lang ? "标记A点：" : "Sign Point A: ");
        this.put("si_B", lang ? "标记B点：" : "Sign Point B: ");
        this.put("si_world", lang ? " 于 " : " in ");
    }
}
