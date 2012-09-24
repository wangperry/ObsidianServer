package me.heldplayer.ObsidianServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import me.heldplayer.ObsidianServer.entities.Player;
import me.heldplayer.ObsidianServer.packets.Packet;
import me.heldplayer.ObsidianServer.packets.Packet02DisconnectError;
import me.heldplayer.ObsidianServer.util.AnonymousThread;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class NetServerChild {
	private Socket socket;
	private LittleEndianInputStream input;
	private LittleEndianOutputStream output;
	private volatile ArrayList<Packet> inQeue;
	private volatile ArrayList<Packet> outQeue;
	private boolean isConnected = true;
	private AnonymousThread inThread;
	private AnonymousThread outThread;
	private final NetServerManager manager;
	private final int slot;
	public PlayerState playerState = PlayerState.Connected;
	public Player player = null;

	public NetServerChild(Socket socket, NetServerManager manager, int slot) throws IOException {
		this.socket = socket;
		this.inQeue = new ArrayList<Packet>();
		this.outQeue = new ArrayList<Packet>();
		this.manager = manager;
		this.slot = slot;

		this.input = new LittleEndianInputStream(this.socket.getInputStream());
		this.output = new LittleEndianOutputStream(this.socket.getOutputStream());

		this.inThread = new AnonymousThread("Network Reader Thread; slot " + slot) {

			@Override
			public void runTask() {
				try {
					readPackets();
				} catch (IOException e) {
					disconnect("Internal Server Error");
				}
			}
		};
		this.outThread = new AnonymousThread("Network Writer Thread; slot " + slot) {

			@Override
			public void runTask() {
				writePackets();
			}
		};

		this.inThread.setDaemon(true);
		this.outThread.setDaemon(true);
		this.inThread.start();
		this.outThread.start();

		this.manager.slots[this.slot] = this;
	}

	public boolean hasPackets() {
		return inQeue.size() > 0;
	}

	public Packet getNextPacket() {
		return inQeue.remove(0);
	}

	public void readPackets() throws IOException {
		while (input.available() > 0) {
			int size = input.readInt();
			byte id = input.readByte();

			Packet packet = Packet.getPacket(id);

			if (packet == null) {
				System.out.println("Unkown Packet ID: " + id);
				disconnect("Unknown Packet ID");
			} else {
				packet.setLength(size);

				packet.readPacket(input);

				inQeue.add(packet);
			}
		}
	}

	public void addToQeue(Packet packet) {
		outQeue.add(packet);
	}

	public void writePackets() {
		try {

			while (outQeue.size() > 0) {
				Packet packet = outQeue.remove(0);
				packet.writePacket(output);
			}
		} catch (IOException e) {
			if (disconnect("Internal Server Error")) {
				System.err.println("Internal Server Error");
				e.printStackTrace();
			}
		} catch (UnsupportedOperationException e) {
			disconnect("Internal Server Error");
		}

	}

	public final boolean disconnect(String message) {
		Packet02DisconnectError packet = new Packet02DisconnectError();
		packet.setMessage(message);

		inThread.stopThread();
		outThread.stopThread();

		isConnected = false;

		boolean flag = false;

		try {
			packet.writePacket(output);
			flag = true;
		} catch (IOException e) {
			System.err.println("Client lost connection to server");
		} finally {
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException e) {
			}
		}

		return flag;
	}

	public final boolean isConnected() {
		return isConnected;
	}

	public int getSlot() {
		return slot;
	}

	public void broadcastPacket(Packet packet) {
		this.manager.broadcastPacket(packet);
	}

}
