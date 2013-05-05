
package net.specialattack.ObsidianServer.util.thread;

import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;

public class ThreadStatusAnnouncer extends Thread {
    private String message = "";
    private long time = 0L;
    public boolean isRunning = false;

    public ThreadStatusAnnouncer() {
        this.setDaemon(true);
        this.setName("Progress Announcer Thread");
    }

    @Override
    public void run() {
        while (true) {
            if (this.isRunning && this.time + 1000L > System.currentTimeMillis()) {
                this.time = System.currentTimeMillis();

                Server.log.log(Level.INFO, this.message);
            }

            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {}
        }
    }

    public final void setMessage(String message, boolean isUpdate) {
        this.message = message;
        if (!this.isRunning) {
            this.resetTimer();
        }

        this.isRunning = message != null && !message.isEmpty();

        if (this.isRunning && !isUpdate) {
            this.time = System.currentTimeMillis();

            Server.log.log(Level.INFO, message);
        }
    }

    public final void resetTimer() {
        this.time = System.currentTimeMillis();
    }
}
