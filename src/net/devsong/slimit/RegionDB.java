package net.devsong.slimit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import net.devsong.slimit.listener.LimitListener;
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
		LimitListener.item = material;
		javaPlugin = javaplugin;
	}

	public static void setSetter(Material m) {
		LimitListener.item = m;
		javaPlugin.getConfig().set("Setter", m.name());
		javaPlugin.saveConfig();
	}

	public static Region getRegion(String name) {
		for (Region region : Regions)
			if (region.getName().equals(name))
				return region;
		return null;
	}

	public static void set(Region region, String player, String name) {
		Region re = getRegion(name);
		if (re == null) {
			Regions.add(region);
			region.setModifier(player);
			region.setModifyTime(getTime());
		} else {
			re.setModifier(player);
			re.setModifyTime(getTime());
		}
		javaPlugin.getConfig().set("RegionList", Regions);
		javaPlugin.saveConfig();
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
						+ getWorld(region.getALocation()) + "\nA点： X:" + region.getALocation().getX() + ", Y:"
						+ region.getALocation().getY() + ", Z:" + region.getALocation().getZ() + "\nB点： X:"
						+ region.getBLocation().getX() + ", Y:" + region.getBLocation().getY() + ", Z:"
						+ region.getBLocation().getZ() + "\n怪物限制速度：" + getMonSpeed(region.getMonSpeed().get(1))
						+ "\n动物限制速度：" + getAniSpeed(region.getAniSpeed().get(1));
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
		Location APoint;
		Location BPoint;
		if (!Regions.isEmpty()) {
			for (Region region : Regions) {
				APoint = region.getALocation();
				BPoint = region.getBLocation();
				if ((APoint.getX() <= location.getX() && location.getX() <= BPoint.getX())
						|| APoint.getX() >= location.getX() && location.getX() >= BPoint.getX())
					if (APoint.getY() - location.getY() <= 2 || BPoint.getY() - location.getY() <= 2)
						if ((APoint.getZ() <= location.getZ() && location.getZ() <= BPoint.getZ())
								|| APoint.getZ() >= location.getZ() && location.getZ() >= BPoint.getZ())
							if (location.getWorld().getName().equals(APoint.getWorld().getName())) {
								i1 = flag ? region.getMonSpeed() : region.getAniSpeed();
								i.add(i1.get(0));
								i.add(i1.get(1));
								i.add(Regions.indexOf(region));
								return i;
							}
			}
		}
		return null;
	}

	private static String getWorld(Location location) {
		String world;
		switch (Objects.requireNonNull(location.getWorld()).getName()) {
		case "world_the_end":
			world = "末影之地";
			break;
		case "world_nether":
			world = "下界地狱";
			break;
		default:
			world = "主世界";
			break;
		}
		return world;
	}

	private static String getMonSpeed(int i) {
		String MonSpeed;
		switch (i) {
		case 0:
			MonSpeed = "暂不限制";
			break;
		case 1:
			MonSpeed = "永不生成";
			break;
		default:
			MonSpeed = "原速度的1/" + i;
			break;
		}
		return MonSpeed;
	}

	private static String getAniSpeed(int i) {
		String AniSpeed;
		switch (i) {
		case 0:
			AniSpeed = "暂不限制";
			break;
		case 1:
			AniSpeed = "永不生成";
			break;
		default:
			AniSpeed = "原速度的1/" + i;
			break;
		}
		return AniSpeed;
	}

	private static String getTime() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(now);
	}
}
