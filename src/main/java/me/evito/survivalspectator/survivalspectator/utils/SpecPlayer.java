package me.evito.survivalspectator.survivalspectator.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.potion.PotionEffect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

public class SpecPlayer {
    public Location position;
    public boolean isFlying;
    public boolean isSneaking;
    public boolean isSprinting;
    public float exp;
    public int level;
    public double absorptionAmount;
    public double health;
    public float fallDistance;
    public int fireTicks;
    public int freezeTicks;
    public int portalCooldown;
    /* //Maybe implement them?
    public boolean isInVehicle;
    public Entity vehicle;
     */
    public int foodLevel;
    public int gameMode;
    public Collection<PotionEffect> activePotionEffects; //TODO: Save in sql
    public int noDamageTicks;
    public int remainingAir;
    public boolean isClimbing;
    public boolean isGliding;
    public boolean isRiptiding;
    public boolean isSwimming;

    public SpecPlayer(ResultSet rs, Server server) throws SQLException {
        this.position = new Location(server.getWorlds().get(rs.getInt("world")),
                rs.getDouble("x"),
                rs.getDouble("y"),
                rs.getDouble("z"),
                rs.getFloat("yaw"),
                rs.getFloat("pitch")
        );
        this.isFlying = rs.getBoolean("isFlying");
        this.isSneaking = rs.getBoolean("isSneaking");
        this.isSprinting = rs.getBoolean("isSprinting");
        this.exp = rs.getFloat("exp");
        this.level = rs.getInt("level");
        this.absorptionAmount = rs.getDouble("absorptionAmount");
        this.health = rs.getDouble("health");
        this.fallDistance = rs.getFloat("fallDistance");
        this.fireTicks = rs.getInt("fireTicks");
        this.freezeTicks = rs.getInt("freezeTicks");
        this.portalCooldown = rs.getInt("portalCooldown");
        this.foodLevel = rs.getInt("foodLevel");
        this.gameMode = rs.getInt("gameMode");
        this.activePotionEffects = Collections.emptySet();
        this.noDamageTicks = rs.getInt("noDamageTicks");
        this.remainingAir =  rs.getInt("remainingAir");
        this.isClimbing = rs.getBoolean("isClimbing");
        this.isGliding = rs.getBoolean("isGliding");
        this.isRiptiding = rs.getBoolean("isRiptiding");
        this.isSwimming = rs.getBoolean("isSwimming");
    }

    public SpecPlayer(Location position, boolean isFlying, boolean isSneaking, boolean isSprinting, float exp, int level, double absorptionAmount, double health, float fallDistance, int fireTicks, int freezeTicks, int portalCooldown, int foodLevel, int gamemode, Collection<PotionEffect> activePotionEffects, int noDamageTicks, int remainingAir, boolean isClimbing, boolean isGliding, boolean isRiptiding, boolean isSwimming) {
        this.position = position;
        this.isFlying = isFlying;
        this.isSneaking = isSneaking;
        this.isSprinting = isSprinting;
        this.exp = exp;
        this.level = level;
        this.absorptionAmount = absorptionAmount;
        this.health = health;
        this.fallDistance = fallDistance;
        this.fireTicks = fireTicks;
        this.freezeTicks = freezeTicks;
        this.portalCooldown = portalCooldown;
        this.foodLevel = foodLevel;
        this.gameMode = gamemode;
        this.activePotionEffects = activePotionEffects;
        this.noDamageTicks = noDamageTicks;
        this.remainingAir = remainingAir;
        this.isClimbing = isClimbing;
        this.isGliding = isGliding;
        this.isRiptiding = isRiptiding;
        this.isSwimming = isSwimming;
    }

    public SpecPlayer(Location position, boolean isFlying, boolean isSneaking, boolean isSprinting, float exp, int level, double absorptionAmount, double health, float fallDistance, int fireTicks, int freezeTicks, int portalCooldown, int foodLevel, GameMode gamemode, Collection<PotionEffect> activePotionEffects, int noDamageTicks, int remainingAir, boolean isClimbing, boolean isGliding, boolean isRiptiding, boolean isSwimming) {
        this.position = position;
        this.isFlying = isFlying;
        this.isSneaking = isSneaking;
        this.isSprinting = isSprinting;
        this.exp = exp;
        this.level = level;
        this.absorptionAmount = absorptionAmount;
        this.health = health;
        this.fallDistance = fallDistance;
        this.fireTicks = fireTicks;
        this.freezeTicks = freezeTicks;
        this.portalCooldown = portalCooldown;
        this.foodLevel = foodLevel;

        switch (gamemode){
            case SURVIVAL:
                this.gameMode = 0;
                break;
            case CREATIVE:
                this.gameMode = 1;
                break;
            case ADVENTURE:
                this.gameMode = 2;
                break;
            case SPECTATOR:
                this.gameMode = 3;
                break;
            default:
                throw new IllegalArgumentException();
        }

        this.activePotionEffects = activePotionEffects;
        this.noDamageTicks = noDamageTicks;
        this.remainingAir = remainingAir;
        this.isClimbing = isClimbing;
        this.isGliding = isGliding;
        this.isRiptiding = isRiptiding;
        this.isSwimming = isSwimming;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public void setSneaking(boolean sneaking) {
        isSneaking = sneaking;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public void setSprinting(boolean sprinting) {
        isSprinting = sprinting;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getAbsorptionAmount() {
        return absorptionAmount;
    }

    public void setAbsorptionAmount(double absorptionAmount) {
        this.absorptionAmount = absorptionAmount;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public void setFallDistance(float fallDistance) {
        this.fallDistance = fallDistance;
    }

    public int getFireTicks() {
        return fireTicks;
    }

    public void setFireTicks(int fireTicks) {
        this.fireTicks = fireTicks;
    }

    public int getFreezeTicks() {
        return freezeTicks;
    }

    public void setFreezeTicks(int freezeTicks) {
        this.freezeTicks = freezeTicks;
    }

    public int getPortalCooldown() {
        return portalCooldown;
    }

    public void setPortalCooldown(int portalCooldown) {
        this.portalCooldown = portalCooldown;
    }

    public void setInVehicle(boolean inVehicle) {
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        switch (gameMode){
            case SURVIVAL:
                this.gameMode = 0;
                return;
            case CREATIVE:
                this.gameMode = 1;
                return;
            case ADVENTURE:
                this.gameMode = 2;
                return;
            case SPECTATOR:
                this.gameMode = 3;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return activePotionEffects;
    }

    public void setActivePotionEffects(Collection<PotionEffect> activePotionEffects) {
        this.activePotionEffects = activePotionEffects;
    }

    public int getNoDamageTicks() {
        return noDamageTicks;
    }

    public void setNoDamageTicks(int noDamageTicks) {
        this.noDamageTicks = noDamageTicks;
    }

    public int getRemainingAir() {
        return remainingAir;
    }

    public void setRemainingAir(int remainingAir) {
        this.remainingAir = remainingAir;
    }

    public boolean isClimbing() {
        return isClimbing;
    }

    public void setClimbing(boolean climbing) {
        isClimbing = climbing;
    }

    public boolean isGliding() {
        return isGliding;
    }

    public void setGliding(boolean gliding) {
        isGliding = gliding;
    }

    public boolean isRiptiding() {
        return isRiptiding;
    }

    public void setRiptiding(boolean riptiding) {
        isRiptiding = riptiding;
    }

    public boolean isSwimming() {
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        isSwimming = swimming;
    }
}
