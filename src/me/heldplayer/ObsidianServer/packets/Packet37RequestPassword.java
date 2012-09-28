package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet37RequestPassword extends Packet {
	public Packet37RequestPassword() {
		id = 37;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		throw new UnsupportedOperationException("Server cannot recieve this packet");
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(1);

		super.writePacket(output);
	}

	@Override
	public void handlePacket(NetServerChild child) {
	}

}
