package net.devsong.smanage;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class SLocation implements ConfigurationSerializable {
    public double x, y, z;

    public double getX() {
        return x;
    }

    public SLocation setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public SLocation setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public SLocation setZ(double z) {
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

    public String world = "world";

    public SLocation() {
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
        return new SLocation().setX((Double) args.get("x")).setY((Double) args.get("y")).setZ((Double) args.get("z")).setWorld((String) args.get("world"));
    }
}