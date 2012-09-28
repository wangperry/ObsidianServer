package me.heldplayer.ObsidianServer.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import me.heldplayer.ObsidianServer.Server;
import me.heldplayer.ObsidianServer.entities.NPC;
import me.heldplayer.ObsidianServer.entities.Player;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class World {
	private Player[] playerList;
	private NPC[] NPCList;
	private final File dataFolder;
	private final File dataFile;
	private final File inventoriesFile;
	private final File terrainFile;
	public final WorldTerrainData terrainData;

	public static float leftWorld = 0.0F;
	public static float rightWorld = 134400.0F;
	public static float topWorld = 0.0F;
	public static float bottomWorld = 38400.0F;
	public static int maxTilesX = (int) rightWorld / 16 + 1;
	public static int maxTilesY = (int) bottomWorld / 16 + 1;

	public int gameTime = 0;
	public int dayTime = 1; // Byte
	public int moonPhase = 0; // Byte
	public int bloodMoon = 0; // Byte
	public int mapWidth = 4200; // 1 = 4200; 2 = 6300; 3 = 8400
	public int mapHeight = 1200;// 1 = 1200; 2 = 1800; 3 = 2400
	public int spawnTileX = 128;
	public int spawnTileY = 128;
	public int groundLevelY = 128;
	public int rockLayerY = 160;
	public int worldId = 0;
	public int flags = 0; // Byte
	public String name = "world";
	public int totalHallow = 0;
	public int totalCorruption = 0;

	public World(File dataFolder) throws IOException {
		System.out.println("Initializing world...");

		switch (Server.getInstance().worldSize) {
		case 2:
			this.mapWidth = 6300;
			this.mapHeight = 1800;
			break;
		case 3:
			this.mapWidth = 8400;
			this.mapHeight = 2400;
			break;
		default:
			this.mapWidth = 4200;
			this.mapHeight = 1200;
			break;
		}

		this.dataFolder = dataFolder.getAbsoluteFile();

		if (this.dataFolder.exists()) {
			if (!this.dataFolder.isDirectory()) {
				throw new RuntimeException("world has to be set to a folder name!");
			}
		} else {
			this.dataFolder.mkdirs();
			if (!this.dataFolder.exists()) {
				throw new RuntimeException("Failed creating world folder!");
			}
		}

		this.dataFile = new File(this.dataFolder, "world.dat");
		this.inventoriesFile = new File(this.dataFolder, "inventories.dat");
		this.terrainFile = new File(this.dataFolder, "terrain.dat");

		this.terrainData = new WorldTerrainData(this);

		if (!this.dataFile.exists()) {
			this.dataFile.createNewFile();
		} else {
			this.loadWorldInfo();
		}
		if (!this.inventoriesFile.exists()) {
			this.inventoriesFile.createNewFile();
		} else {

		}
		if (!this.terrainFile.exists()) {
			this.terrainFile.createNewFile();

			this.terrainData.generateWorld(0L);
		} else {
			this.terrainData.loadWorld(this.terrainFile);
		}

		this.playerList = new Player[254];
		this.NPCList = new NPC[32767];

		debug();

		System.out.println("Done!");
	}

	public void debug() {
		for (Field field : this.getClass().getFields()) {
			try {
				System.out.println(field.getName() + " is set to " + field.get(this).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveWorld() {
		System.out.println("Saving world info..");

		LittleEndianOutputStream output = null;

		try {
			output = new LittleEndianOutputStream(new FileOutputStream(dataFile));

			output.writeInt(this.gameTime);
			output.writeByte(this.dayTime);
			output.writeByte(this.moonPhase);
			output.writeByte(this.bloodMoon);
			output.writeInt(this.mapWidth);
			output.writeInt(this.mapHeight);
			output.writeInt(this.spawnTileX);
			output.writeInt(this.spawnTileY);
			output.writeInt(this.groundLevelY);
			output.writeInt(this.rockLayerY);
			output.writeInt(this.worldId);
			output.writeByte(this.name.length());
			output.writeBytes(this.name);

			System.out.println("World info saved!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				System.err.println("Error closing world info save stream");

				e.printStackTrace();
			}
		}
	}

	private void loadWorldInfo() {
		System.out.println("Loading world info...");

		LittleEndianInputStream input = null;

		try {
			input = new LittleEndianInputStream(new FileInputStream(dataFile));

			this.gameTime = input.readInt();
			this.dayTime = input.readUnsignedByte();
			this.moonPhase = input.readUnsignedByte();
			this.bloodMoon = input.readUnsignedByte();
			this.mapWidth = input.readInt();
			this.mapHeight = input.readInt();
			this.spawnTileX = input.readInt();
			this.spawnTileY = input.readInt();
			this.groundLevelY = input.readInt();
			this.rockLayerY = input.readInt();
			this.worldId = input.readInt();
			int nameSize = input.readUnsignedByte();
			byte[] name = new byte[nameSize];
			input.read(name);
			this.name = new String(name);

			System.out.println("World info loaded!");
		} catch (FileNotFoundException e) {
			System.err.println("Error loading world info!");

			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error loading world info!");

			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				System.err.println("Error closing world info load stream");

				e.printStackTrace();
			}
		}
	}

	public Player getPlayer(byte slot) {
		if (slot <= 0 || slot >= 255)
			return null;
		return playerList[slot];
	}

	public NPC getNPC(short slot) {
		if (slot <= 0 || slot >= 32767)
			return null;
		return NPCList[slot];
	}

	public void setPlayer(int slot, Player player) {
		if (!(slot <= 0 || slot >= playerList.length)) {
			playerList[slot] = player;
		}
	}

	public void setNPC(short slot, NPC npc) {
		if (!(slot <= 0 || slot >= NPCList.length)) {
			NPCList[slot] = npc;
		}
	}
}
