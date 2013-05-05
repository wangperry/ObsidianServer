
package net.specialattack.ObsidianServer.util.thread;

import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;

public abstract class AnonymousThread extends Thread {
    public AnonymousThread(String name) {
        super(name);
    }

    private boolean isRunning = true;

    public abstract void runTask();

    @Override
    public void run() {
        while (this.isRunning) {
            this.runTask();

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
        this.isRunning = false;
    }
}
