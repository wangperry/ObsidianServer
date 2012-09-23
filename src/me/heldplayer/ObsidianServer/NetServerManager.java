package me.heldplayer.ObsidianServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class NetServerManager {
	protected ArrayList<NetServerChild> children;
	private Server server;
	private ConnectionListenThread listenThread;

	public NetServerManager(Server server, int port, InetAddress address) throws IOException {
		this.server = server;

		listenThread = new ConnectionListenThread(this, port, address);
	}

	public void stopConnection() {
		listenThread.isListening = false;
	}
}
