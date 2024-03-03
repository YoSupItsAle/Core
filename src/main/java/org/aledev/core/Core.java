package org.aledev.core;

import lombok.Getter;
import org.aledev.core.Commands.BalanceCommand;
import org.aledev.core.Managers.ChatManager;
import org.aledev.core.Managers.ProfileManager;
import org.aledev.core.Managers.SqlManager;
import org.aledev.core.Models.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;



public final class Core extends JavaPlugin {

    @Getter
    static Core instance;
    @Getter
    SqlManager sqlManager;
    @Getter
    ProfileManager profileManager;

    @Override
    public void onEnable() {
        instance = this;
        loadManagers();
        loadListeners();
        loadCommands();

    }

    @Override
    public void onDisable() {
        if(sqlManager !=null){
            sqlManager.shutdown();
        }
    }

    private void loadManagers(){
        sqlManager = new SqlManager(this);
        sqlManager.connect();
        profileManager = new ProfileManager(this);
    }

    private void loadListeners(){
        getServer().getPluginManager().registerEvents(new PlayerData(), this);
        getServer().getPluginManager().registerEvents(new ChatManager(this), this);
    }

    private void loadCommands(){
        getCommand("balance").setExecutor(new BalanceCommand(this));
    }
}
