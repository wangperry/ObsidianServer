package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet16PlayerHealth extends Packet {
	private byte playerSlot = 0;
	private short health = 0;
	private short maxHealth = 0;

	public Packet16PlayerHealth() {
		this.id = (byte) 1;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.playerSlot = input.readByte();
		this.health = input.readShort();
		this.maxHealth = input.readShort();
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(6);

		super.writePacket(output);

		output.writeByte(this.playerSlot);
		output.writeShort(this.health);
		output.writeShort(this.maxHealth);
	}

	@Override
	public void handlePacket(NetServerChild child) {
		if (child.playerState != PlayerState.Initializing)
			throw new UnsupportedOperationException("Client cannot send this packet at this time");

		if (this.maxHealth > 400 || this.health > 400) {
			child.disconnect("Invalid health");
		}

		child.player.health = this.health;
		child.player.maxHealth = this.maxHealth;

		child.broadcastPacket(this);
	}

}