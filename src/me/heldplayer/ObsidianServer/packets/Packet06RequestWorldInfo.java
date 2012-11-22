package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.Server;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet06RequestWorldInfo extends Packet {
	public Packet06RequestWorldInfo() {
		id = 6;
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
		if (child.playerState != PlayerState.Initializing)
			throw new UnsupportedOperationException("Client cannot send this packet at this time");

		Packet07WorldInfo packet = new Packet07WorldInfo();
		packet.setWorld(Server.getInstance().world);

		child.addToQeue(packet);

		child.playerState = PlayerState.Initialized;
	}

}
