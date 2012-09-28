package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class Packet50PlayerBuffs extends Packet {
	private int playerSlot = 0;
	private int[] buffs = new int[10];

	public Packet50PlayerBuffs() {
		this.id = 50;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.playerSlot = input.readUnsignedByte();

		for (int i = 0; i < 10; i++) {
			buffs[i] = input.readUnsignedByte();
		}
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(12);

		super.writePacket(output);

		output.writeByte(this.playerSlot);

		for (int i = 0; i < 10; i++) {
			output.writeByte(buffs[i]);
		}
	}

	@Override
	public void handlePacket(NetServerChild child) {
		for (int i = 0; i < 10; i++) {
			//System.out.println("Buff " + i + ": " + buffs[i]);
		}

		//child.broadcastPacket(this);
	}

}
