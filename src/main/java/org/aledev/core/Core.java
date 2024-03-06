package org.aledev.core;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.aledev.core.Listeners.PlayerJoinListener;
import org.aledev.core.Listeners.PlayerQuitListener;
import org.aledev.core.Managers.DatabaseManager;
import org.aledev.core.Managers.ProfileManager;
import org.aledev.core.Utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;



public final class Core extends JavaPlugin {

    @Getter
    static Core instance;
    @Getter
    LuckPerms luckperms;
    @Getter
    ProfileManager profileManager;
    @Getter
    DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        ChatUtils.info("Core is starting up...");
        loadLuckperms();
        loadManagers();
        loadListeners();

        if(this.isEnabled()){
            ChatUtils.info("Core started up correctly!");
        }
    }

    @Override
    public void onDisable() {
        ChatUtils.info("Core is shutting down...");
        //databaseManager.shutdown();
        ChatUtils.info("Core shutted down correctly!");
    }

    public void loadLuckperms(){
        Plugin luckpermsPlugin = Bukkit.getPluginManager().getPlugin("LuckPerms");
        if(luckpermsPlugin == null || !luckpermsPlugin.isEnabled()){
            ChatUtils.error("Luckperms plugin not found or not enabled, disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
        }else{
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if(provider!=null){
                luckperms = provider.getProvider();
            }
        }
    }

    public void loadManagers(){
        databaseManager = new DatabaseManager(this, luckperms);
        profileManager = new ProfileManager(databaseManager, luckperms);
    }

    public void loadListeners(){
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

}
