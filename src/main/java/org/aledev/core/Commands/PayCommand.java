package org.aledev.core.Commands;
import org.aledev.core.Core;
import org.aledev.core.Models.CoreCommand;
import org.aledev.core.Models.Profile;
import org.aledev.core.Utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends CoreCommand implements CommandExecutor {
    public PayCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p){
            if(args.length < 2){
                p.sendMessage(Color.main("&aECO", "&cUsage: /pay <player> <quantity>"));
                return true;
            }

            Profile playerProfile = plugin.getProfileManager().getProfile(p);


            String targetString = args[0];
            Profile targetProfile = plugin.getProfileManager().getProfile(targetString);
            if(targetProfile== null){
                p.sendMessage(Color.main("&aECO", "&cThat player is non existent."));
                return true;
            }

            if(targetProfile == playerProfile){
                p.sendMessage(Color.main("&aECO", "&cYou can't pay yourself."));
                return true;
            }

            int amount;
            //check if amount is actually a number.
            try{
                amount = Integer.parseInt(args[1]);
            }catch (NumberFormatException exception){
                p.sendMessage(Color.main("&aECO", "&cThe amount is not a valid number."));
                return true;
            }

            int balance = playerProfile.getPlayerData().getCoins().getAmount();

            if(balance >= amount){
                playerProfile.getPlayerData().getCoins().decreaseAmount(amount);
                targetProfile.getPlayerData().getCoins().increaseAmount(amount);

                p.sendMessage(Color.main("&aECO", "You paid &b" + targetProfile.getUsername() + " &a" + amount + " &rcoins."));
                Bukkit.getPlayer(targetProfile.getUuid()).sendMessage(Color.main("&aECO", "&b" + playerProfile.getUsername() + " &rhas just paid you &a" + amount + " &rcoins."));
            }else{
                p.sendMessage(Color.main("&aECO", "&cYou don't have that many coins."));
            }





        }else{
            sender.sendMessage(Color.main("&aECO", "&cYou have to be a player to run that command."));
        }


        return true;
    }
}
