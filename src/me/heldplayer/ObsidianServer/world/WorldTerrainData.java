package me.heldplayer.ObsidianServer.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;

public class WorldTerrainData {
	public final World world;
	public final int[][] tiles;
	public final int[][] walls;
	public final int[][] wires;

	public WorldTerrainData(World world) {
		this.world = world;

		tiles = new int[world.mapWidth][world.mapHeight];
		walls = new int[world.mapWidth][world.mapHeight];
		wires = new int[world.mapWidth][world.mapHeight];

		calculateCorruptionVsHallow();
	}

	public void loadWorld(File file) {
		System.out.println("Loading terrain data...");

		LittleEndianInputStream input = null;

		try {
			input = new LittleEndianInputStream(new FileInputStream(file));

			//--- Tiles
			// Num Tiles
			// Tile X + Tile Y + Tile Type + Tile U + Tile V
			// Potential repeat value

			//--- Walls
			// Num Walls
			// Wall X + Wall Y + Wall Type
			// Potential repeat value

			//--- Liquids
			// Num Liquids
			// Liquid X + Liquid Y + Liquid Amount + Liquid Type
			// Potential repeat value

			//--- Wires
			// Num Wires
			// Wire X + WireZ
			// Potential repeat value

			System.out.println("World terrain data loaded!");
		} catch (FileNotFoundException e) {
			System.err.println("Error loading world terrain data!");

			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error loading world terrain data!");

			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				System.err.println("Error closing world terrain data load stream");

				e.printStackTrace();
			}
		}
	}

	public void generateWorld(long seed) {

	}

	public void calculateCorruptionVsHallow() {

	}
}
