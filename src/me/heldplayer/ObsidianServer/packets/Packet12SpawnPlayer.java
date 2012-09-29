package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet12SpawnPlayer extends Packet {
	private int playerSlot = 0;
	private int spawnX = 0;
	private int spawnY = 0;

	public Packet12SpawnPlayer() {
		this.id = 12;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.playerSlot = input.readUnsignedByte();
		this.spawnX = input.readInt();
		this.spawnY = input.readInt();
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(6);

		super.writePacket(output);

		output.writeByte(this.playerSlot);
		output.writeInt(this.spawnX);
		output.writeInt(this.spawnY);
	}

	@Override
	public void handlePacket(NetServerChild child) {
		//if (child.playerState != PlayerState.Initialized)
		//throw new UnsupportedOperationException("Client cannot send this packet at this time");

		child.playerState = PlayerState.Playing;

		child.broadcastPacket(this);
	}

}
