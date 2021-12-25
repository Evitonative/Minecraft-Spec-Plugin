package me.evito.survivalspectator.survivalspectator.commands;

import me.evito.survivalspectator.survivalspectator.SurvivalSpectator;
import me.evito.survivalspectator.survivalspectator.utils.ChatMessage;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        ArrayList<String> msg = new ArrayList<>(Arrays.asList(message.split("\n")));

        //Regex pattern to find permissions in string
        final Pattern permPatter = Pattern.compile("%perm(\\..+?)*%");

        for (int i = msg.size() - 1; i >= 0; i--) {
            String line = msg.get(i);
            System.out.println(line);

            Matcher matcher = permPatter.matcher(line);

            int start;
            int end;
            String subString = "";

            while (matcher.find()){
                start = matcher.start();
                end = matcher.end();
                subString = line.substring(matcher.start(), matcher.end());
            }

            if(subString.equals(""))
                continue;

            subString = subString.replace("%perm.", "").replace("%", "");

            if(!player.hasPermission(subString)){
                msg.remove(i);
                continue;
            }

            //Remove the permission form the message send to the player
            line = line.replace("%perm." + subString + "%", "");
            msg.set(i, line);
        }

        //If player has no permisions for the plugin
        if(msg.toArray().length == 2){
            msg.add(1, messages.fromConfigParseAmpersand("messages.no-permissions"));
        }

        //Add all lines to a single string to send to the player
        message = String.join("\n", msg);

        //Send message
        player.sendMessage(message);
        return true;
    }

    public boolean stay(Player player, SurvivalSpectator plugin, String[] args) {
        return true; //TODO
    }

    public boolean reload(Player player, SurvivalSpectator plugin, String[] args) {
        if(!player.hasPermission("spec.reload")){
            player.sendMessage(messages.fromConfigParseAmpersand("messages.no-permission"));
            return true;
        }

        plugin.reloadConfig(); //TODO Change so it actually works and add message

        return true;
    }
}
