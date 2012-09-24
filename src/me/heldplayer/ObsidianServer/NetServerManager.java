package me.heldplayer.ObsidianServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import me.heldplayer.ObsidianServer.packets.Packet;

public class NetServerManager {
	private Server server;
	private ConnectionListenThread listenThread;
	protected NetServerChild[] slots;

	public NetServerManager(Server server, int port, InetAddress address) throws IOException {
		this.server = server;
		slots = new NetServerChild[server.slots];

		this.listenThread = new ConnectionListenThread(this, port, address);
		this.listenThread.setDaemon(true);
		this.listenThread.start();
	}

	public void stopConnection() {
		listenThread.isListening = false;
	}

	protected void processConnections() {
		int index = 0;

		for (NetServerChild child : slots) {
			if (child == null) {
				continue;
			}
			if (!child.isConnected()) {
				slots[index] = null;
			}

			try {
				if (child.hasPackets()) {
					Packet packet = child.getNextPacket();

					packet.handlePacket(child);
				}
			} catch (Exception ex) {
				System.out.println("Failed reading packet!");

				ex.printStackTrace();
			}

			index++;
		}
	}

	public final Server getServer() {
		return server;
	}

	public final int getNextAvailableSlot() {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null)
				return i;
		}
		return -1;
	}
}
