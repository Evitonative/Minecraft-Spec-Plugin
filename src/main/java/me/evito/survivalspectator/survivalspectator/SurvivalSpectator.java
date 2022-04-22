package me.evito.survivalspectator.survivalspectator;

import me.evito.survivalspectator.survivalspectator.commands.CommandHandler;
import me.evito.survivalspectator.survivalspectator.utils.SpecPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class SurvivalSpectator extends JavaPlugin{

    private PlayerMapManager playerMapManager;
    private Map<UUID, SpecPlayer> spectators;
    private Connection connection;

    @Override
    @SuppressWarnings("ConstantConditions") //This yellow line was annoying me
    public void onEnable() {
        this.saveDefaultConfig();

        //Saving Event to save players currently in spectator mode
        //TODO: Config for times saves instead of saving with the world
        //TODO: Maybe even when a player enters spectator mode
        //implementing this might have the disadvantage that if the server crashes and a spec player is saved
        playerMapManager = new PlayerMapManager(this);
        this.getServer().getPluginManager().registerEvents(playerMapManager, this);

        try {
            initSql();
            spectators = playerMapManager.loadSpectators();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Failed to connect to or load data from database.", e);
            getServer().getPluginManager().disablePlugin(this);
        }

        //Initialise the command
        this.getCommand("spec").setExecutor((new CommandHandler(this)));
        this.getCommand("spec").setTabCompleter(new TabHandler(this));
    }

    private void initSql() throws SQLException {
        String url;
        if(getConfig().getBoolean("MySQL.enabled"))
            connection = DriverManager.getConnection("jdbc:mysql://" + getConfig().getString("MySQL.uri") + ":" + getConfig().getInt("MySQL.port") + "/" + getConfig().getString("MySQL.database"), getConfig().getString("MySQL.user"), getConfig().getString("MySQL.password"));
        else
            connection = DriverManager.getConnection("jdbc:sqlite:" + this.getDataFolder() + File.separator + "database.db");

        Statement statement = connection.createStatement();
        statement.execute("""
            CREATE TABLE IF NOT EXISTS spec (
                uuid varchar(36) primary key not null,
                x double not null,
                y double not null,
                z double not null,
                world int not null,
                yaw float not null,
                pitch float not null,
                isFlying bool,
                isSneaking bool,
                isSprinting bool,
                exp float,
                level integer,
                absorptionAmount double,
                health double,
                fallDistance float,
                fireTicks integer,
                freezeTicks integer,
                portalCooldown integer,
                foodLevel int,
                gameMode int,
                noDamageTicks int,
                remainingAir int,
                isClimbing bool,
                isGliding bool,
                isRiptiding bool,
                isSwimming bool
            );
            """);
        statement.close();
    }

    @Override
    public void onDisable() {
        //TODO
    }

    public PlayerMapManager getSaving() {
        return playerMapManager;
    }

    public void setSaving(PlayerMapManager playerMapManager) {
        this.playerMapManager = playerMapManager;
    }

    public Map<UUID, SpecPlayer> getSpectators() {
        return spectators;
    }

    public void setSpectators(Map<UUID, SpecPlayer> spectators) {
        this.spectators = spectators;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
