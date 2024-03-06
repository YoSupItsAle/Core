package org.aledev.core.Listeners;

import org.aledev.core.Core;
import org.aledev.core.Models.Component;
import org.aledev.core.Models.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends Component implements Listener {

    public PlayerQuitListener(Core plugin) {
        super(plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        plugin.getProfileManager().saveProfile(event.getPlayer());
        plugin.getProfileManager().removeProfile(event.getPlayer().getUniqueId());
    }



}
