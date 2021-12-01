package me.evito.survivalspectator.survivalspectator;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SurvivalSpectator extends JavaPlugin{

    public Saving saving;
    public Map<UUID, Object[]> spectators;

    @Override
    @SuppressWarnings("ConstantConditions") //This yellow line was annoying me
    public void onEnable() {
        this.saveDefaultConfig();
        spectators = new HashMap<UUID, Object[]>();

        //Saving Event to save players currently in spectator mode
        //TODO: Config for times saves instead of saving with the world
        //TODO: Maybe even when a player enters spectator mode
        //implementing this might have the disadvantage that if the server crashes and a spec player is saved
        saving = new Saving(this);
        this.getServer().getPluginManager().registerEvents(saving, this);

        spectators = saving.loadSpectators();

        //TODO: Rely implement this; this is just a test
        ExtraConfiguration demo = new ExtraConfiguration(this, "demo.yml");
        demo.saveDefaultConfig();

        //Initialise the command
        this.getCommand("spec").setExecutor((new CommandHandler(this)));
        this.getCommand("spec").setTabCompleter(new TabHandler(this));
    }

    @Override
    public void onDisable() {
        //TODO
    }
}
