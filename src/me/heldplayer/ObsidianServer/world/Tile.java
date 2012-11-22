package me.heldplayer.ObsidianServer.world;

import me.heldplayer.ObsidianServer.util.Constants;

public class Tile {
	public boolean active = false;
	public byte type = 0; // Byte
	public byte wall = 0; // Byte
	public byte wallFrameX = 0; // Byte
	public byte wallFrameY = 0; // Byte
	public byte wallFrameNumber = 0; // Byte
	public boolean wire = false;
	public byte liquid = 0; // Byte
	public boolean checkingLiquid = false;
	public boolean skipLiquid = false;
	public boolean lava = false;
	public byte frameNumber = 0; // Byte
	public short frameX = 0;
	public short frameY = 0;

	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Tile))
			return false;

		Tile tile = (Tile) obj;

		if (this.type != tile.type)
			return false;
		if (this.wall != tile.wall)
			return false;
		if (this.wallFrameX != tile.wallFrameX)
			return false;
		if (this.wallFrameY != tile.wallFrameY)
			return false;
		if (this.wallFrameNumber != tile.wallFrameNumber)
			return false;
		if (this.wire != tile.wire)
			return false;
		if (this.liquid != tile.liquid)
			return false;
		if (this.frameNumber != tile.frameNumber)
			return false;
		if (this.frameX != tile.frameX)
			return false;
		if (this.frameY != tile.frameY)
			return false;

		return true;
	}

	public final boolean importantTile() {
		return Constants.tileFrameImportant[type];
	}

}
