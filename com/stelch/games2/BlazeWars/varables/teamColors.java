package com.stelch.games2.BlazeWars.varables;

import org.bukkit.Color;

public enum teamColors {
    WHITE(Color.WHITE), PURPLE(Color.PURPLE), RED(Color.RED), BLUE(Color.BLUE), GREEN(Color.GREEN), YELLOW(Color.YELLOW), ORANGE(Color.ORANGE), GREY(Color.GRAY);
    private Color color;
    public Color getColor() {return this.color;}
    private teamColors(Color Color){this.color = Color;}
}
