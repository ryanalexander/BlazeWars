/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 14/7/2019
 *
 */

package com.stelch.games2.BlazeWars.varables;

import org.bukkit.Color;

public enum translations {
    BLAZE_ROD("Rod Spawners");
    private String translation;
    public String getTranslation() {return this.translation;}
    private translations(String translation){this.translation = translation;}
}
