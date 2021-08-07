package net.devsong.smanage;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class SLocation implements ConfigurationSerializable {
    public int x, y, z;
    public String world = "world";

    public SLocation() {
    }

    public SLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public SLocation setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public SLocation setY(int y) {
        this.y = y;
        return this;
    }

    public int getZ() {
        return z;
    }

    public SLocation setZ(int z) {
        this.z = z;
        return this;
    }

    public String getWorld() {
        return world;
    }

    public SLocation setWorld(String world) {
        this.world = world;
        return this;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("x", x);
        data.put("y", y);
        data.put("z", z);
        data.put("world", world);
        return data;
    }

    public static SLocation deserialize(Map<String, Object> args) {
        return new SLocation().setX((int) args.get("x")).setY((int) args.get("y")).setZ((int) args.get("z")).setWorld((String) args.get("world"));
    }
}