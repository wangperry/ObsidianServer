
package net.specialattack.ObsidianServer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.specialattack.ObsidianServer.network.NetServerManager;
import net.specialattack.ObsidianServer.util.Configuration;
import net.specialattack.ObsidianServer.util.io.ConsoleCommandReader;
import net.specialattack.ObsidianServer.util.io.ConsoleLogHandler;
import net.specialattack.ObsidianServer.util.io.LogFormatter;
import net.specialattack.ObsidianServer.util.thread.ThreadStatusAnnouncer;
import net.specialattack.ObsidianServer.world.World;


public class Server implements Runnable {
    private static Server instance = null;
    private boolean isRunning = true;
    private boolean hasShutDown = false;
    private ConsoleCommandReader conReader;
    private NetServerManager serverManager;
    private Configuration config;
    public String password = "";
    public int slots = 0;
    public World world;
    public byte worldSize = 0;
    public boolean isChristmas = false;
    public final static ThreadStatusAnnouncer announcer = new ThreadStatusAnnouncer();
    public static final Logger log = Logger.getLogger("ObsidianServer");

    @Override
    public void run() {
        log.setUseParentHandlers(false);

        ConsoleLogHandler handler = new ConsoleLogHandler();
        handler.setFormatter(new LogFormatter());

        log.addHandler(handler);
        log.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);

        instance = this;
        conReader = new ConsoleCommandReader();
        announcer.start();
        try {
            if (!startServer()) {
                isRunning = false;
            }
        }
        catch (Exception e) {
            Server.log.log(Level.SEVERE, "Unexpected exception while starting the server", e);
            isRunning = false;

            announcer.isRunning = false;
        }

        while (isRunning) {
            try {
                instance.serverLoop();
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                Server.log.log(Level.SEVERE, "Interrupted whilst sleeping", e);
            }
        }

        shutdown();

        System.out.println();
    }

    public boolean startServer() throws IOException {
        //--- Christmas check
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.MONTH) == 12) {
            int day = cal.get(Calendar.DAY_OF_MONTH);

            if (day >= 15 && day <= 31)
                this.isChristmas = true;
        }

        //--- Load config
        this.config = new Configuration(new File("config.txt"));

        //--- Password
        this.password = this.config.getString("password", "");

        //--- Slots
        this.slots = this.config.getInt("slots", 8);

        if (this.slots <= 0 || this.slots >= 255) {
            this.slots = 8;
            this.config.set("slots", 7777);
        }

        //--- Bind address
        String addr = this.config.getString("host-name", "");

        InetAddress inetAddr = null;

        if (addr.isEmpty())
            inetAddr = InetAddress.getByName(addr);

        //--- Port
        int port = this.config.getInt("port", 7777);

        if (port <= 0) {
            port = 7777;
            this.config.set("port", 7777);
        }

        Server.log.log(Level.INFO, "Starting the server on " + (!addr.isEmpty() ? addr : "*") + "; port = " + port);

        this.serverManager = new NetServerManager(this, port, inetAddr);

        //--- World
        this.worldSize = (byte) this.config.getInt("world-size", 1);

        String world = this.config.getString("world", "world");

        if (world.isEmpty()) {
            world = "world";
            this.config.set("world", "world");
        }

        this.world = new World(new File(world));

        return true;
    }

    public boolean serverLoop() {
        while (!conReader.consoleCommands.isEmpty()) {
            String command = conReader.consoleCommands.remove(0);
            Server.log.log(Level.FINE, "Command: " + command);

            if (command.equalsIgnoreCase("stop"))
                initiateShutdown();
        }

        serverManager.processConnections();

        return true;
    }

    protected void shutdown() {
        Server.log.log(Level.FINE, "Shutting down server...");

        world.saveWorldInfo();

        serverManager.stopConnection();
    }

    public void initiateShutdown() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning && !hasShutDown;
    }

    public static Server getInstance() {
        return instance;
    }

    public World getWorld() {
        return world;
    }
}
