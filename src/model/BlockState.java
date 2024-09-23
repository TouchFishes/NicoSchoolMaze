package model;

import java.awt.*;

/**
 * Block State
 */
public enum BlockState {
    ACCESS("Access", new Color(235, 235, 235)),
    WALL("Wall", new Color(20, 20, 20)),
    START("Start", new Color(57, 255, 12)),
    END("End", new Color(255, 148, 9, 255)),
    WIN("WIN", new Color(255, 224, 26, 255));

    private final String name;
    private final Color color;

    BlockState(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
}
