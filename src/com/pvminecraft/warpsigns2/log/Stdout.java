package com.pvminecraft.warpsigns2.log;

import com.pvminecraft.warpsigns2.Config;

public class Stdout {
    public static void println(String message){
        println(message, Level.NONE);
    }

    public static void println(String message, Level lvl) {
        if(lvl == Level.DEBUG && !Config.debugging.getBoolean())
            return;

        if(lvl == Level.DEBUG)
            System.err.println("[WarpSigns2] [DEBUG] " + message);
        else if(lvl == Level.ERROR)
            System.err.println("[WarpSigns2] " + message);
        else if(lvl == Level.NONE)
            System.out.println("[WarpSigns2] " + message);
        else
            System.out.println("[WarpSigns2] [" + lvl.getPrefix() + "] " + message);
    }
}
