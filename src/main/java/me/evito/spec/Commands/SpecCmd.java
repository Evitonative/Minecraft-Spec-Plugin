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
        if(!lable.equalsIgnoreCase("spec") &&
                !lable.equalsIgnoreCase("c") &&
                !lable.equalsIgnoreCase("spectator") &&
                !lable.equalsIgnoreCase("spec:spec") &&
                !lable.equalsIgnoreCase("spec:c") &&
                !lable.equalsIgnoreCase("spec:spectator")
        )return false;

        if(!(sender instanceof Player)){
            sender.sendMessage(plugin.getConfig().getString("messages.not-in-console"));
            return true;
        }

        Player player = (Player) sender;
        if(!player.hasPermission("spec.use")){
            sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no-permissions"));
            return true;
        }

        //Commands
        if(args.length > 0)
            switch (args[0]){
                case "reload":
                    if(player.hasPermission("spec.reload")){
                        try {
                            plugin.reloadConfig();
                            player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messages.done"));
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.reload-error"));
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no-permissions"));
                    }
                    return true;
                case "player":
                    if(!plugin.getConfig().getBoolean("other-players.enabled")) return true;
                    if(!player.hasPermission("spec.others")){
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no-permissions"));
                        return false;
                    }
                    if(args.length != 2){
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.wrong-args"));
                        return false;
                    }

                    final Player playerForSpec;
                    try{
                        playerForSpec = plugin.getServer().getPlayer(args[1]);
                    }catch (Exception e){
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("other-players.not-found"));
                        return true;
                    }
                    if(!playerForSpec.hasPermission("spec.use")){
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("other-players.target-no-perms"));
                        return true;
                    }
                    if(!playerForSpec.isOnGround()){//TODO FIX: Player enters flymode but not spectator mode?
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("other-players.target-not-on-ground"));
                        return true;
                    }

                    //TODO: Check all messages
                    //TODO: Add more commands
                    //TODO: Add more requirements like water, lava
                    playerForSpec.performCommand("spec:spec");
                default:
                    if(player.hasPermission("spec.help")){
                        player.sendMessage();
                    }
            }


        // If they are not in Spectator already
        if(player.getGameMode() != GameMode.SPECTATOR){
            if (!player.isOnGround() && !player.hasPermission("spec.bypass")){
                sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("in-air.message"));
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

        //stay command
        if(args.length > 0 && args[0].equals("stay")){
            if (!plugin.getConfig().getBoolean("stay.enabled")) return true;
            if (player.hasPermission("spec.stay") || !plugin.getConfig().getBoolean("enable.require-permission")) {
                gamemodes(player);
            } else {
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no-permissions"));
            }
            return true;
        }

        //If the player does not exist
        if(!Spec.specs.containsKey(player.getUniqueId())){
            try{
                player.teleport(player.getBedSpawnLocation());
            } catch (Exception e) {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            }

            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no-location"));
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