package me.evito.survivalspectator.survivalspectator.utils;

import me.evito.survivalspectator.survivalspectator.SurvivalSpectator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ExtraConfiguration {
    private final SurvivalSpectator plugin;
    private final String fileName;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public ExtraConfiguration(SurvivalSpectator plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName;
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), fileName);

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(fileName);
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){
        if(this.dataConfig == null)
            reloadConfig();

        return this.dataConfig;
    }

    public void saveConfig() {
        if(this.dataConfig == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save speced players to " + this.configFile, e); //TODO: Change message
        }
    }

    public void saveDefaultConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), fileName);

        if(!this.configFile.exists()){
            this.plugin.saveResource(fileName, false);
        }
    }
}

/*TODO
 * Allow saving comments
 * Or maybe not...
 */