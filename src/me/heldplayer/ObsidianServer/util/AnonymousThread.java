
package me.heldplayer.ObsidianServer.util;

import java.util.logging.Level;

import me.heldplayer.ObsidianServer.Server;

public abstract class AnonymousThread extends Thread {
    public AnonymousThread(String name) {
        super(name);
    }

    private boolean isRunning = true;

    public abstract void runTask();

    @Override
    public void run() {
        while (this.isRunning) {
            runTask();

            try {
                Thread.sleep(50L);
            }
            catch (InterruptedException e) {
                Server.log.log(Level.SEVERE, "Interrupted whilst sleeping", e);
                break;
            }
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
