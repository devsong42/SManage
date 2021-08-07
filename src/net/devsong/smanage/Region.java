package net.devsong.smanage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class Region implements ConfigurationSerializable {
    private String name;
    private final String player;
    private final String time;
    private boolean aniMode = true;
    private boolean monMode = true;
    private SUniversal universal = SUniversal.COMMON;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer() {
        return player;
    }

    public String getTime() {
        return time;
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

    public SUniversal getUniversal() {
        return universal;
    }

    public Region setUniversal(String universal) {
        this.universal = SUniversal.getInstance(universal);
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

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("player", this.player);
        data.put("time", this.time);
        data.put("monMode", this.monMode);
        data.put("aniMode", this.aniMode);
        data.put("universal", this.universal.name());
        data.put("modifier", this.modifier);
        data.put("modifyTime", this.modifyTime);
        data.put("MonSpeed", this.MonSpeed);
        data.put("AniSpeed", this.AniSpeed);
        data.put("ALocation", this.ALocation);
        data.put("BLocation", this.BLocation);
        return data;
    }

    private static Object Get(Map<String, Object> args, String key) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "unknown");
        values.put("player", "unknown");
        values.put("time", "unknown");
        values.put("monMode", RegionDB.defaultMonMode);
        values.put("aniMode", RegionDB.defaultAniMode);
        values.put("universal", RegionDB.defaultUniversal);
        values.put("modifier", "unknown");
        values.put("modifyTime", "unknown");
        values.put("MonSpeed", RegionDB.defaultMonSpeed);
        values.put("AniSpeed", RegionDB.defaultAniSpeed);
        values.put("ALocation", new SLocation(0, 0, 0));
        values.put("BLocation", new SLocation(0, 0, 0));
        return args.get(key) == null ? values.get(key) : args.get(key);
    }

    public static Region deserialize(Map<String, Object> args) {
        return new Region((String) Get(args, "name"), (String) Get(args, "player"), (String) Get(args, "time")).setMonMode((Boolean) Get(args, "monMode")).setAniMode((Boolean) Get(args, "aniMode"))
                .setUniversal((String) Get(args, "universal")).setMonSpeed((List<Integer>) Get(args, "MonSpeed")).setAniSpeed((List<Integer>) Get(args, "AniSpeed"))
                .setALocation((SLocation) Get(args, "ALocation")).setBLocation((SLocation) Get(args, "BLocation"))
                .setModifier((String) Get(args, "modifier")).setModifyTime((String) Get(args, "modifyTime"));
    }
}
