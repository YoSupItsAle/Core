package org.aledev.core;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
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

    @Override
    public void onEnable() {
        instance = this;
        ChatUtils.info("Core is starting up...");
        loadLuckperms();


        if(this.isEnabled()){
            ChatUtils.info("Core started up correctly!");
        }
    }

    @Override
    public void onDisable() {
        ChatUtils.info("Core is shutting down...");
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
}
