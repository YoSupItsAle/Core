package org.aledev.core.Commands;

import org.aledev.core.Core;
import org.aledev.core.Managers.DatabaseManager;
import org.aledev.core.Managers.ProfileManager;
import org.aledev.core.Models.Component;
import org.aledev.core.Models.Profile;
import org.aledev.core.Utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BalanceCommand extends Component implements CommandExecutor, TabExecutor {

    DatabaseManager databaseManager;

    public BalanceCommand(Core plugin, DatabaseManager databaseManager) {
        super(plugin);
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0){
            if(sender instanceof Player player){
                sendBalance(player);
                return  true;
            }else{
                ChatUtils.sendUsage(sender, ChatColor.RED + "/balance <player>");
            }
        }

        if(args.length == 1){
            String targetUsername = args[0];
            sendBalance(sender, targetUsername);
            return  true;
        }
        return true;
    }

    private void sendBalance(Player player){
        if(databaseManager.existsInDB(player.getUniqueId())){
            ResultSet playerData = databaseManager.getProfileInfo(player.getUniqueId());
            int playerCoins = 0;
            int playerPoints = 0;
            try {
                playerCoins = playerData.getInt("coins");
                playerPoints = playerData.getInt("points");
            } catch (SQLException exception) {
                ChatUtils.printException(exception.getMessage());
                ChatUtils.sendMessage(player, ChatColor.RED + "ERROR", ChatColor.RED + "C'è stato un errore nel trovare il balance del player. Reporta questo messaggio agli staffer.");
            }

            player.sendMessage(ChatColor.GREEN + "Il tuo balance:\n"+ ChatColor.YELLOW +  "Coins: " + ChatColor.WHITE + playerCoins + ChatColor.GOLD + "\nPoints: " + ChatColor.WHITE + playerPoints);

        }else{
            ChatUtils.sendMessage(player, ChatColor.RED + "ERROR", ChatColor.RED + "C'è stato un errore nel trovare il tuo balance. Reporta questo messaggio agli staffer.");
        }
    }

    private void sendBalance(CommandSender sender, String targetUsername){
        if(databaseManager.existsInDB(targetUsername)){
            ResultSet targetData = databaseManager.getProfileInfo(targetUsername);
            int targetCoins = 0;
            int targetPoints = 0;
            try {
                targetCoins = targetData.getInt("coins");
                targetPoints = targetData.getInt("points");
            } catch (SQLException exception) {
                ChatUtils.printException(exception.getMessage());
                ChatUtils.sendMessage(sender, ChatColor.RED + "ERROR", ChatColor.RED + "C'è stato un errore nel trovare il balance del player. Reporta questo messaggio agli staffer.");
            }
            sender.sendMessage(ChatColor.GREEN +"Il balance di " + ChatColor.AQUA + targetUsername + ChatColor.YELLOW + ":\n" + "Coins: " + ChatColor.WHITE + targetCoins + ChatColor.GOLD + "\nPoints: " + ChatColor.WHITE + targetPoints);
        }else{
            ChatUtils.sendMessage(sender, ChatColor.GREEN + "ECO", "Il player " + ChatColor.AQUA + targetUsername + ChatColor.WHITE + " non esiste.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        return new ArrayList<>();
    }
}
