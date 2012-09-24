package me.heldplayer.ObsidianServer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import me.heldplayer.ObsidianServer.util.Configuration;
import me.heldplayer.ObsidianServer.util.ConsoleCommandReader;

public class Server implements Runnable {
	private static Server instance = null;
	private boolean isRunning = true;
	private boolean hasShutDown = false;
	private ConsoleCommandReader conReader;
	private NetServerManager serverManager;
	private Configuration config;
	public String password = "";
	public int slots = 0;

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

		shutdown();
	}

	public boolean startServer() throws IOException {
		config = new Configuration(new File("config.txt"));

		password = config.getString("password", "");
		slots = config.getInt("slots", 8);

		String addr = config.getString("host-name", "");

		InetAddress inetAddr = null;

		if (addr.length() > 0)
			inetAddr = InetAddress.getByName(addr);

		int port = config.getInt("port", 7777);

		System.out.println("Starting the server on " + (!addr.equalsIgnoreCase("") ? addr : "*") + "; port = " + port);

		serverManager = new NetServerManager(this, port, inetAddr);

		return true;
	}

	public boolean serverLoop() {
		while (!conReader.consoleCommands.isEmpty()) {
			String command = conReader.consoleCommands.remove(0);
			System.out.println("Command: " + command);

			if (command.equalsIgnoreCase("stop"))
				initiateShutdown();
		}

		serverManager.processConnections();

		return true;
	}

	protected void shutdown() {
		System.out.println("Shutting down server...");

		serverManager.stopConnection();
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
