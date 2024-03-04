package org.aledev.core.Commands;

import org.aledev.core.Core;
import org.aledev.core.Models.CoreCommand;
import org.aledev.core.Models.Profile;
import org.aledev.core.Models.Rank;
import org.aledev.core.Utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends CoreCommand implements CommandExecutor{

    public BalanceCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        if(sender instanceof Player player){
            Profile profile = plugin.getProfileManager().getProfile(player);
            if(args.length < 1){
                //bal
                player.sendMessage(Color.translate("&a&l|&r Your balance:\n&a&l|&r Coins: " + profile.getPlayerData().getCoins().getAmount() + "\n&a&l|&r Points: ") + profile.getPlayerData().getPoints().getAmount());
                return true;
            }else {
                if (args.length == 1) {
                    //bal <name>
                    String targetUsername = args[0];
                    Profile targetProfile = plugin.getProfileManager().getProfile(targetUsername);
                    if (targetProfile == null) {
                        player.sendMessage(Color.main("&aBALANCE", "&cThat player is not existent."));
                        return true;
                    }

                    player.sendMessage(Color.translate("&a&l|&r " + targetProfile.getUsername() + "'s balance:\n&a&l|&r Coins: " + targetProfile.getPlayerData().getCoins().getAmount() + "\n&a&l|&r Points: ") + targetProfile.getPlayerData().getPoints().getAmount());
                    return true;
                }

                if (profile.getPlayerData().getRank().isHigherOrEqualTo(player, Rank.ADMIN, true)) {
                    if (args.length < 4) {
                        player.sendMessage(Color.main("&aECO", "&cUsage: /bal <player> <set|add|remove> <coins|points> <quantity>"));
                        return true;
                    }

                    String function = args[1];
                    String currency = args[2];
                    String targetUsername = args[0];
                    int amount;

                    //check if amount is actually a number.
                    try{
                        amount = Integer.parseInt(args[3]);
                    }catch (NumberFormatException exception){
                        player.sendMessage(Color.main("&aECO", "&cThe quantity is not a valid number."));
                        return true;
                    }


                    Profile targetProfile = plugin.getProfileManager().getProfile(targetUsername);

                    if (targetProfile == null) {
                        player.sendMessage(Color.main("&aECO", "&cThat player is not existent."));
                        return true;
                    }

                    switch (function) {
                        case "set":
                            switch (currency){
                                case "coins":
                                    targetProfile.getPlayerData().getCoins().setAmount(amount);
                                    player.sendMessage(Color.main("&aECO", "&aSet " + targetProfile.getUsername() + "'s coins to " + amount + "."));
                                    break;

                                case "points":
                                    targetProfile.getPlayerData().getPoints().setAmount(amount);
                                    player.sendMessage(Color.main("&aECO", "&aSet " + targetProfile.getUsername() + "'s points to " + amount + "."));
                                    break;
                            }
                        break;

                        case "add":
                            switch (currency){
                                case "coins":
                                    targetProfile.getPlayerData().getCoins().increaseAmount(amount);
                                    player.sendMessage(Color.main("&aECO", "&aAdded " + amount + " coins to " + targetProfile.getUsername() + "'s balance."));
                                break;

                                case "points":
                                    targetProfile.getPlayerData().getPoints().increaseAmount(amount);
                                    player.sendMessage(Color.main("&aECO", "&aAdded " + amount + " points to " + targetProfile.getUsername() + "'s balance."));
                                break;
                            }
                        break;

                        case "remove":
                            switch (currency){
                                case "coins":
                                    if (targetProfile.getPlayerData().getCoins().getAmount() >= amount) {
                                        targetProfile.getPlayerData().getCoins().decreaseAmount(amount);
                                        player.sendMessage(Color.main("&aECO", "&aRemoved " + amount + " coins from " + targetProfile.getUsername() + "'s balance."));
                                    } else {
                                        player.sendMessage(Color.main("&aECO", "&c" + targetProfile.getUsername() + " doesn't have that many coins."));
                                    }
                                break;

                                case "points":
                                    if (targetProfile.getPlayerData().getPoints().getAmount() >= amount) {
                                        targetProfile.getPlayerData().getPoints().decreaseAmount(amount);
                                        player.sendMessage(Color.main("&aECO", "&aRemoved " + amount + " points from " + targetProfile.getUsername() + "'s balance."));
                                    } else {
                                        player.sendMessage(Color.main("&aECO", "&c" + targetProfile.getUsername() + " doesn't have that many points."));
                                    }
                                break;
                            }
                        break;

                        default:
                            player.sendMessage(Color.main("&aECO", "&cUsage: /bal <player> <set|add|remove> <coins|points> <quantity>"));
                        break;
                    }
                }
            }
        }else{
            sender.sendMessage(Color.main("&aECO", "&cThat command is only for players. (Temporary)"));
        }

        return true;
    }
}
