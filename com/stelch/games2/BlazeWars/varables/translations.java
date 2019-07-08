package com.stelch.games2.BlazeWars.varables;

import org.bukkit.Color;

public enum translations {
    BLAZE_ROD("Rod Spawners");
    private String translation;
    public String getTranslation() {return this.translation;}
    private translations(String translation){this.translation = translation;}
}
