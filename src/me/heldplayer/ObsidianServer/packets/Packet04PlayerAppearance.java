package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.Server;
import me.heldplayer.ObsidianServer.entities.Player;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet04PlayerAppearance extends Packet {
	private byte playerSlot = 0;
	private byte hairStyle = 0;
	private byte gender = 0;
	private byte[] hairColor = new byte[3];
	private byte[] skinColor = new byte[3];
	private byte[] eyeColor = new byte[3];
	private byte[] shirtColor = new byte[3];
	private byte[] undershirtColor = new byte[3];
	private byte[] pantsColor = new byte[3];
	private byte[] shoeColor = new byte[3];
	private byte difficulty = 0;
	private String playerName = "";

	public Packet04PlayerAppearance() {
		this.id = (byte) 4;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.playerSlot = input.readByte();
		this.hairStyle = input.readByte();
		this.gender = input.readByte();

		for (int i = 0; i < 3; i++)
			this.hairColor[i] = input.readByte();
		for (int i = 0; i < 3; i++)
			this.skinColor[i] = input.readByte();
		for (int i = 0; i < 3; i++)
			this.eyeColor[i] = input.readByte();
		for (int i = 0; i < 3; i++)
			this.shirtColor[i] = input.readByte();
		for (int i = 0; i < 3; i++)
			this.undershirtColor[i] = input.readByte();
		for (int i = 0; i < 3; i++)
			this.pantsColor[i] = input.readByte();
		for (int i = 0; i < 3; i++)
			this.shoeColor[i] = input.readByte();

		this.difficulty = input.readByte();

		byte[] buffer = new byte[this.length - 26];

		input.read(buffer);

		this.playerName = new String(buffer);
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(26 + this.playerName.length());

		super.writePacket(output);

		output.writeByte(this.playerSlot);
		output.writeByte(this.hairStyle);
		output.writeByte(this.gender);

		for (int i = 0; i < 3; i++)
			output.writeByte(this.hairColor[i]);
		for (int i = 0; i < 3; i++)
			output.writeByte(this.skinColor[i]);
		for (int i = 0; i < 3; i++)
			output.writeByte(this.eyeColor[i]);
		for (int i = 0; i < 3; i++)
			output.writeByte(this.shirtColor[i]);
		for (int i = 0; i < 3; i++)
			output.writeByte(this.undershirtColor[i]);
		for (int i = 0; i < 3; i++)
			output.writeByte(this.pantsColor[i]);
		for (int i = 0; i < 3; i++)
			output.writeByte(this.shoeColor[i]);

		output.writeByte(this.difficulty);

		output.writeBytes(this.playerName);
	}

	@Override
	public void handlePacket(NetServerChild child) {
		if (child.playerState != PlayerState.Initializing)
			throw new UnsupportedOperationException("Client cannot send this packet at this time");

		child.player = new Player(child);

		child.player.playerSlot = this.playerSlot;
		child.player.hairStyle = this.hairStyle;
		child.player.gender = this.gender;
		child.player.hairColor = this.hairColor;
		child.player.skinColor = this.skinColor;
		child.player.eyeColor = this.eyeColor;
		child.player.shirtColor = this.shirtColor;
		child.player.undershirtColor = this.undershirtColor;
		child.player.pantsColor = this.pantsColor;
		child.player.shoeColor = this.shoeColor;
		child.player.difficulty = this.difficulty;
		child.player.playerName = this.playerName;

		Server.getInstance().world.setPlayer(this.playerSlot, child.player);

		child.broadcastPacket(this);
	}

}
