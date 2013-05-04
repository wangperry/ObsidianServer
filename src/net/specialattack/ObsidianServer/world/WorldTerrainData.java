
package net.specialattack.ObsidianServer.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.network.packet.Packet10TileRowData;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.util.thread.ThreadStatusAnnouncer;


public class WorldTerrainData {
    public final World world;
    //public final Tile[][] tiles;
    public final boolean[][] active;
    public final short[][] type; // Byte
    public final short wall[][]; // Byte
    public final short wallFrameX[][]; // Byte
    public final short wallFrameY[][]; // Byte
    public final short wallFrameNumber[][]; // Byte
    public final boolean wire[][];
    public final short liquid[][]; // Byte
    public final boolean checkingLiquid[][];
    public final boolean skipLiquid[][];
    public final boolean lava[][];
    public final short frameNumber[][]; // Byte
    public final short frameX[][];
    public final short frameY[][];

    public WorldTerrainData(World world) {
        this.world = world;

        //ThreadStatusAnnouncer announcer = Server.announcer;

        //tiles = new Tile[world.mapHeight][world.mapWidth];

        //for (int y = 0; y < world.mapHeight; y++) {
        //double progressY = (double) y / (double) world.mapHeight;

        //for (int x = 0; x < world.mapWidth; x++) {
        //announcer.setMessage("Loading Tiles: " + (int) (progressY * 100.0D + 1.0D) + "%");
        //tiles[y][x] = new Tile();
        //}

        //}

        //announcer.isRunning = false;

        active = new boolean[world.mapHeight][world.mapWidth];
        type = new short[world.mapHeight][world.mapWidth];
        wall = new short[world.mapHeight][world.mapWidth];
        wallFrameX = new short[world.mapHeight][world.mapWidth];
        wallFrameY = new short[world.mapHeight][world.mapWidth];
        wallFrameNumber = new short[world.mapHeight][world.mapWidth];
        wire = new boolean[world.mapHeight][world.mapWidth];
        liquid = new short[world.mapHeight][world.mapWidth];
        checkingLiquid = new boolean[world.mapHeight][world.mapWidth];
        skipLiquid = new boolean[world.mapHeight][world.mapWidth];
        lava = new boolean[world.mapHeight][world.mapWidth];
        frameNumber = new short[world.mapHeight][world.mapWidth];
        frameX = new short[world.mapHeight][world.mapWidth];
        frameY = new short[world.mapHeight][world.mapWidth];

        calculateCorruptionVsHallow();
    }

    public void loadWorld(File file) {
        Server.log.log(Level.INFO, "Loading terrain data...");

        LittleEndianInputStream input = null;

        try {
            input = new LittleEndianInputStream(new FileInputStream(file));

            Server.log.log(Level.INFO, "World terrain data loaded!");
        }
        catch (FileNotFoundException e) {
            Server.log.log(Level.SEVERE, "Error loading world terrain data!", e);
        }
        finally {
            try {
                input.close();
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Error closing world terrain data load stream", e);
            }
        }
    }

    public void saveWorld(File file) {
        Server.log.log(Level.INFO, "Saving world terrain data..");

        LittleEndianOutputStream output = null;

        try {
            output = new LittleEndianOutputStream(new FileOutputStream(file));

            for (int y = 0; y < world.mapHeight; y++) {
                for (int x = 0; x < world.mapWidth; x++) {

                }
            }

            Server.log.log(Level.INFO, "World terrain data saved!");
        }
        catch (FileNotFoundException e) {
            Server.log.log(Level.SEVERE, "Error saving world terrain data!", e);
        }
        finally {
            try {
                output.close();
            }
            catch (IOException e) {
                Server.log.log(Level.SEVERE, "Error closing world terrain data save stream", e);
            }
        }
    }

    public void generateWorld(long seed) {
        Random rand = new Random(seed <= 0 ? System.currentTimeMillis() : seed);
        world.worldId = rand.nextInt(Integer.MAX_VALUE);

        ThreadStatusAnnouncer announcer = Server.announcer;

        int num1 = 0;
        int num2 = 0;
        double num3 = (double) world.mapHeight * 0.3D * ((double) rand(rand, 90, 110) * 0.005D);
        double num4 = (num3 + (double) world.mapHeight) * 0.3D * ((double) rand(rand, 90, 110) * 0.005D);
        double num5 = num3;
        double num6 = num3;
        double num7 = num4;
        double num8 = num4;
        int num9 = rand.nextBoolean() ? 1 : -1;
        for (int index1 = 0; index1 < world.mapWidth; ++index1) {
            float num10 = (float) index1 / (float) world.mapWidth;

            announcer.setMessage("Generating world terrain: " + (int) ((double) num10 * 100.0D + 1.0D) + "%");
            if (num3 < num5)
                num5 = num3;
            if (num3 > num6)
                num6 = num3;
            if (num4 < num7)
                num7 = num4;
            if (num4 > num8)
                num8 = num4;
            if (num2 <= 0) {
                num1 = rand(rand, 0, 5);
                num2 = rand(rand, 5, 40);
                if (num1 == 0)
                    num2 *= (int) ((double) rand(rand, 5, 30) * 0.2D);
            }
            --num2;
            if (num1 == 0) {
                while (rand(rand, 0, 7) == 0)
                    num3 += (double) rand(rand, -1, 2);
            }
            else if (num1 == 1) {
                while (rand(rand, 0, 4) == 0)
                    --num3;
                while (rand(rand, 0, 10) == 0)
                    ++num3;
            }
            else if (num1 == 2) {
                while (rand(rand, 0, 4) == 0)
                    ++num3;
                while (rand(rand, 0, 10) == 0)
                    --num3;
            }
            else if (num1 == 3) {
                while (rand(rand, 0, 2) == 0)
                    --num3;
                while (rand(rand, 0, 6) == 0)
                    ++num3;
            }
            else if (num1 == 4) {
                while (rand(rand, 0, 2) == 0)
                    ++num3;
                while (rand(rand, 0, 5) == 0)
                    --num3;
            }
            if (num3 < (double) world.mapHeight * 0.17D) {
                num3 = (double) world.mapHeight * 0.17D;
                num2 = 0;
            }
            else if (num3 > (double) world.mapHeight * 0.3D) {
                num3 = (double) world.mapHeight * 0.3D;
                num2 = 0;
            }
            if ((index1 < 275 || index1 > world.mapWidth - 275) && num3 > (double) world.mapHeight * 0.25D) {
                num3 = (double) world.mapHeight * 0.25D;
                num2 = 1;
            }
            while (rand(rand, 0, 3) == 0)
                num4 += (double) rand(rand, -2, 3);
            if (num4 < num3 + (double) world.mapHeight * 0.05D)
                ++num4;
            if (num4 > num3 + (double) world.mapHeight * 0.35D)
                ++num4;
            for (int index2 = 0; index2 < num3; ++index2) {
                active[index2][index1] = false;
                frameX[index2][index1] = (short) -1;
                frameY[index2][index1] = (short) -1;
            }
            for (int index2 = (int) num3; index2 < world.mapHeight; ++index2) {
                if ((double) index2 < num4) {
                    active[index2][index1] = true;
                    type[index2][index1] = 0;
                    frameX[index2][index1] = (short) -1;
                    frameY[index2][index1] = (short) -1;
                }
                else {
                    active[index2][index1] = true;
                    type[index2][index1] = 1;
                    frameX[index2][index1] = (short) -1;
                    frameY[index2][index1] = (short) -1;
                }
            }
        }
        world.groundLevelY = num6 + 25.0D;
        world.rockLayerY = num8;
        double num11 = (double) ((int) ((world.rockLayerY - world.groundLevelY) / 6.0D) * 6);
        world.rockLayerY = world.groundLevelY + num11;
        int waterLine = (int) (world.rockLayerY + (double) world.mapHeight) / 2;
        waterLine += rand(rand, -100, 20);
        int lavaLine = waterLine + rand(rand, 50, 80);
        int num12 = 0;
        for (int index1 = 0; index1 < (int) ((double) world.mapWidth * 0.0015); ++index1) {
            int[] numArray1 = new int[10];
            int[] numArray2 = new int[10];
            int index2 = rand(rand, 450, world.mapWidth - 450);
            int index3 = 0;
            for (int index4 = 0; index4 < 10; ++index4) {
                while (!active[index3][index2])
                    index3++;
                numArray1[index4] = index2;
                numArray2[index4] = index3 - rand(rand, 11, 16);
                index2 += rand(rand, 5, 11);
            }
            for (int index4 = 0; index4 < 10; ++index4) {

            }
        }
        announcer.setMessage("Adding sand...");

        announcer.isRunning = false;
    }

    public void tileRunner(int i, int j, double strength, int steps, int type, boolean addTile, float speedX, float speedY, boolean noYChange, boolean overRide) {

    }

    private int rand(Random rand, int min, int max) {
        if (min < 0 && max < 0) {
            return -rand.nextInt(-max + min) - min;
        }

        return rand.nextInt(max - min) + min;
    }

    public void calculateCorruptionVsHallow() {

    }

    public void sendTerrainSections(NetServerChild child, short sectionX, short sectionY) {
        if (sectionX < 0 || sectionX > world.sectionsX)
            return;
        if (sectionY < 0 || sectionY > world.sectionsY)
            return;

        int startX = sectionX * 200;
        int startY = sectionY * 150;

        child.knownTileSections[sectionX][sectionY] = true;

        for (int i = 0; i < 150; i++) {
            Packet10TileRowData packet = new Packet10TileRowData();

            packet.width = 200;

            packet.tileX = startX;
            packet.tileY = startY + i;

            packet.terrain = this;

            child.addToQeue(packet);
        }
    }

    /*
     * public Tile getTileAt(int posX, int posY) {
     * if (posX > world.mapWidth) {
     * return null;
     * }
     * if (posY > world.mapHeight) {
     * return null;
     * }
     * return tiles[posY][posX];
     * }
     */
}
