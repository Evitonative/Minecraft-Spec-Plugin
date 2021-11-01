package me.evito.spec;
import me.evito.spec.Commands.SpecCmd;
import me.evito.spec.Commands.SpecTab;
import me.evito.spec.Files.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class Spec extends JavaPlugin implements Listener {

    public static Map<UUID, Object[]> specs = new HashMap<UUID, Object[]>();
    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("spec").setExecutor(new SpecCmd(this));
        this.getCommand("spec").setTabCompleter(new SpecTab());

        this.data = new DataManager(this);
        if(data.getConfig().contains("specs"))
            this.restoreSpecs();

        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(!specs.isEmpty()){
            this.saveSpecs();
        }
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event){
        if(event.getWorld() == this.getServer().getWorlds().get(0))
        saveSpecs();
    }

    public void saveSpecs(){
        this.getLogger().log(Level.INFO, getConfig().getString("saving-message"));
        for (Map.Entry<UUID, Object[]> entry : specs.entrySet()) {
            data.getConfig().set("specs." + entry.getKey(), entry.getValue());
        }
        data.saveConfig();
    }

    public void restoreSpecs(){
        data.getConfig().getConfigurationSection("specs").getKeys(false).forEach(key -> {
            Object[] content = ((List<Object>)  data.getConfig().get("specs." + key)).toArray(new Object[0]);
            specs.put(UUID.fromString(key), content);
        });


    }
}
