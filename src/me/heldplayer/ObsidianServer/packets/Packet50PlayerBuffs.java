package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet50PlayerBuffs extends Packet {
	private byte playerSlot = 0;
	private byte[] buffs = new byte[10];

	public Packet50PlayerBuffs() {
		this.id = (byte) 1;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.playerSlot = input.readByte();

		for (int i = 0; i < 10; i++) {
			buffs[i] = input.readByte();
		}
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(6);

		super.writePacket(output);

		output.writeByte(this.playerSlot);

		for (int i = 0; i < 10; i++) {
			output.writeByte(buffs[i]);
		}
	}

	@Override
	public void handlePacket(NetServerChild child) {
		if (child.playerState != PlayerState.Initializing)
			throw new UnsupportedOperationException("Client cannot send this packet at this time");

		for (int i = 0; i < 10; i++) {
			System.out.println("Buff " + i + ": " + buffs[i]);
		}

		child.broadcastPacket(this);
	}

}
