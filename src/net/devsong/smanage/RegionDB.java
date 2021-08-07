package net.devsong.smanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import net.devsong.smanage.language.SLanguages;
import net.devsong.smanage.listener.SManageListener;
import org.bukkit.Location;
import org.bukkit.Material;

public class RegionDB {
    public static List<Region> Regions;
    public static boolean defaultMonMode = true;
    public static boolean defaultAniMode = true;
    public static int defaultMonSpeed = 3;
    public static int defaultAniSpeed = 3;
    public static String defaultUniversal = "COMMON";

    private RegionDB() {
    }

    public static void Load(List<Region> list, Material material) {
        Regions = list;
        SManageListener.item = material;
    }

    public static void setSetter(Material m) {
        SManageListener.item = m;
    }

    public static Region getRegion(String name) {
        for (Region region : Regions)
            if (region.getName().equals(name))
                return region;
        return null;
    }

    public static boolean set(Region region, String player, String name) {
        Region re = getRegion(name);
        boolean flag;
        if (re == null) {
            Regions.add(region);
            region.setMonMode(defaultMonMode).setAniMode(defaultAniMode).setMonSpeed(Arrays.asList(1, defaultMonSpeed)).setAniSpeed(Arrays.asList(1, defaultAniSpeed)).setUniversal(defaultUniversal).setModifier(player).setModifyTime(getTime());
            flag = true;
        } else {
            re.setALocation(region.getALocation()).setBLocation(region.getBLocation());
            re.setModifier(player);
            re.setModifyTime(getTime());
            flag = false;
        }
        return flag;
    }

    public static boolean remove(String name) {
        for (Region region : Regions)
            if (region.getName().equals(name)) {
                Regions.remove(region);
                return true;
            }
        return false;
    }

    public static boolean rename(String name, String name_later) {
        for (Region region : Regions)
            if (region.getName().equals(name)) {
                region.setName(name_later);
                return true;
            }
        return false;
    }

    public static boolean setMonMode(boolean mode, String name, String player) {
        Region re = getRegion(name);
        if (re != null) {
            re.setMonMode(mode).setModifier(player).setModifyTime(getTime());
            return true;
        } else
            return false;
    }

    public static void setALLMonMode(boolean mode, String player) {
        for (Region region : Regions) {
            region.setMonMode(mode).setModifier(player).setModifyTime(getTime());
        }
    }

    public static boolean setAniMode(boolean mode, String name, String player) {
        Region re = getRegion(name);
        if (re != null) {
            re.setAniMode(mode).setModifier(player).setModifyTime(getTime());
            return true;
        } else
            return false;
    }

    public static void setALLAniMode(boolean mode, String player) {
        for (Region region : Regions) {
            region.setAniMode(mode).setModifier(player).setModifyTime(getTime());
        }
    }

    public static boolean setUniversal(String universal, String name, String player) {
        Region re = getRegion(name);
        if (re != null) {
            re.setUniversal(universal).setModifier(player).setModifyTime(getTime());
            return true;
        } else
            return false;
    }

    public static void setALLUniversal(String universal, String player) {
        for (Region region : Regions) {
            region.setUniversal(universal).setModifier(player).setModifyTime(getTime());
        }
    }

    public static void setMonSpeed0(int Index, int i) {
        Region region = Regions.get(Index);
        List<Integer> ints = region.getMonSpeed();
        ints.set(0, i);
        region.setMonSpeed(ints);
    }

    public static void setAniSpeed0(int Index, int i) {
        Region region = Regions.get(Index);
        List<Integer> ints = region.getAniSpeed();
        ints.set(0, i);
        region.setAniSpeed(ints);
    }

    public static void setALLMonSpeed1(int i, String player) {
        for (Region region : Regions) {
            region.setMonSpeed(Arrays.asList(1, i));
            region.setModifier(player);
            region.setModifyTime(getTime());
        }
    }

    public static boolean setMonSpeed1(String name, int i, String player) {
        for (Region region : Regions) {
            if (region.getName().equals(name)) {
                region.setMonSpeed(Arrays.asList(1, i));
                region.setModifier(player);
                region.setModifyTime(getTime());
                return true;
            }
        }
        return false;
    }

    public static void setALLAniSpeed1(int i, String player) {
        for (Region region : Regions) {
            region.setAniSpeed(Arrays.asList(1, i));
            region.setModifier(player);
            region.setModifyTime(getTime());
        }
    }

    public static boolean setAniSpeed1(String name, int i, String player) {
        for (Region region : Regions) {
            if (region.getName().equals(name)) {
                region.setAniSpeed(Arrays.asList(1, i));
                region.setModifier(player);
                region.setModifyTime(getTime());
                return true;
            }
        }
        return false;
    }

    public static String getDetail(String name) {
        String detail;
        for (Region region : Regions) {
            if (region.getName().equals(name)) {
                detail = SManage.info.get("d_name") + region.getName() + SManage.info.get("d_player") + region.getPlayer() + SManage.info.get("d_time") + region.getTime()
                        + SManage.info.get("d_modifier") + region.getModifier() + SManage.info.get("d_modifyTime") + region.getModifyTime() + SManage.info.get("d_world")
                        + getWorld(region.getALocation().getWorld()) + SManage.info.get("d_universal") + switch (region.getUniversal()) {
                    case COMMON -> SManage.info.get("ws_uni_common");
                    case WORLD -> SManage.info.get("ws_uni_world");
                    case SERVER -> SManage.info.get("ws_uni_server");
                };
                if (region.getUniversal() == SUniversal.COMMON)
                    detail += SManage.info.get("d_A") + " X: " + region.getALocation().getX() + ", Y: "
                            + region.getALocation().getY() + ", Z: " + region.getALocation().getZ() + SManage.info.get("d_B") + " X: "
                            + region.getBLocation().getX() + ", Y: " + region.getBLocation().getY() + ", Z: "
                            + region.getBLocation().getZ();
                detail += SManage.info.get("d_mon") + getSpeed(region.getMonSpeed().get(1), region.getMonMode())
                        + SManage.info.get("d_ani") + getSpeed(region.getAniSpeed().get(1), region.getAniMode());
                return detail;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        List<String> list = new ArrayList<>();
        for (Region region : Regions) {
            list.add(region.getName());
        }
        return list;
    }

    public static int getSize() {
        return Regions.size();
    }

    public static List<Integer> retrieval(Location location, boolean flag, int index) {
        List<Integer> i = new ArrayList<>();
        List<Integer> i1;
        SLocation APoint;
        SLocation BPoint;
        if (!Regions.isEmpty()) {
            for (Region region = Regions.get(index); index < getSize(); ) {
                APoint = region.getALocation();
                BPoint = region.getBLocation();
                if (region.getUniversal() == SUniversal.SERVER || region.getUniversal() == SUniversal.WORLD || (APoint.getX() <= location.getBlockX() && location.getBlockX() <= BPoint.getX())
                        || APoint.getX() >= location.getBlockX() && location.getBlockX() >= BPoint.getX())
                    if (region.getUniversal() == SUniversal.SERVER || region.getUniversal() == SUniversal.WORLD || location.getBlockY() > APoint.getY() || location.getBlockY() > BPoint.getY())
                        if (region.getUniversal() == SUniversal.SERVER || region.getUniversal() == SUniversal.WORLD || (APoint.getZ() <= location.getBlockZ() && location.getBlockZ() <= BPoint.getZ())
                                || APoint.getZ() >= location.getBlockZ() && location.getBlockZ() >= BPoint.getZ())
                            if (region.getUniversal() == SUniversal.SERVER || Objects.requireNonNull(location.getWorld()).getName().equals(APoint.getWorld())) {
                                if (flag ? region.getMonMode() : region.getAniMode()) {
                                    i1 = flag ? region.getMonSpeed() : region.getAniSpeed();
                                    i.add(i1.get(0));
                                    i.add(i1.get(1));
                                    i.add(Regions.indexOf(region));
                                    return i;
                                } else {
                                    i1 = flag ? region.getMonSpeed() : region.getAniSpeed();
                                    i.add(-1);
                                    i.add(i1.get(1));
                                    i.add(Regions.indexOf(region));
                                    return i;
                                }
                            }
                index++;
                if (index < getSize())
                    region = Regions.get(index);
            }
        }
        return null;
    }

    private static String getWorld(String name) {
        return switch (name) {
            case "world" -> SManage.info.get("ws_world");
            case "world_the_end" -> SManage.info.get("ws_end");
            case "world_nether" -> SManage.info.get("ws_nether");
            default -> name;
        };
    }

    private static String getSpeed(int i, boolean flag) {
        if (i < 0) i = 0;
        return flag ? switch (i) {
            case 0 -> SManage.info.get("ws_noLi");
            case 1 -> SManage.info.get("ws_noSp");
            default -> SManage.info.lang ? SManage.info.get("ws_li") + i : "1 / " + i + SManage.info.get("ws_li");
        } : switch (i) {
            case 0, 1 -> SManage.info.get("ws_noAc");
            default -> SManage.info.lang ? SManage.info.get("ws_ac") + i + "倍" : i + SManage.info.get("ws_ac");
        };
    }

    private static String getTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(now);
    }
}
