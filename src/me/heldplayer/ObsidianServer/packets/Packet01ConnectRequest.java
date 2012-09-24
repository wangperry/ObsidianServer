package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet01ConnectRequest extends Packet {
	private String version = "";

	public Packet01ConnectRequest() {
		id = (byte) 1;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		byte[] buffer = new byte[length - 1];

		input.read(buffer);

		version = new String(buffer);
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		throw new UnsupportedOperationException("Server cannot send this packet");
	}

	@Override
	public void handlePacket(NetServerChild child) {
		if (!version.equalsIgnoreCase("Terraria39")) {
			child.disconnect(version);
		} else {

		}
	}

}
