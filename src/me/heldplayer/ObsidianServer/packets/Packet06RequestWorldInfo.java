package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet06RequestWorldInfo extends Packet {
	public Packet06RequestWorldInfo() {
		id = (byte) 37;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		throw new UnsupportedOperationException("Server cannot send this packet");
	}

	@Override
	public void handlePacket(NetServerChild child) {
		
	}

}
