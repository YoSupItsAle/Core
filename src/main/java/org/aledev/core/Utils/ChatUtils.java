package org.aledev.core.Utils;

import org.aledev.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils {

    private static Core plugin = Core.getInstance();

    public static void info(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "[" + ChatColor.GOLD + "INFO" + ChatColor.WHITE + "] " + message);
    }


    public static void debug(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "[" + ChatColor.GREEN + "DEBUG" + ChatColor.WHITE + "] " + message);
    }

    public static void error(String message){
        Bukkit.getConsoleSender().sendMessage("[" + ChatColor.RED + "ERROR" + ChatColor.WHITE + "] " + message);
    }

    public static void broadcast(String message){
        Bukkit.broadcastMessage("[" + ChatColor.RED + "BROADCAST" + ChatColor.WHITE + "] " + message);
    }

    public static void printException(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "EXCEPTION" + ChatColor.WHITE + "] " + message);
    }

    public static void sendUsage(CommandSender sender, String message){
        sender.sendMessage(ChatColor.BOLD + "[" + ChatColor.RED + "USAGE" + ChatColor.WHITE + "] " + message);
    }

    public static void sendMessage(CommandSender sender, String module, String message){
        sender.sendMessage(ChatColor.BOLD + "[" + module + ChatColor.RESET + "] " + message);
    }

}
