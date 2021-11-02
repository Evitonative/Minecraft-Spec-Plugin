package me.evito.spec.Commands;

import me.evito.spec.Spec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpecCmd implements CommandExecutor{
    List<float[]> data;

    static Spec plugin;
    public SpecCmd(Spec spec) {
        plugin = spec;
        data = new ArrayList<float[]>();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args){
        if(lable.equalsIgnoreCase("spec") ||
                lable.equalsIgnoreCase("c") ||
                lable.equalsIgnoreCase("spectator") ||
                lable.equalsIgnoreCase("spec:spec") ||
                lable.equalsIgnoreCase("spec:c") ||
                lable.equalsIgnoreCase("spec:spectator")) {

            if(!(sender instanceof Player)){
                sender.sendMessage(plugin.getConfig().getString("not-in-console"));
                return true;
            }

            Player player = (Player) sender;
            if(!player.hasPermission("spec.use")){
                sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("no-permissions"));
                return true;
            }

            // If they are not in Spectator already
            if(player.getGameMode() != GameMode.SPECTATOR){
                if (!player.isOnGround() && !player.hasPermission("spec.bypass")){
                    sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("in-air"));
                    return true;
                }

                if(player.getGameMode() == GameMode.SURVIVAL){
                    Spec.specs.put(player.getUniqueId(), new Object[]{
                            player.getLocation(),
                            0,
                            player.getRemainingAir(),
                            player.getActivePotionEffects(),
                            player.getFireTicks()
                    });
                }
                else if(player.getGameMode() == GameMode.CREATIVE){
                    Spec.specs.put(player.getUniqueId(), new Object[]{
                            player.getLocation(),
                            1,
                            player.getRemainingAir(),
                            player.getActivePotionEffects(),
                            player.getFireTicks()
                    });
                }
                else if(player.getGameMode() == GameMode.ADVENTURE){
                    Spec.specs.put(player.getUniqueId(), new Object[]{
                            player.getLocation(),
                            2,
                            player.getRemainingAir(),
                            player.getActivePotionEffects(),
                            player.getFireTicks()
                    });
                }

                player.setGameMode(GameMode.SPECTATOR);
                return true;
            }

            //but first more command stuff
            if(args.length > 0)
                switch (args[0]){
                    case "stay":
                        if(!plugin.getConfig().getBoolean("allow-stay")) break;
                        if(player.hasPermission("spec.stay")){
                            gamemodes(player);
                        }
                        else {
                            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("no-permissions"));
                        }
                        return true;
                    default:
                        if(player.hasPermission("spec.help")){
                            player.sendMessage();
                        }
                }

            //If the player does not exist
            if(!Spec.specs.containsKey(player.getUniqueId())){
                try{
                    player.teleport(player.getBedSpawnLocation());
                } catch (Exception e) {
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }

                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("no-location"));
                player.setGameMode(GameMode.SURVIVAL);
                return true;
            }

            //Do the teleporty backy stuffy
            player.teleport((Location) Spec.specs.get(player.getUniqueId())[0]);

            gamemodes(player);

            player.setRemainingAir((int) Spec.specs.get(player.getUniqueId())[2]);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.addPotionEffects((Collection<PotionEffect>) Spec.specs.get(player.getUniqueId())[3]);

            player.setFireTicks((int) Spec.specs.get(player.getUniqueId())[4]);
            return true;
        }
        return false;
    }

    private void gamemodes(Player player) {
        switch ((int) Spec.specs.get(player.getUniqueId())[1]){
            /*case 0:
            player.setGameMode(GameMode.SURVIVAL);
            break;*/
            case 1:
                player.setGameMode(GameMode.CREATIVE);
                break;
            case 2:
                player.setGameMode(GameMode.ADVENTURE);
                break;
            default:
                player.setGameMode(GameMode.SURVIVAL);
                break;
        }
    }
}