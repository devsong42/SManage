package net.devsong.smanage;

public enum SUniversal {
    COMMON,
    WORLD,
    SERVER;

    public static SUniversal getInstance(String universal) {
        return switch (universal) {
            case "WORLD" -> WORLD;
            case "SERVER" -> SERVER;
            default -> COMMON;
        };
    }
}
