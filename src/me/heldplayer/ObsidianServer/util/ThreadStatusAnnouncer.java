
package me.heldplayer.ObsidianServer.util;

import java.util.logging.Level;

import me.heldplayer.ObsidianServer.Server;

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
            if (isRunning && this.time + 1000L > System.currentTimeMillis()) {
                this.time = System.currentTimeMillis();

                Server.log.log(Level.INFO, message);
            }

            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {}
        }
    }

    public final void setMessage(String message) {
        this.message = message;
        if (!isRunning)
            resetTimer();

        isRunning = !message.isEmpty();
    }

    public final void resetTimer() {
        this.time = System.currentTimeMillis();
    }
}
