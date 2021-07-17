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
	private boolean aniMode = true;
	private boolean monMode = true;
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

	public boolean getAniMode() {
		return aniMode;
	}

	public Region setAniMode(boolean mode) {
		this.aniMode = mode;
		return this;
	}

	public boolean getMonMode() {
		return monMode;
	}

	public Region setMonMode(boolean mode) {
		this.monMode = mode;
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
		data.put("monMode", this.monMode);
		data.put("aniMode", this.aniMode);
		data.put("modifier", this.modifier);
		data.put("modifyTime", this.modifyTime);
		data.put("MonSpeed", this.MonSpeed);
		data.put("AniSpeed", this.AniSpeed);
		data.put("ALocation", this.ALocation);
		data.put("BLocation", this.BLocation);
		return data;
	}

	public static Region deserialize(Map<String, Object> args) {
		return new Region((String) args.get("name"), (String) args.get("player"), (String) args.get("time")).setMonMode((Boolean) args.get("monMode")).setAniMode((Boolean) args.get("aniMode"))
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
