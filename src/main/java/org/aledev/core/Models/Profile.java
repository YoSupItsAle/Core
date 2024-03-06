package org.aledev.core.Models;

import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    @Getter
    String username;

    @Getter
    UUID uuid;

    @Setter
    @Getter
    String rank;

    @Setter
    @Getter
    int coins;

    @Setter
    @Getter
    int points;

    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
    }

    public Profile(Player player){
        this.username = player.getName();
        this.uuid = player.getUniqueId();
    }




}
