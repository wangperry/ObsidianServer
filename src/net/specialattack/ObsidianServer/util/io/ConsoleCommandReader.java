
package net.specialattack.ObsidianServer.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;

public class ConsoleCommandReader extends Thread {
    public final ArrayList<String> consoleCommands;
    private final BufferedReader bufRead;

    public ConsoleCommandReader() {
        this.consoleCommands = new ArrayList<String>();
        this.bufRead = new BufferedReader(new InputStreamReader(System.in));
        this.setDaemon(true);
        this.setName("Console command reader thread");
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (this.bufRead.ready()) {
                    this.consoleCommands.add(this.bufRead.readLine());
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
