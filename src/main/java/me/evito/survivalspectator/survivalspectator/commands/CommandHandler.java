package me.evito.survivalspectator.survivalspectator.commands;

import me.evito.survivalspectator.survivalspectator.SurvivalSpectator;
import me.evito.survivalspectator.survivalspectator.utils.ChatMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

public class CommandHandler implements CommandExecutor {

    final SurvivalSpectator plugin;
    final ChatMessage messages;
    final Commands commands;
    final String[] validCommands;

    public CommandHandler(SurvivalSpectator plugin){
        this.plugin = plugin;
        this.validCommands = new String[]{
                "c",
                "s",
                "spec",
                "spectator",
                "survivalspectator:c" ,
                "survivalspectator:s",
                "survivalspectator:spec",
                "survivalspectator:spectator"
        }; //TODO: Config param? - What?
        this.messages = new ChatMessage(plugin.getConfig());
        this.commands = new Commands(messages);
    }

    public CommandHandler(SurvivalSpectator plugin, String[] validCommands) {
        this.plugin = plugin;
        this.validCommands = validCommands;
        this.messages = new ChatMessage(plugin.getConfig());
        this.commands = new Commands(messages);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!Arrays.asList(validCommands).contains(label)) return false;

        //Send Message to the console if it uses the command, because it can not go into spectator
        if(!(sender instanceof Player)){
            sender.sendMessage(Objects.requireNonNull(messages.fromConfigParseAmpersand("messages.player-only")));
            return true;
        }

        //Get a player to work with
        final Player player = (Player) sender;

        //Commands
        if(args.length <= 0) return commands.use(player, plugin, command, args);
        switch (args[0]){
            case "use": return commands.use(player, plugin, command, args);
            case "help": return commands.help(player, plugin, command, args, label);
            case "stay": return commands.stay(player, plugin, command, args);
            case "reload": return commands.reload(player, plugin, command, args);
            default:
                if(player.hasPermission("spec.help")) return commands.help(player, plugin, command, args, label);
                else if(player.hasPermission("spec.use")) return commands.use(player, plugin, command, args);
                else {
                    player.sendMessage(messages.fromConfigParseAmpersand("messages.error"));
                    return true;
                }
        }
    }
}
