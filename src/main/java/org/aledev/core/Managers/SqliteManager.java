package org.aledev.core.Managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.aledev.core.Core;
import org.aledev.core.Manager;
import org.aledev.core.Utils.Color;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqliteManager extends Manager {

    private HikariDataSource hikariDataSource;

    public SqliteManager(Core plugin) {
        super(plugin);
        connect();
        Color.log("Enabled SqliteManager");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::createTables);

    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    /**
     * Calls the close() function and logs the disabling of the SqliteManager.
     */
    public void shutdown(){
        close();
        Color.log("SqliteManager has been disabled.");
    }


    /**
     * Creates all the tables needed, here's where tables are added.
     */
    public void createTables(){
        Color.log("Creating tables...");
        createTable("player", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), coins INT(11), points INT(11), rank VARCHAR(20)");
    }

    /**
     * Connects to Database.
     * @return Exception if connection fails.
     */
    public Exception connect(){
        try{
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "CoreData");
            hikariConfig.setUsername("Server");
            hikariConfig.setPassword("CoreServer");
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setMaximumPoolSize(50);
            hikariConfig.setConnectionTimeout(30000);
            hikariConfig.setLeakDetectionThreshold(60*1000);
            hikariConfig.setIdleTimeout(600000);
            hikariConfig.setMaxLifetime(1800000);
            this.hikariDataSource = new HikariDataSource(hikariConfig);

        }catch (Exception exception){
            this.hikariDataSource = null;
            return exception;
        }

        return null;
    }

    /**
     * Checks if database is initialized or not.
     * @return If database is initialized return True, else False.
     */
    public boolean isInitiated(){
        return this.hikariDataSource != null;
    }

    /**
     * Closes Database.
     */
    public void close(){
        this.hikariDataSource.close();
    }


    /**
     * Creates tables into the database.
     * @param name The name of the table.
     * @param info The table info between the round VALUES() brackets.
     */
    public void createTable(String name, String info){
        new Thread(() -> {
            try(Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")){
                statement.execute();
            }catch (SQLException exception){
                Color.log("An error occurred while creating database table " + name + ".");
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Executes statements to the database.
     * @param query The statement to the database.
     * @param values The values to be inserted to the statement.
     */
    public void execute(String query, Object... values){
        new Thread(() -> {
            try(Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)){
                for(int i = 0; i<values.length; i++){
                    statement.setObject((i+1), values[i]);
                }
            }catch (SQLException exception){
                Color.log("An error occured while executing an update to the database.");
                Color.log("Sqlite#execute : " + query);
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Executes queries to the database.
     * @param query The statement to the database.
     * @param callback The data callback (Async).
     * @param values The values to be inserted into the statement.
     */
    public void select(String query, SelectCall callback, Object... values){
        new Thread(() -> {
            try(Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)){
                for(int i = 0; i< values.length; i++){
                    statement.setObject((i+1), values[i]);
                }
                callback.call(statement.executeQuery());
            }catch (SQLException exception){
                Color.log("An error occurred while executing a query on the database");
                Color.log("Sqlite#select : " + query);
                exception.printStackTrace();
            }
        }).start();
    }


}
