package org.aledev.core.Managers;

import org.aledev.core.Core;
import org.aledev.core.Models.CoreManager;
import org.aledev.core.Models.Profile;
import org.aledev.core.Utils.Color;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager extends CoreManager {

    private Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(Core plugin) {
        super(plugin);
        Color.log("Enabled ProfileManager");

    }

    /**
     * Puts profile in hashmap if not in it.
     * @param uuid Player uuid.
     * @param username Player username.
     */
    public void handleProfileCreation(UUID uuid, String username){
        if(!this.profiles.containsKey(uuid)){
            profiles.put(uuid, new Profile(uuid, username));
        }
    }

    /**
     * Gets profile from hashmap.
     * @param object Player or UUID or Username
     * @return If player found returns profile else null.
     */
    public Profile getProfile(Object object){
        if(object instanceof Player){
            Player target = (Player) object;
            if(!this.profiles.containsKey(target.getUniqueId())){
                return null;
            }
            return profiles.get(target.getUniqueId());
        }

        if(object instanceof UUID){
            UUID uuid = (UUID) object;
            if(!this.profiles.containsKey(uuid)){
                return null;
            }
            return this.profiles.get(uuid);
        }

        if(object instanceof String){
            String string = (String) object;
            return this.profiles.values().stream().filter(profile -> profile.getUsername().equalsIgnoreCase(object.toString())).findFirst().orElse(null);
        }

        return null;
    }

    /**
     * Gets profiles hashmap.
     * @return Returns profile hashmap.
     */
    public Map<UUID, Profile> getProfiles(){
        return this.profiles;
    }

    /**
     * Sets hashmap to a provided hashmap.
     * @param profiles A new hashmap to set the profiles hashmap to.
     */
    public void setProfiles(Map<UUID, Profile> profiles){
        this.profiles = profiles;
    }


}
