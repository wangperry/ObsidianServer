package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet05PlayerInventory extends Packet {
	private int playerSlot = 0;
	private int inventorySlot = 0;
	private int itemStack = 0;
	private int itemPrefixId = 0;
	private short itemId = 0;

	public Packet05PlayerInventory() {
		this.id = 5;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.playerSlot = input.readUnsignedByte();
		this.inventorySlot = input.readUnsignedByte();
		this.itemStack = input.readUnsignedByte();
		this.itemPrefixId = input.readUnsignedByte();
		this.itemId = input.readShort();
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(7);

		super.writePacket(output);

		output.writeByte(this.playerSlot);
		output.writeByte(this.inventorySlot);
		output.writeByte(this.itemStack);
		output.writeByte(this.itemPrefixId);
		output.writeShort(this.itemId);
	}

	@Override
	public void handlePacket(NetServerChild child) {
		//if (child.playerState != PlayerState.Initializing)
		//throw new UnsupportedOperationException("Client cannot send this packet at this time");

		//System.out.println("Player " + child.getSlot() + " inventory. Slot: " + inventorySlot + "; Stack: " + itemStack + "; PrefixId: " + itemPrefixId + "; Id: " + itemId);

		child.broadcastPacket(this);
	}

}
