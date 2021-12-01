package me.evito.survivalspectator.survivalspectator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

    final SurvivalSpectator plugin;

    public CommandHandler(SurvivalSpectator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false; //TODO
    }
}
