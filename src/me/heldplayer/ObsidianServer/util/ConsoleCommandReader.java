
package me.heldplayer.ObsidianServer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;

import me.heldplayer.ObsidianServer.Server;

public class ConsoleCommandReader extends Thread {
    public final ArrayList<String> consoleCommands;
    private final BufferedReader bufRead;

    public ConsoleCommandReader() {
        consoleCommands = new ArrayList<String>();
        bufRead = new BufferedReader(new InputStreamReader(System.in));
        setDaemon(true);
        setName("Console command reader thread");
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (bufRead.ready()) {
                    consoleCommands.add(bufRead.readLine());
                }
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                Server.log.log(Level.SEVERE, "Interrupted whilst sleeping", e);
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Error reading", e);
            }
        }
    }
}
