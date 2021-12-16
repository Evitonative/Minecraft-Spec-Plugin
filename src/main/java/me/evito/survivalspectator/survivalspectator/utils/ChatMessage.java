package me.evito.survivalspectator.survivalspectator.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

public class ChatMessage {
    private final Configuration config;

    public ChatMessage(Configuration config){
        this.config = config;
    }

    public String negative(String message){
        return ChatColor.RED + " " + message;
    }
    public String positive(String message){
        return ChatColor.GREEN + " " + message;
    }
    public String parseAmpersand(String message){
        return message
                .replace("&0", ChatColor.BLACK.toString())
                .replace("&1", ChatColor.DARK_BLUE.toString())
                .replace("&2", ChatColor.DARK_GREEN.toString())
                .replace("&3", ChatColor.DARK_AQUA.toString())
                .replace("&4", ChatColor.DARK_RED.toString())
                .replace("&5", ChatColor.DARK_PURPLE.toString())
                .replace("&6", ChatColor.GOLD.toString())
                .replace("&7", ChatColor.GRAY.toString())
                .replace("&8", ChatColor.DARK_GRAY.toString())
                .replace("&9", ChatColor.BLUE.toString())
                .replace("&a", ChatColor.GREEN.toString())
                .replace("&b", ChatColor.AQUA.toString())
                .replace("&c", ChatColor.RED.toString())
                .replace("&d", ChatColor.LIGHT_PURPLE.toString())
                .replace("&e", ChatColor.YELLOW.toString())
                .replace("&f", ChatColor.WHITE.toString())
                .replace("&k", ChatColor.MAGIC.toString())
                .replace("&l", ChatColor.BOLD.toString())
                .replace("&m", ChatColor.STRIKETHROUGH.toString())
                .replace("&n", ChatColor.UNDERLINE.toString())
                .replace("&o", ChatColor.ITALIC.toString())
                .replace("&r", ChatColor.RESET.toString());
    }
    public String fromConfig(String configPath){
        return config.getString(configPath);
    }
    public String fromConfigParseAmpersand(String configPath){
        return parseAmpersand(fromConfig(configPath));
    }
}
