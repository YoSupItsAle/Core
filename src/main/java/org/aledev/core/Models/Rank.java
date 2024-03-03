package org.aledev.core.Models;

import lombok.Getter;
import org.aledev.core.Utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Rank {


    OWNER("OWNER", ChatColor.DARK_PURPLE, true, false, 100),
    ADMIN("ADMIN", ChatColor.RED, true, false, 10),
    PREMIUM("PREMIUM", ChatColor.GOLD, true, false, 4),
    VIP_PLUS("VIP+", ChatColor.DARK_GREEN, false, false, 3),
    VIP("VIP", ChatColor.GREEN , false, false, 2),
    MEMBER("Member", ChatColor.GRAY, false, false, 1);



    @Getter
    private String name;
    @Getter
    private ChatColor color;
    @Getter
    private boolean bold, ital;

    private int level;

    Rank(String name, ChatColor color, boolean bold, boolean ital, int level){
        this.name = name;
        this.color = color;
        this.bold = bold;
        this.ital = ital;
        this.level = level;
    }

    /**
     * Checks if rank exists.
     * @param rank Rank name.
     * @return If rank exists returns true else false.
     */
    public static boolean contains(String rank){

        for(Rank ranks : Rank.values()){
            if(ranks.name().equals(rank)){
                return true;
            }
        }
        return false;
    }

    public boolean isHigher(Rank rankToCompare){
        return this.level > rankToCompare.level;
    }

    public boolean isLower(Rank rankToCompare){
        return  this.level < rankToCompare.level;
    }


    public boolean isHigherOrEqualTo(Player player, Rank rank, boolean callback){
        if(callback && this.level < rank.level){
            player.sendMessage(Color.main("&cPERMISSIONS", "&cYou need to be atleast rank " + rank.name() + " to use this command."));
            return false;
        }
        if(this.level >= rank.level){
            return true;
        }
        return false;
    }

    public boolean isLowerOrEqualTo(Rank rankToCompare){
        return this.level <= rankToCompare.level;
    }

    public String getPrefix(){
        String name = this.name.toUpperCase();

        if(bold && ital) return Color.translate(this.color + "&o&l[" + name + "]");
        if(bold) return Color.translate(this.color + "&l[" + name + "]");
        if(ital) return Color.translate(this.color + "&o[" + name + "]");

        return Color.translate(this.color + "[" + name + "]");

    }


}
