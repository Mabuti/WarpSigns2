package com.pvminecraft.warpsigns2.log;

public enum Level {
    ERROR("[warpsigns2] ERROR:"),
    DEBUG("[warpsigns2] DEBUG:"),
    MESSAGE("[warpsigns2]"),
    NONE("");
    
    private String prefix;
    
    private Level(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
}
