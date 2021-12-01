package me.evito.survivalspectator.survivalspectator;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.util.Map;
import java.util.UUID;

public class Saving implements Listener {

    private final SurvivalSpectator plugin;

    public Saving(SurvivalSpectator plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event){
        //TODO
    }

    public void saveSpectators(){
        //TODO
    }

    public Map<UUID, Object[]> loadSpectators(){
        //TODO
        return null;
    }
}
