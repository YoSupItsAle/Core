package org.aledev.core.Managers;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.TextComponent;
import org.aledev.core.Core;
import org.aledev.core.Models.CoreManager;
import org.aledev.core.Models.Profile;
import org.aledev.core.Models.Rank;
import org.aledev.core.Utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatManager extends CoreManager implements Listener {

    public ChatManager(Core plugin) {
        super(plugin);
        Color.log("Enabled ChatManager");
    }

    private final List<String> BLOCKED_COMMANDS = Lists.newArrayList("pl", "plugins", "ver", "version", "icanhasbukkit", "about", "op");

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        String commandName = event.getMessage().substring(1);
        String argString = event.getMessage().substring(event.getMessage().indexOf(' ') + 1);
        String[] args = new String[]{};
        Profile profile = plugin.getProfileManager().getProfile(event.getPlayer());
        String lowercase = event.getMessage().toLowerCase();
        if(lowercase.startsWith("/me") || lowercase.startsWith("/bukkit") || lowercase.startsWith("/minecraft")){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Color.main("CHAT", "&cYou cannot use this command."));
        }

        if(BLOCKED_COMMANDS.contains(commandName.toLowerCase()) && !profile.getPlayerData().getRank().isHigherOrEqualTo(event.getPlayer(), Rank.ADMIN, false)){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Color.main("CHAT", "&cYou cannot use this command."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handleChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);
        String message = event.getMessage();

        if(event.isCancelled() || message.isEmpty()){
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        message = ChatColor.translateAlternateColorCodes('&', message);

        final TextComponent formatted = new net.md_5.bungee.api.chat.TextComponent("");
        String rank = profile.getPlayerData().getRank().getPrefix();
        TextComponent rankComponent = new TextComponent(rank + " ");
        TextComponent playerNameText = new TextComponent(Color.translate(profile.getPlayerData().getRank().getColor() + player.getDisplayName()));
        TextComponent messageContent = new TextComponent(" " + message);
        formatted.addExtra(rankComponent);
        formatted.addExtra(playerNameText);
        formatted.addExtra(messageContent);

        event.getRecipients().forEach(players -> players.spigot().sendMessage(formatted));
        System.out.println(rank + " " + player.getDisplayName() + " " + message);

    }
}