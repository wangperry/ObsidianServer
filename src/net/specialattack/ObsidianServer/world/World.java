
package net.specialattack.ObsidianServer.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.NPC;
import net.specialattack.ObsidianServer.entities.Player;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class World {
    private Player[] playerList;
    private NPC[] NPCList;
    private final File dataFolder;
    private final File dataFile;
    private final File inventoriesFile;
    private final File terrainFile;
    public final WorldTerrainData terrainData;

    //public static float leftWorld = 0.0F;
    //public static float rightWorld = 134400.0F;
    //public static float topWorld = 0.0F;
    //public static float bottomWorld = 38400.0F;
    //public static int maxTilesX = (int) rightWorld / 16 + 1;
    //public static int maxTilesY = (int) bottomWorld / 16 + 1;

    public int gameTime = 0;
    public int dayTime = 1; // Byte
    public int moonPhase = 0; // Byte
    public int bloodMoon = 1; // Byte
    public int mapWidth = 4200; // 1 = 4200; 2 = 6300; 3 = 8400
    public int mapHeight = 1200;// 1 = 1200; 2 = 1800; 3 = 2400
    public int sectionsX = this.mapWidth / 200;
    public int sectionsY = this.mapHeight / 150;
    public int spawnTileX = 128;
    public int spawnTileY = 128;
    public double groundLevelY = 128;
    public double rockLayerY = 160;
    public int worldId = 0;
    public int flags = 0; // Byte
    public String name = "world";
    public int totalHallow = 0;
    public int totalCorruption = 0;

    public boolean shadowOrbSmashed = false;
    public boolean killedBoss1 = false; // The Big Eye
    public boolean killedBoss2 = false; // The Big Worm
    public boolean killedBoss3 = false; // The Big Old Man
    public boolean hardMode = false;
    public boolean killedBoss4 = false; // The Scary Man

    public World(File dataFolder) throws IOException {
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
        this.spawnTileX = this.mapWidth / 2;

        this.sectionsX = this.mapWidth / 200;
        this.sectionsY = this.mapHeight / 150;

        this.dataFolder = dataFolder.getAbsoluteFile();

        if (this.dataFolder.exists()) {
            if (!this.dataFolder.isDirectory()) {
                throw new RuntimeException("world has to be set to a folder name!");
            }
        }
        else {
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
        }
        else {
            this.loadWorldInfo();
        }
        if (!this.inventoriesFile.exists()) {
            this.inventoriesFile.createNewFile();
        }
        else {

        }
        if (!this.terrainFile.exists()) {
            this.terrainFile.createNewFile();

            this.terrainData.generateWorld(0L);
        }
        else {
            this.terrainData.loadWorld(this.terrainFile);
        }

        this.playerList = new Player[254];
        this.NPCList = new NPC[32767];
    }

    public void saveWorldInfo() {
        Server.log.log(Level.INFO, "Saving world info..");

        LittleEndianOutputStream output = null;

        try {
            output = new LittleEndianOutputStream(new FileOutputStream(this.dataFile));

            output.writeInt(this.gameTime);
            output.writeByte(this.dayTime);
            output.writeByte(this.moonPhase);
            output.writeByte(this.bloodMoon);
            output.writeInt(this.mapWidth);
            output.writeInt(this.mapHeight);
            output.writeInt(this.spawnTileX);
            output.writeInt(this.spawnTileY);
            output.writeDouble(this.groundLevelY);
            output.writeDouble(this.rockLayerY);
            output.writeInt(this.worldId);
            output.writeByte(this.name.length());
            output.writeBytes(this.name);
            output.writeBoolean(this.shadowOrbSmashed);
            output.writeBoolean(this.killedBoss1);
            output.writeBoolean(this.killedBoss2);
            output.writeBoolean(this.killedBoss3);
            output.writeBoolean(this.hardMode);
            output.writeBoolean(this.killedBoss4);

            Server.log.log(Level.INFO, "World info saved!");
        }
        catch (FileNotFoundException e) {
            Server.log.log(Level.SEVERE, "Error saving world info!", e);
        }
        catch (IOException e) {
            Server.log.log(Level.SEVERE, "Error saving world info!", e);
        }
        finally {
            try {
                output.close();
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Error closing world info save stream", e);
            }
        }
    }

    private void loadWorldInfo() {
        Server.log.log(Level.INFO, "Loading world info...");

        LittleEndianInputStream input = null;

        try {
            input = new LittleEndianInputStream(new FileInputStream(this.dataFile));

            this.gameTime = input.readInt();
            this.dayTime = input.readUnsignedByte();
            this.moonPhase = input.readUnsignedByte();
            this.bloodMoon = input.readUnsignedByte();
            this.mapWidth = input.readInt();
            this.mapHeight = input.readInt();
            this.spawnTileX = input.readInt();
            this.spawnTileY = input.readInt();
            this.groundLevelY = input.readDouble();
            this.rockLayerY = input.readDouble();
            this.worldId = input.readInt();
            int nameSize = input.readUnsignedByte();
            byte[] name = new byte[nameSize];
            input.read(name);
            this.name = new String(name);
            this.shadowOrbSmashed = input.readBoolean();
            this.killedBoss1 = input.readBoolean();
            this.killedBoss2 = input.readBoolean();
            this.killedBoss3 = input.readBoolean();
            this.hardMode = input.readBoolean();
            this.killedBoss4 = input.readBoolean();

            Server.log.log(Level.INFO, "World info loaded!");
        }
        catch (FileNotFoundException e) {
            Server.log.log(Level.SEVERE, "Error loading world info!", e);
        }
        catch (IOException e) {
            Server.log.log(Level.SEVERE, "Error loading world info!", e);
        }
        finally {
            try {
                input.close();
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Error closing world info load stream", e);
            }
        }
    }

    public Player getPlayer(byte slot) {
        if (slot <= 0 || slot >= 255) {
            return null;
        }
        return this.playerList[slot];
    }

    public NPC getNPC(short slot) {
        if (slot <= 0 || slot >= 32767) {
            return null;
        }
        return this.NPCList[slot];
    }

    public void setPlayer(int slot, Player player) {
        if (!(slot <= 0 || slot >= this.playerList.length)) {
            this.playerList[slot] = player;
        }
    }

    public void setNPC(short slot, NPC npc) {
        if (!(slot <= 0 || slot >= this.NPCList.length)) {
            this.NPCList[slot] = npc;
        }
    }

    public static int tile(int x, int y) {
        World world = Server.getInstance().world;

        return (x % world.mapWidth);
    }
}
