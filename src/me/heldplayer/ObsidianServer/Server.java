package me.heldplayer.ObsidianServer;

import me.heldplayer.ObsidianServer.console.ConsoleCommandReader;

public class Server implements Runnable {
	private static Server instance = null;
	private boolean isRunning = true;
	private boolean hasShutDown = false;
	private ConsoleCommandReader conReader;

	@Override
	public void run() {
		instance = this;
		conReader = new ConsoleCommandReader();

		while (isRunning) {
			try {
				instance.serverLoop();
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean startServer() {
		return true;
	}

	public boolean serverLoop() {
		while (!conReader.consoleCommands.isEmpty()) {
			String command = conReader.consoleCommands.remove(0);
			System.out.println("Command: " + command);
		}

		return true;
	}

	protected void shutdown() {
		isRunning = false;
		System.out.println("Shutting down server...");
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

}
