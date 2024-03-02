package org.aledev.core;

import lombok.Getter;
import org.aledev.core.Managers.SqliteManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Getter
    Core instance;
    SqliteManager sqliteManager;

    @Override
    public void onEnable() {
        instance = this;
        loadManagers();

    }

    @Override
    public void onDisable() {
        if(sqliteManager!=null){
            sqliteManager.shutdown();
        }
    }

    private void loadManagers(){
        sqliteManager = new SqliteManager(this);
        sqliteManager.connect();
    }
}
