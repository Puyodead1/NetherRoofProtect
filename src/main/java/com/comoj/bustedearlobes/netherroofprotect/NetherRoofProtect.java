package com.comoj.bustedearlobes.netherroofprotect;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class NetherRoofProtect extends JavaPlugin {
    private int scheduleDelay = 10;
    public String teleportWorldName;

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.teleportWorldName = getConfig().getString("teleportWorldName");
        if (getServer().getWorld(this.teleportWorldName) == null) {
            getLogger().severe("WORLD TO TELEPORT TO WAS NOT FOUND! You must supply a world even if you are not teleporting players. Check your config file to make sure everything is correct. Plugin disabled.");
            getServer().getPluginManager().disablePlugin((Plugin) this);
        } else {
            this.scheduleDelay = getConfig().getInt("checkDelay");
            if (this.scheduleDelay < 5) {
                this.scheduleDelay = 5;
            }
            getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, (Runnable) new PlayerHeightCheckTask(this), this.scheduleDelay, this.scheduleDelay);
        }
    }

    public void onDisable() {
    }
}