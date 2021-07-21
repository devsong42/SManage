package net.devsong.smanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import net.devsong.smanage.listener.SManageListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class RegionDB {
	private static ArrayList<Region> Regions;
	private static JavaPlugin javaPlugin;

	private RegionDB() {
	}

	public static void Load(ArrayList<Region> list, Material material, JavaPlugin javaplugin) {
		Regions = list;
		SManageListener.item = material;
		javaPlugin = javaplugin;
	}

	public static void setSetter(Material m) {
		SManageListener.item = m;
		javaPlugin.getConfig().set("Setter", m.name());
		javaPlugin.saveConfig();
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
			region.setModifier(player);
			region.setModifyTime(getTime());
			flag = true;
		} else {
			re.setALocation(region.getALocation()).setBLocation(region.getBLocation());
			re.setModifier(player);
			re.setModifyTime(getTime());
			flag = false;
		}
		javaPlugin.getConfig().set("RegionList", Regions);
		javaPlugin.saveConfig();
		return flag;
	}

	public static boolean remove(String name) {
		for (Region region : Regions)
			if (region.getName().equals(name)) {
				Regions.remove(region);
				javaPlugin.getConfig().set("RegionList", Regions);
				javaPlugin.saveConfig();
				return true;
			}
		return false;
	}

	public static boolean setMonMode(boolean mode, String name, String player) {
		Region re = getRegion(name);
		if (re != null) {
			re.setMonMode(mode).setModifier(player).setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
			return true;
		} else
			return false;
	}

	public static void setALLMonMode(boolean mode, String player) {
		for (Region region : Regions) {
			region.setMonMode(mode).setModifier(player).setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
		}
	}

	public static boolean setAniMode(boolean mode, String name, String player) {
		Region re = getRegion(name);
		if (re != null) {
			re.setAniMode(mode).setModifier(player).setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
			return true;
		} else
			return false;
	}

	public static void setALLAniMode(boolean mode, String player) {
		for (Region region : Regions) {
			region.setAniMode(mode).setModifier(player).setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
		}
	}

	public static boolean setUniversal(boolean universal, String name, String player) {
		Region re = getRegion(name);
		if (re != null) {
			re.setUniversal(universal).setModifier(player).setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
			return true;
		} else
			return false;
	}

	public static void setALLUniversal(boolean universal, String player) {
		for (Region region : Regions) {
			region.setUniversal(universal).setModifier(player).setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
		}
	}

	public static void setMonSpeed0(int Index, int i) {
		Region region = Regions.get(Index);
		List<Integer> ints = region.getMonSpeed();
		ints.set(0, i);
		region.setMonSpeed(ints);
		javaPlugin.getConfig().set("RegionList", Regions);
		javaPlugin.saveConfig();
	}

	public static void setAniSpeed0(int Index, int i) {
		Region region = Regions.get(Index);
		List<Integer> ints = region.getAniSpeed();
		ints.set(0, i);
		region.setAniSpeed(ints);
		javaPlugin.getConfig().set("RegionList", Regions);
		javaPlugin.saveConfig();
	}

	public static void setALLMonSpeed1(int i, String player) {
		for (Region region : Regions) {
			region.setMonSpeed(Arrays.asList(1, i));
			region.setModifier(player);
			region.setModifyTime(getTime());
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
		}
	}

	public static boolean setMonSpeed1(String name, int i, String player) {
		for (Region region : Regions) {
			if (region.getName().equals(name)) {
				region.setMonSpeed(Arrays.asList(1, i));
				region.setModifier(player);
				region.setModifyTime(getTime());
				javaPlugin.getConfig().set("RegionList", Regions);
				javaPlugin.saveConfig();
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
			javaPlugin.getConfig().set("RegionList", Regions);
			javaPlugin.saveConfig();
		}
	}

	public static boolean setAniSpeed1(String name, int i, String player) {
		for (Region region : Regions) {
			if (region.getName().equals(name)) {
				region.setAniSpeed(Arrays.asList(1, i));
				region.setModifier(player);
				region.setModifyTime(getTime());
				javaPlugin.getConfig().set("RegionList", Regions);
				javaPlugin.saveConfig();
				return true;
			}
		}
		return false;
	}

	public static String getDetail(String name) {
		String detail;
		for (Region region : Regions) {
			if (region.getName().equals(name)) {
				detail = "名称：" + region.getName() + "\n创建者： " + region.getPlayer() + "\n创建时间： " + region.getTime()
						+ "\n最近修改者： " + region.getModifier() + "\n最近修改时间： " + region.getModifyTime() + "\n世界： "
						+ getWorld(region.getALocation().getWorld());
				if (region.getUniversal())
					detail += "\n世界通用性：是";
				else
					detail += "\nA点： X:" + region.getALocation().getX() + ", Y:"
							+ region.getALocation().getY() + ", Z:" + region.getALocation().getZ() + "\nB点： X:"
							+ region.getBLocation().getX() + ", Y:" + region.getBLocation().getY() + ", Z:"
							+ region.getBLocation().getZ();
				detail += "\n怪物生成速度：" + getSpeed(region.getMonSpeed().get(1), region.getMonMode())
						+ "\n动物生成速度：" + getSpeed(region.getAniSpeed().get(1), region.getAniMode());
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

	public static List<Integer> retrieval(Location location, boolean flag) {
		List<Integer> i = new ArrayList<>();
		List<Integer> i1;
		SLocation APoint;
		SLocation BPoint;
		if (!Regions.isEmpty()) {
			for (Region region : Regions) {
				APoint = region.getALocation();
				BPoint = region.getBLocation();
				if (APoint == null || BPoint == null)
					continue;
				if (region.getUniversal() || (APoint.getX() <= location.getBlockX() && location.getBlockX() <= BPoint.getX())
						|| APoint.getX() >= location.getBlockX() && location.getBlockX() >= BPoint.getX())
					if (region.getUniversal() || location.getBlockY() > APoint.getY() || location.getBlockY() > BPoint.getY())
						if (region.getUniversal() || (APoint.getZ() <= location.getBlockZ() && location.getBlockZ() <= BPoint.getZ())
								|| APoint.getZ() >= location.getBlockZ() && location.getBlockZ() >= BPoint.getZ())
							if (Objects.requireNonNull(location.getWorld()).getName().equals(APoint.getWorld())) {
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
			}
		}
		return null;
	}

	private static String getWorld(String name) {
		return switch (name) {
			case "world" -> "主世界";
			case "world_the_end" -> "末影之地";
			case "world_nether" -> "下界地狱";
			default -> name;
		};
	}

	private static String getSpeed(int i, boolean flag) {
		return flag ? switch (i) {
			case 0 -> "暂不限制";
			case 1 -> "永不生成";
			default -> "原速度的1/" + i;
		} : switch (i) {
			case 0, 1 -> "暂不加速";
			default -> "原速度的" + i + "倍";
		};
	}

	private static String getTime() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(now);
	}
}
