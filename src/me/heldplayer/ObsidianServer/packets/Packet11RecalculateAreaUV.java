package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet11RecalculateAreaUV extends Packet {
	public int startX = 0;
	public int startY = 0;
	public int endX = 0;
	public int endY = 0;

	public Packet11RecalculateAreaUV() {
		id = 11;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		throw new UnsupportedOperationException("Server cannot recieve this packet");
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(17);

		super.writePacket(output);

		output.writeInt(startX);
		output.writeInt(startY);
		output.writeInt(endX);
		output.writeInt(endY);

	}

	@Override
	public void handlePacket(NetServerChild child) {
	}

}
