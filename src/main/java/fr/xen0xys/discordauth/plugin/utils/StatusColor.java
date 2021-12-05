package fr.xen0xys.discordauth.plugin.utils;

import java.awt.*;

public enum StatusColor {

    OK(Color.GREEN),
    Warning(Color.ORANGE),
    Error(Color.RED);

    private Color color;

    StatusColor(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
