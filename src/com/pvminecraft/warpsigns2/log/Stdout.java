package com.pvminecraft.warpsigns2.log;

import com.pvminecraft.warpsigns2.Config;

public class Stdout {
    public static void println(String message, Level lvl) {
        if(lvl == Level.DEBUG && !Config.debugging.getBoolean())
            return;
        if(lvl == Level.ERROR)
            System.err.println(lvl.getPrefix() + " " + message);
        else
            System.out.println(lvl.getPrefix() + " " + message);
    }
}
