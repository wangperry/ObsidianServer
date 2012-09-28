package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet02DisconnectError extends Packet {
	private String message = "";

	public Packet02DisconnectError() {
		id = 2;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		throw new UnsupportedOperationException("Server cannot recieve this packet");
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(message.length() + 1);

		super.writePacket(output);

		output.writeBytes(message);
	}

	@Override
	public void handlePacket(NetServerChild child) {
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
