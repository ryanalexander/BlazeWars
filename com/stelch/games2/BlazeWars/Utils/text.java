package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.varables.lang;

public class text {
    public static String f(String text) {return text.replaceAll("&","§");}
    public static String f(lang lang) {return lang.get().replaceAll("&","§");}


}
