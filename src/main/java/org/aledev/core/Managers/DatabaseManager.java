package org.aledev.core.Managers;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.aledev.core.Core;
import org.aledev.core.Models.Component;
import org.aledev.core.Models.Profile;
import org.aledev.core.Utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager extends Component {

    private final String DB_URL = "jdbc:mysql://localhost:3306/core";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    @Getter
    Connection connection;
    private LuckPerms luckPerms;

    public DatabaseManager(Core plugin, LuckPerms luckPerms) {
        super(plugin);
        this.luckPerms = luckPerms;
        initialize();
    }

    public void initialize(){
        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            this.connection = conn;
            createTables();
        }catch (SQLException exception){
            ChatUtils.printException(exception.getMessage());
            ChatUtils.error("Cannot connect to database, disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public void shutdown(){
        try{
            if(this.connection != null && !this.connection.isClosed()){
                this.connection.close();
            }
        }catch (SQLException exception){
            ChatUtils.printException(exception.getMessage());
            ChatUtils.error("Cannot shutdown database.");
        }
    }

    public void createTables(){
        try{
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS profiles ("
                                                                           + "uuid VARCHAR(36) PRIMARY KEY,"
                                                                           + " username VARCHAR(16),"
                                                                           + " coins INT,"
                                                                           + " points INT,"
                                                                           + " rank VARCHAR(20));");
            statement.execute();
            ChatUtils.info("Created " + ChatColor.GREEN + "profiles " + ChatColor.WHITE + "table succesfully.");
        }catch (SQLException exception){
            ChatUtils.printException(exception.getMessage());
            ChatUtils.error("Couldn't create tables, disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }


    public boolean existsInDB(UUID uuid){
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }catch (SQLException exception){
            ChatUtils.printException(exception.getMessage());
            ChatUtils.error("Couldn't check if profile exists in database.");
        }

        return false;
    }

    public void createToDB(Profile profile){
        if(!existsInDB(profile.getUuid())){
            try{
                PreparedStatement statement = this.connection.prepareStatement("INSERT INTO profiles VALUES (?, ?, ?, ?, ?)");
                statement.setString(1, profile.getUuid().toString());
                statement.setString(2, profile.getUsername());
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                User user = luckPerms.getUserManager().getUser(profile.getUuid());
                statement.setString(5, user.getPrimaryGroup());
            }catch (SQLException exception){
                ChatUtils.printException(exception.getMessage());
                ChatUtils.error("Couldn't create new profile to database.");
            }
        }
    }

    public ResultSet getProfileInfo(UUID uuid){
        if(existsInDB(uuid)){
            try{
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                return statement.executeQuery();
            }catch (SQLException exception){
                ChatUtils.printException(exception.getMessage());
                ChatUtils.error("Couldn't load profile from database.");
            }
        }
        return null;
    }

    //public void getProfileInfo(String string){
        //TODO to get coins, points and rank even if player is offline. (Same as getProfileInfo with uuid but with username.
    //}


    public void saveToDB(Profile profile){
        if(existsInDB(profile.getUuid())){
            try{
                PreparedStatement statement = this.connection.prepareStatement("UPDATE profiles SET coins = ?, points = ?, rank = ? WHERE uuid = ?");
                statement.setInt(1, profile.getCoins());
                statement.setInt(2, profile.getPoints());
                statement.setString(3, profile.getRank());
                statement.execute();
            }catch (SQLException exception){
                ChatUtils.printException(exception.getMessage());
                ChatUtils.error("Couldn't save profile to database.");
            }
        }
    }


}
