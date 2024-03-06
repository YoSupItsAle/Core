package org.aledev.core.Listeners;

import org.aledev.core.Core;
import org.aledev.core.Models.Component;
import org.aledev.core.Models.Profile;
import org.aledev.core.Utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends Component implements Listener {

    public PlayerJoinListener(Core plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!event.getPlayer().hasPlayedBefore()){
            ChatUtils.broadcast(ChatColor.AQUA + event.getPlayer().getName() + ChatColor.WHITE + " è entrato per la prima volta nel server, dategli il benvenuto!");
            plugin.getProfileManager().createProfile(event.getPlayer());
            plugin.getProfileManager().addProfile(new Profile(event.getPlayer()));
        }else{
            plugin.getProfileManager().loadProfile(event.getPlayer());
            plugin.getProfileManager().addProfile(new Profile(event.getPlayer()));
        }

    }

}
