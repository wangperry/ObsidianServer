
package me.heldplayer.ObsidianServer.util;

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
                e.printStackTrace();
                break;
            }
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
