package io.github.dead_i.bungeehttpconsole;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.log.StdErrLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class BungeeHTTPConsole extends Plugin {
    private static Configuration config;

    public void onEnable() {
        // Get configuration
        reloadConfig();

        // Configuration reload command
        getProxy().getPluginManager().registerCommand(this, new Command("reloadhttp", "bungeehttpconsole.reload") {
            @Override
            public void execute(CommandSender sender, String[] args) {
                reloadConfig();
                sender.sendMessage(new TextComponent("The configuration has been reloaded."));
            }
        });

        // Setup logging
        org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());
        Properties p = new Properties();
        p.setProperty("org.eclipse.jetty.LEVEL", "WARN");
        StdErrLog.setProperties(p);

        // Setup the context
        ContextHandler context = new ContextHandler("/");
        SessionHandler sessions = new SessionHandler(new HashSessionManager());
        sessions.setHandler(new WebHandler(this));
        context.setHandler(sessions);

        // Setup the server
        final Server server = new Server(getConfig().getInt("port"));
        server.setSessionIdManager(new HashSessionIdManager());
        server.setHandler(sessions);
        server.setStopAtShutdown(true);

        // Start listening
        getProxy().getScheduler().runAsync(this, new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch(Exception e) {
                    getLogger().warning("Unable to bind web server to port.");
                    e.printStackTrace();
                }
            }
        });
    }

    public static Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                ByteStreams.copy(getResourceAsStream("config.yml"), new FileOutputStream(configFile));
                getLogger().warning("A new configuration file has been created. Please edit config.yml and restart BungeeCord, or run /reloadhttp");
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            if (config.getList("apikeys").size() == 0) {
                String[] apikeys = { UUID.randomUUID().toString().replace("-", "") };
                config.set("apikeys", apikeys);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
