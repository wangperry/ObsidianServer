package me.heldplayer.ObsidianServer.world;

import java.io.File;
import java.io.IOException;

import me.heldplayer.ObsidianServer.entities.NPC;
import me.heldplayer.ObsidianServer.entities.Player;

public class World {
	private Player[] playerList;
	private NPC[] NPCList;
	private final File dataFolder;
	private final File dataFile;

	public World(File dataFolder) throws IOException {
		System.out.println("Initializing world...");

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

		if (!this.dataFile.exists()) {
			this.dataFile.createNewFile();
		}

		this.playerList = new Player[254];
		this.NPCList = new NPC[32767];

		System.out.println("Done!");
	}

	public void saveWorld() {

	}

	public void loadWorld() {

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

	public void setPlayer(byte slot, Player player) {
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
