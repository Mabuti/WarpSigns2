package com.pvminecraft.warpsigns2;

import org.bukkit.configuration.file.YamlConfiguration;

public enum Config {
    debugging("debugging", false),
    autoUpdate("auto-update", true);
    
    private String path;
    private Object value;
    
    private Config(String path, Object value) {
        this.path = path;
        this.value = value;
    }
    
    public String getPath() {
        return path;
    }
    
    public Boolean getBoolean() {
        return (Boolean) value;
    }
    
    public String getString() {
        return (String) value;
    }
    
    public Integer getInteger() {
        return (Integer) value;
    }
    
    public void setValue(Object obj) {
        this.value = obj;
    } 
    
    public static void load(YamlConfiguration yaml) {
        for(Config c : values())
            if(!c.getPath().isEmpty())
                if(yaml.get(c.getPath()) != null)
                    c.setValue(yaml.get(c.getPath()));
    }
}
