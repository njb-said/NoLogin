package me.imnjb.nologin;

import java.io.*;
import java.util.Properties;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class NoLogin extends Plugin implements Listener {

    // TODO Possibly edit motd and/or ping
    private String message;
    private boolean log;

    @Override
    public void onEnable() {
        try {
            if(!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            File file = new File(getDataFolder(), "nologin.properties");
            Properties prop = new Properties();
            if(!file.exists()) {
                file.createNewFile();
                InputStream stream = new FileInputStream(file);
                prop.load(stream);

                prop.setProperty("message", "&cWe aren't ready to open yet!");
                prop.setProperty("log", "false");

                prop.store(new FileOutputStream(file), "nologin defaults");
                getLogger().info("Creating default nologin.properties");
            }

            InputStream stream = new FileInputStream(file);
            prop.load(stream);

            log = Boolean.parseBoolean(prop.getProperty("log", "false"));
            message = ChatColor.translateAlternateColorCodes('&', prop.getProperty("message", "&cWe aren't ready to open yet!"));
        } catch(IOException ex) {
            ex.printStackTrace();
            getLogger().severe("Failed to make nologin.properties file");
            return;
        }

        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        e.setCancelled(true);
        e.setCancelReason(message);

        if(log) {
            getLogger().info(e.getConnection().getName() + " tried to join, not allowing.");
        }
    }

}
