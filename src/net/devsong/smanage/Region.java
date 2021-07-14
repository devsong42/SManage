package net.devsong.smanage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Region implements ConfigurationSerializable {
	private final String name;
	private final String player;
	private final String time;
	private boolean mode = true;
	private String modifier;
	private String modifyTime;
	private SLocation ALocation, BLocation;
	private List<Integer> MonSpeed = Arrays.asList(1, 3);
	private List<Integer> AniSpeed = Arrays.asList(1, 3);

	public Region(String name, String player, String time) {
		this.name = name;
		this.player = player;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public String getPlayer() {
		return player;
	}

	public String getTime() {
		return time;
	}

	public boolean getMode() {
		return mode;
	}

	public Region setMode(boolean mode) {
		this.mode = mode;
		return this;
	}

	public List<Integer> getMonSpeed() {
		return this.MonSpeed;
	}

	public Region setMonSpeed(List<Integer> monSpeed) {
		this.MonSpeed = monSpeed;
		return this;
	}

	public List<Integer> getAniSpeed() {
		return this.AniSpeed;
	}

	public Region setAniSpeed(List<Integer> aniSpeed) {
		this.AniSpeed = aniSpeed;
		return this;
	}

	public Region setALocation(SLocation location) {
		this.ALocation = location;
		return this;
	}

	public Region setBLocation(SLocation location) {
		this.BLocation = location;
		return this;
	}

	public SLocation getALocation() {
		return ALocation;
	}

	public SLocation getBLocation() {
		return BLocation;
	}

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<>();
		data.put("name", this.name);
		data.put("player", this.player);
		data.put("time", this.time);
		data.put("mode", this.mode);
		data.put("modifier", this.modifier);
		data.put("modifyTime", this.modifyTime);
		data.put("MonSpeed", this.MonSpeed);
		data.put("AniSpeed", this.AniSpeed);
		data.put("ALocation", this.ALocation);
		data.put("BLocation", this.BLocation);
		return data;
	}

	public static Region deserialize(Map<String, Object> args) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = sdf.format(now);
		return new Region((String) args.get("name"), (String) args.get("player"), dateStr).setMode((Boolean) args.get("mode"))
				.setMonSpeed((List<Integer>) args.get("MonSpeed")).setAniSpeed((List<Integer>) args.get("AniSpeed"))
				.setALocation((SLocation) args.get("ALocation")).setBLocation((SLocation) args.get("BLocation"))
				.setModifier((String) args.get("modifier")).setModifyTime((String) args.get("modifyTime"));
	}

	public String getModifier() {
		return modifier;
	}

	public Region setModifier(String modifier) {
		this.modifier = modifier;
		return this;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public Region setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
		return this;
	}
}
