package org.aledev.core.Managers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.aledev.core.Models.Profile;
import org.aledev.core.Utils.ChatUtils;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    HashMap<UUID, Profile> profiles;
    DatabaseManager databaseManager;
    LuckPerms luckperms;

    public ProfileManager(DatabaseManager databaseManager, LuckPerms luckperms) {
        this.profiles = new HashMap<>();
        this.databaseManager = databaseManager;
        this.luckperms = luckperms;
    }

    public boolean exists(Player player){
        return this.profiles.containsKey(player.getUniqueId());
    }

    public boolean exists(UUID uuid){
        return this.profiles.containsKey(uuid);
    }

    public boolean exists(Profile profile){
        return this.profiles.containsKey(profile.getUuid());
    }

    public void addProfile(Profile profile){
        if(!exists(profile)){
            this.profiles.put(profile.getUuid(), profile);
        }
    }

    public void removeProfile(Profile profile){
        if(exists(profile)){
            this.profiles.remove(profile.getUuid());
        }
    }

    public void removeProfile(UUID uuid){
        if(exists(uuid)){
            this.profiles.remove(uuid);
        }
    }

    public HashMap<UUID, Profile> getProfilesHashmap(){
        return this.profiles;
    }

    public List<Profile> getProfilesList(){
        return this.profiles.values().stream().toList();
    }

    public void createProfile(Player player){
        Profile profile = new Profile(player);
        databaseManager.createToDB(profile);
    }

    public void loadProfile(Player player){
        Profile profile = new Profile(player);
        ResultSet info = databaseManager.getProfileInfo(player.getUniqueId());
        if(info != null){
            try{
                profile.setCoins(info.getInt("coins"));
                profile.setPoints(info.getInt("points"));
                User user = luckperms.getUserManager().getUser(player.getUniqueId());
                user.setPrimaryGroup(info.getString("rank"));
                profile.setRank(user.getPrimaryGroup());
            }catch (SQLException exception){
                ChatUtils.printException(exception.getMessage());
                ChatUtils.error("Couldn't load profile data to profile from database.");
            }
        }else{
            createProfile(player);
        }
    }

    public void saveProfile(Player player){
        Profile profile = new Profile(player);
        databaseManager.saveToDB(profile);
    }








}
