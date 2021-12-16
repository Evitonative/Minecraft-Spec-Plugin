package me.evito.survivalspectator.survivalspectator.commands;

import me.evito.survivalspectator.survivalspectator.SurvivalSpectator;
import me.evito.survivalspectator.survivalspectator.utils.ChatMessage;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Commands {

    private final ChatMessage messages;

    public Commands(ChatMessage messages){
        this.messages = messages;
    }

    public boolean use(Player player, SurvivalSpectator plugin, String[] args) {
        return true; //TODO
    }

    public boolean help(Player player, SurvivalSpectator plugin, String[] args, String label) {
        String message = messages.fromConfigParseAmpersand("messages.help");
        message = message.replace("%command", label);

        List<String> msg = Arrays.asList(message.split("\n"));
        List<Integer> remove = new List<Integer>();
        for (int i = 0; i < msg.size(); i++) {
            String line = msg.get(i);
            if(line.contains("%perm.use") && !player.hasPermission("spec.use")) remove.add(i); //TODO FIX, do not remove in loop but outside
            else if(line.contains("%perm.help") && !player.hasPermission("spec.help")) msg.remove(i);
            else if(line.contains("%perm.stay") && !player.hasPermission("spec.stay")) msg.remove(i);
            else if(line.contains("%perm.reload") && !player.hasPermission("spec.reload")) msg.remove(i);
        }

        message = String.join("\n", msg);

        player.sendMessage(message);
        return true;
    }

    public boolean stay(Player player, SurvivalSpectator plugin, String[] args) {
        return true; //TODO
    }

    public boolean reload(Player player, SurvivalSpectator plugin, String[] args) {
        return true; //TODO
    }
}
