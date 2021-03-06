package com.pvminecraft.warpsigns2;

import com.pvminecraft.points.PointsService;
import com.pvminecraft.warpsigns2.log.Stdout;
import com.pvminecraft.warpsigns2.log.Level;
import com.pvminecraft.warpsigns2.listeners.SignListener;

import java.io.*;
import java.nio.channels.FileChannel;

import com.pvminecraft.warpsigns2.utils.Updater;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private PointsService points;
    private SignListener listener;
    private SignManager manager;
    private YamlConfiguration config;
    private File confFile;

    @Override
    public void onEnable() {

        Stdout.println("Start loading WarpSigns2");

        setupConfig();

        if(!getPoints()) {
            Stdout.println("Couldn't find Points! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(Config.autoUpdate.getBoolean()) {
            Updater updater = new Updater(this, "warpsigns2", this.getFile(), Updater.UpdateType.DEFAULT, false);
        }

        importOld();

        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        Stdout.println("Finished loading Points");
        manager = new SignManager(points, getDataFolder(), getServer());
        listener = new SignListener(points.getPlayerManager(), manager);
        manager.load();
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {

    }
    
    public boolean getPoints() {
        Stdout.println("Loading Points...");
        ServicesManager sm = getServer().getServicesManager();
        points = sm.load(PointsService.class);
        return points != null;
    }
    
    public void importOld() {
        File pointsData = getServer().getPluginManager().getPlugin("Points").getDataFolder();
        if(pointsData.exists()) {
            File signsDB = new File(pointsData, "signs.db");
            if(signsDB.exists()) {
                if(!getDataFolder().exists())
                    getDataFolder().mkdirs();
                try {
                    Stdout.println("Importing old Points signs");
                    copyFile(signsDB, new File(getDataFolder(), "signs.db"));
                    if(!signsDB.delete()) {
                        Stdout.println("Could not delete Points/signs.db", Level.ERROR);
                        Stdout.println("Delete this yourself or risk data loss!", Level.ERROR);
                    }
                    Stdout.println("Finished importing old signs");
                } catch(IOException e) {
                    Stdout.println("Could not import old signs", Level.ERROR);
                }
            }
        }
    }

    private void setupConfig() {
        Stdout.println("Loading configuration");

        try {
            confFile = new File(getDataFolder().getPath(), "config.yml");
            config = new YamlConfiguration();
            config.load(confFile.getPath());
            Config.load(config);
            Stdout.println("Loaded configuration");
        } catch(FileNotFoundException e) {
            Stdout.println("Couldn't find config.yml... Generating...");
            if(copyFile(Plugin.class.getResourceAsStream("resources/config.yml"), confFile.getPath())) {
                Stdout.println("config.yml has been generated!");
                setupConfig();
            } else {
                Stdout.println("Couldn't generate config.yml! Anything goes...", Level.ERROR);
            }
        } catch(IOException e) {
            Stdout.println("Couldn't read config.yml! Anything goes...", Level.ERROR);
        } catch(InvalidConfigurationException e) {
            Stdout.println("Malformed configuration! Anything goes...", Level.ERROR);
        }
    }
    
    private void copyFile(File old, File dest) throws IOException {
        if(!dest.exists())
            dest.createNewFile();
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(old).getChannel();
            destination  = new FileOutputStream(dest).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if(source != null)
                source.close();
            if(destination != null)
                destination.close();
        }
    }

    private static boolean copyFile(InputStream resource, String path) {
        try {
            File target = new File(path);
            if(!target.exists())
                target.createNewFile();
            OutputStream out = new FileOutputStream(target);
            int next = resource.read();
            while(next != -1) {
                out.write(next);
                next = resource.read();
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
}
