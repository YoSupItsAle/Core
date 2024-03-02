package org.aledev.core.Models;
import org.aledev.core.Core;
import org.aledev.core.Utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerData implements Listener {

    private Core plugin = Core.getInstance();
    private UUID uuid;
    private String username;
    private Int coins = new Int();
    private Int points = new Int();
    private Rank rank = Rank.MEMBER;

    public PlayerData(){

    }

    public PlayerData(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public void load(Player player){
        System.out.println("LOADING");
        Core.getInstance().getSqlManager().select("SELECT * FROM players WHERE uuid = ?", resultSet -> {
            try {
                if (resultSet.next()) {
                    coins.setAmount(resultSet.getInt("coins"));
                    points.setAmount(resultSet.getInt("points"));
                    Core.getInstance().getSqlManager().execute("UPDATE players SET name = ? WHERE uuid = ?",
                            player.getName(), player.getUniqueId().toString());
                }else{
                    Core.getInstance().getSqlManager().execute("INSERT INTO players(uuid, name, coins, points, rank) VALUES (?, ?, ?, ?, ?)",
                            player.getUniqueId().toString(), player.getName(), 0, 0, Rank.MEMBER.toString());
                }
            }catch (SQLException exception){
                Color.log(Color.main("DEBUG", "There was an error loading player."));
            }
        }, player.getUniqueId().toString());
    }

    public void save(Player player){
        System.out.println("SAVING");
        Core.getInstance().getSqlManager().execute("UPDATE players SET coins = ?, points = ? WHERE uuid = ?", coins.getAmount(), points.getAmount(), player.getUniqueId().toString());
    }

    @EventHandler
    void handleLogin(AsyncPlayerPreLoginEvent event){
        try{
            plugin.getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
        }catch (NullPointerException exception){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "&c[ERROR] Could not create profile.");
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);
        try{
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profile.getPlayerData().load(player));
        }catch (NullPointerException exception){
            player.kickPlayer(ChatColor.RED + "[ERROR] Profile returned null.");
            return;
        }
        if(profile == null){
            player.kickPlayer(ChatColor.RED + "[ERROR] Profile returned null.");
            return;
        }
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);
        if(profile.getPlayerData() == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profile.getPlayerData().save(player));
    }

}
