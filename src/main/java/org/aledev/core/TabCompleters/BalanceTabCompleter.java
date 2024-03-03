package org.aledev.core.TabCompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BalanceTabCompleter implements TabCompleter {

    List<String> result = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();

        // bal <name>
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                result.add(player.getName());
            }
        } else if (args.length == 2) {
            // bal <set or add or remove> <name>
            String arg = args[1].toLowerCase();
            for (String action : Arrays.asList("set", "add", "remove")) {
                if (action.startsWith(arg)) {
                    result.add(action);
                }
            }
        } else if (args.length == 3) {
            // bal <set or add or remove> <name> <coins|points>
            String arg = args[2].toLowerCase();
            for(String action : Arrays.asList("coins", "points")){
                if(action.startsWith(action)){
                    result.add(action);
                }
            }
        }


        return result;
    }
}
