package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet10TileRowData extends Packet {
	public int tileX = 0;
	public int tileY = 0;

	public Packet10TileRowData() {
		id = 10;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		throw new UnsupportedOperationException("Server cannot recieve this packet");
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(1 + 2 + 4 + 4 + 1 + 2 + 1);

		super.writePacket(output);

		output.writeShort(100);

		output.writeInt(tileX);

		output.writeInt(tileY);

		output.writeByte(0);

		output.writeByte(1);

		output.writeShort(100);

	}

	@Override
	public void handlePacket(NetServerChild child) {
	}

}
