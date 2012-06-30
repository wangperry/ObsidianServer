package me.heldplayer.ObsidianServer;

import me.heldplayer.ObsidianServer.console.ConsoleCommandReader;

public class Server implements Runnable {
	private static Server instance = null;
	private boolean isRunning = true;
	private boolean hasShutDown = false;
	private ConsoleCommandReader conReader;
	private NetServerManager serverManager;

	@Override
	public void run() {
		instance = this;
		conReader = new ConsoleCommandReader();
		try {
			if (!startServer()) {
				isRunning = false;
			}
		} catch (Exception ex) {
			System.err.println("Unexpected exception while starting the server: " + ex.getMessage());
			ex.printStackTrace();
			isRunning = false;
		}

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
		serverManager = new NetServerManager(this);
		
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
