package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.world.World;

public class Packet07WorldInfo extends Packet {
	private World world;

	public Packet07WorldInfo() {
		id = 7;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		throw new UnsupportedOperationException("Server cannot recieve this packet");
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		setLength(36 + world.name.length());

		super.writePacket(output);
		
		System.out.println("Sending world info!");

		output.writeInt(world.gameTime);
		output.writeByte(world.dayTime);
		output.writeByte(world.moonPhase);
		output.writeByte(world.bloodMoon);
		output.writeInt(world.mapWidth);
		output.writeInt(world.mapHeight);
		output.writeInt(world.spawnTileX);
		output.writeInt(world.spawnTileY);
		output.writeInt(world.groundLevelY);
		output.writeInt(world.rockLayerY);
		output.writeInt(world.worldId);
		output.writeBytes(world.name);
	}

	@Override
	public void handlePacket(NetServerChild child) {
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
