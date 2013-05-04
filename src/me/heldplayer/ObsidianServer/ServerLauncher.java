
package me.heldplayer.ObsidianServer;

import java.util.logging.Level;

public class ServerLauncher {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Server instance = new Server();
        Thread serverThread = new Thread(instance, "Main server thread");
        serverThread.setDaemon(true);
        serverThread.start();

        while (instance.isRunning() && serverThread.isAlive()) {
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException e) {
                Server.log.log(Level.SEVERE, "Error while sleeping!", e);
            }
        }

        if (instance.isRunning()) {
            instance.shutdown();
        }

        Server.log.log(Level.INFO, "Server stopped!");
    }

}
