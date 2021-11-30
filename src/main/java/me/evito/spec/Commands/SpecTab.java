package me.evito.spec.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpecTab implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arguments = new ArrayList<String>();
        if(sender.hasPermission("spec.stay")){
            arguments.add("stay");
        }
        if(sender.hasPermission("spec.reload")){
            arguments.add("reload");
        }
        if(sender.hasPermission("spec.others")){
            arguments.add("player");
        }

        List<String> result = new ArrayList<String>();
        if (args.length == 1) {
            for (String a : arguments){
                if(a.toLowerCase().startsWith(args[0].toLowerCase(Locale.ROOT)))
                    result.add(a);
            }
            return result;
        }

        return null;
    }
}
