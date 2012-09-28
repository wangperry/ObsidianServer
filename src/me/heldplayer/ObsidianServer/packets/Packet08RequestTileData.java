package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.Server;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;
import me.heldplayer.ObsidianServer.world.World;

public class Packet08RequestTileData extends Packet {
	private int spawnX = 0;
	private int spawnY = 0;

	public Packet08RequestTileData() {
		this.id = 8;
	}

	@Override
	public void readPacket(LittleEndianInputStream input) throws IOException {
		this.spawnX = input.readInt();
		this.spawnY = input.readInt();
	}

	@Override
	public void writePacket(LittleEndianOutputStream output) throws IOException {
		throw new UnsupportedOperationException("Server cannot send this packet");
	}

	@Override
	public void handlePacket(NetServerChild child) {
		if (child.playerState != PlayerState.Initialized)
			throw new UnsupportedOperationException("Client cannot send this packet at this time");

		World world = Server.getInstance().world;

		Packet09StatusbarText packet = new Packet09StatusbarText();
		packet.messages = 51;
		packet.text = "Sending tile data";

		child.addToQeue(packet);

		int spawnX = this.spawnX;
		int spawnY = this.spawnY;

		if (spawnX == -1 || spawnY == -1) {
			spawnX = Server.getInstance().world.spawnTileX;
			spawnY = Server.getInstance().world.spawnTileY;
		}

		for (int y = spawnY; y < spawnY + 50; y++) {
			Packet10TileRowData tileData = new Packet10TileRowData();
			tileData.tileX = spawnX - 50;
			tileData.tileY = y;

			child.addToQeue(tileData);
		}

		Packet11RecalculateAreaUV recalculatePacket = new Packet11RecalculateAreaUV();
		recalculatePacket.startX = spawnX - 50;
		recalculatePacket.startY = spawnY - 50;
		recalculatePacket.endX = spawnX + 50;
		recalculatePacket.endY = spawnY + 50;

		child.addToQeue(recalculatePacket);

		Packet49PlayerFirstSpawn spawnPacket = new Packet49PlayerFirstSpawn();

		child.addToQeue(spawnPacket);

		//}
	}

}
