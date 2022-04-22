package me.evito.survivalspectator.survivalspectator;

import me.evito.survivalspectator.survivalspectator.utils.SpecPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;

public class PlayerMapManager implements Listener {

    private final SurvivalSpectator plugin;

    /**
     * Create new Instance of PlayerMapManager for saving and loading
     * @param plugin The plugin this Class is used by
     */
    public PlayerMapManager(SurvivalSpectator plugin) { this.plugin = plugin; }

    /**
     * Saves all Spectators and if configured removes players which are no longer on the server.
     * @throws SQLException When SQL fails
     */
    public void saveSpectators() throws SQLException {
        //Don't save when there is no one saved
        if(plugin.getSpectators().isEmpty())
            return;

        Statement statement = plugin.getConnection().createStatement();

        List<Object[]> players = new ArrayList<>();
        plugin.getSpectators().forEach((k, v) -> {
            players.add(new Object[]{
                    "'" + k + "'",
                    v.getPosition().getX(),
                    v.getPosition().getY(),
                    v.getPosition().getZ(),
                    v.getPosition().getWorld().getEnvironment().getId(),
                    v.getPosition().getYaw(),
                    v.getPosition().getPitch(),
                    v.isFlying(),
                    v.isSneaking(),
                    v.isSprinting(),
                    v.getExp(),
                    v.getLevel(),
                    v.getAbsorptionAmount(),
                    v.getHealth(),
                    v.getFallDistance(),
                    v.getFireTicks(),
                    v.getFreezeTicks(),
                    v.getPortalCooldown(),
                    v.getFoodLevel(),
                    v.getGameMode(),
                    v.getNoDamageTicks(),
                    v.getRemainingAir(),
                    v.isClimbing(),
                    v.isGliding(),
                    v.isRiptiding(),
                    v.isSwimming()
            });
        });

        StringBuilder valueBuilder = new StringBuilder();
        players.forEach(i -> {
            valueBuilder.append(Arrays.toString(i).replace('[', '(').replace(']', ')'));
            valueBuilder.append(",");
        });
        valueBuilder.deleteCharAt(valueBuilder.length() - 1);

        statement.execute("INSERT INTO spec VALUES " + valueBuilder.toString() + "ON DUPLICATE KEY UPDATE x = VALUES(x), y = VALUES(y),z = VALUES(z),world = VALUES(world),yaw = VALUES(yaw),pitch = VALUES(pitch),isFlying = VALUES(isFlying),isSneaking = VALUES(isSneaking),isSprinting = VALUES(isSprinting),exp = VALUES(exp),level = VALUES(level),absorptionAmount = VALUES(absorptionAmount),health = VALUES(health),fallDistance = VALUES(fallDistance),fireTicks = VALUES(fireTicks),freezeTicks = VALUES(freezeTicks),portalCooldown = VALUES(portalCooldown),foodLevel = VALUES(foodLevel),gameMode = VALUES(gameMode),noDamageTicks = VALUES(noDamageTicks),remainingAir = VALUES(remainingAir),isClimbing = VALUES(isClimbing),isGliding = VALUES(isGliding),isRiptiding = VALUES(isRiptiding),isSwimming = VALUES(isSwimming);");

        //Clean up disconnected players
        if(!plugin.getConfig().getBoolean("remove-disconnected-players"))
            return;
        Map<UUID, SpecPlayer> spectators = plugin.getSpectators();
        List<UUID> remove = new ArrayList<>();
        spectators.forEach((k, v) -> {
           if(plugin.getServer().getPlayer(k) == null){
               remove.add(k);
           }
        });

        remove.forEach(spectators::remove);
        plugin.setSpectators(spectators);
    }

    public Map<UUID, SpecPlayer> loadSpectators() throws SQLException {
        if(!plugin.getConfig().getBoolean("remove-disconnected-players"))
            return loadAllPlayers();

        plugin.getServer().getOnlinePlayers().forEach(p -> {
            try {
                loadPlayer(p.getUniqueId(), plugin.getSpectators());
            }
            catch (SQLException e){
                plugin.getServer().getPlayer(p.getUniqueId()).sendMessage(plugin.getConfig().getString("messages.error-could-not-load-player"));
                plugin.getLogger().log(Level.SEVERE, "Failed to load player " + plugin.getServer().getPlayer(p.getUniqueId()).getName() + "UUID: " + plugin.getServer().getPlayer(p.getUniqueId()).getUniqueId());
            }
        });

        return new HashMap<UUID, SpecPlayer>();
    }

    /**
     * Load a new Player to player map.
     *
     * @param playerUUID UUID of the player to add
     * @param spectators Existing Map to which the player should be added.
     * @throws SQLException When SQL fails
     * @return Map with new player inside
     */
    public Map<UUID, SpecPlayer> loadPlayer(UUID playerUUID, Map<UUID, SpecPlayer> spectators) throws SQLException{
        if(spectators.containsKey(playerUUID))
            return spectators;

        Statement statement = plugin.getConnection().createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM spec WHERE uuid = '" + playerUUID + "''");

        if (rs.next()){
            spectators.put(UUID.fromString(rs.getString("UUID")), new SpecPlayer(rs, plugin.getServer()));
        }

        statement.close();
        rs.close();
        return spectators;
    }

    /**
     * Loads all players
     * @return New Map of all players in DB
     * @throws SQLException When SQL fails
     */
    private HashMap<UUID, SpecPlayer> loadAllPlayers() throws SQLException {
        HashMap<UUID, SpecPlayer> result = new HashMap<UUID, SpecPlayer>();

        Statement statement = plugin.getConnection().createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM spec");

        while (rs.next()){
            result.put(UUID.fromString(rs.getString("UUID")), new SpecPlayer(rs, plugin.getServer()));
        }

        if(rs.getRow() > 50 && !plugin.getConfig().getBoolean("disable-many-users"))
            plugin.getLogger().log(Level.WARNING, "You have more than 50 (" + rs.getRow() + ") players in your database. You might want to consider settings the remove-disconnected-players config to true.\nCreate the config-key \"disable-many-users: true\" if you wish to disable this message.");

        statement.close();
        rs.close();
        return result;
    }

    //Events:
    /**
     * Saving on world save.
     * @param event
     */
    @EventHandler
    public void onWorldSave(WorldSaveEvent event){
        if(event.getWorld() != plugin.getServer().getWorlds().get(0))
            return;

        try {
            saveSpectators();
        } catch (SQLException e) {
            e.printStackTrace();
        }//TODO - Other saving options
    }

    /**
     * When a player joins the game
     * @param event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(plugin.getConfig().getBoolean("remove-disconnected-players")){
            try {
                plugin.setSpectators(loadPlayer(event.getPlayer().getUniqueId(), plugin.getSpectators()));
            }
            catch (SQLException e){
                //noinspection ConstantConditions
                event.getPlayer().sendMessage(plugin.getConfig().getString("messages.error-could-not-load-player"));
                plugin.getLogger().log(Level.SEVERE, "Failed to load player " + event.getPlayer().getName() + "UUID: " + event.getPlayer().getUniqueId());
            }
        }
    }
}
