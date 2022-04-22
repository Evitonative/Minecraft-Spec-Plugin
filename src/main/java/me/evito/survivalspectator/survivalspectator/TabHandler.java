package me.evito.survivalspectator.survivalspectator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TabHandler implements TabCompleter {

    final SurvivalSpectator plugin;

    public TabHandler(SurvivalSpectator plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null; //TODO
    }
}
