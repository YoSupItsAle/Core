package org.aledev.core;

import lombok.Getter;

import java.util.UUID;

@Getter

public class Profile {

    private Core core = Core.getInstance();
    private UUID uuid;
    private String username;
    private  PlayerData playerData;

    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.playerData = new PlayerData(uuid, username);
    }
}
