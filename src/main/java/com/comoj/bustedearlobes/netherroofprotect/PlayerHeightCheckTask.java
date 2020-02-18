package com.comoj.bustedearlobes.netherroofprotect;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerHeightCheckTask extends BukkitRunnable {
    private NetherRoofProtect plugin;
    public static final String X_CORD = "%X_CORD%";
    public static final String Y_CORD = "%Y_CORD%";
    public static final String Z_CORD = "%Z_CORD%";
    public static final String PLAYER_NAME = "%PLAYER_NAME%";

    public PlayerHeightCheckTask(NetherRoofProtect netherRoofProtect) {
        this.plugin = netherRoofProtect;
        this.configString = this.plugin.getConfig().getString("teleportMessage");
        this.overrideTeleport = Boolean.valueOf(this.plugin.getConfig().getBoolean("overrideTeleport"));
        this.overrideCommand = this.plugin.getConfig().getString("overrideCommand");
        this.maxHeight = this.plugin.getConfig().getInt("maxNetherHeight");
        this.spawnWorld = this.plugin.teleportWorldName;
    }

    private String configString;
    private Boolean overrideTeleport;
    private String overrideCommand;
    private String spawnWorld;
    private int maxHeight;

    public void run() {
        for (World world : this.plugin.getServer().getWorlds()) {
            if (world != null && world.getEnvironment().equals(World.Environment.NETHER)) {
                for (Player player : world.getPlayers()) {
                    if (player.getLocation().getY() > this.maxHeight && !player.hasPermission("netherroofprotect.allow")) {
                        sendMessage(this.configString, player, (int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ());
                        if (!this.overrideTeleport.booleanValue()) {
                            player.teleport(this.plugin.getServer().getWorld(this.spawnWorld).getSpawnLocation(),
                                    PlayerTeleportEvent.TeleportCause.PLUGIN);
                            continue;
                        }
                        this.plugin.getLogger().info("Running command on player...");
                        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getConsoleSender(), processString(this.overrideCommand, player, (int) player.getLocation().getX(),
                                (int) player.getLocation().getY(), (int) player.getLocation().getZ()));
                    }
                }
            }
        }
    }

    private String processString(String string, Player player, int x, int y, int z) {
        return string.replace("%PLAYER_NAME%", player.getName())
                .replace("%X_CORD%", (new StringBuilder(String.valueOf(x))).toString())
                .replace("%Y_CORD%", (new StringBuilder(String.valueOf(y))).toString())
                .replace("%Z_CORD%", (new StringBuilder(String.valueOf(z))).toString());
    }

    private void sendMessage(String string, Player player, int x, int y, int z) {
        player.sendMessage(processString(string, player, x, y, z).split("\n"));
    }
}