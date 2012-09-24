package me.heldplayer.ObsidianServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import me.heldplayer.ObsidianServer.packets.Packet;

public class NetServerManager {
	protected volatile ArrayList<NetServerChild> children;
	private Server server;
	private ConnectionListenThread listenThread;

	public NetServerManager(Server server, int port, InetAddress address) throws IOException {
		this.server = server;
		this.children = new ArrayList<NetServerChild>();

		this.listenThread = new ConnectionListenThread(this, port, address);
		this.listenThread.setDaemon(true);
		this.listenThread.start();
	}

	public void stopConnection() {
		listenThread.isListening = false;
	}

	protected void processConnections() {
		ArrayList<NetServerChild> children = new ArrayList<NetServerChild>();
		children.addAll(this.children);

		for (NetServerChild child : children) {
			try {
				if (child.hasPackets()) {
					Packet packet = child.getNextPacket();

					packet.handlePacket(child);
				}
			} catch (Exception ex) {
				System.out.println("Failed reading packet!");

				ex.printStackTrace();
			}
		}
	}
}
