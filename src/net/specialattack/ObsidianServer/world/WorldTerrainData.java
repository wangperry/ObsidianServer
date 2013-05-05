
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
import net.specialattack.ObsidianServer.network.packet.Packet;
import net.specialattack.ObsidianServer.network.packet.Packet0ATileRowData;
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

        this.active = new boolean[world.mapHeight][world.mapWidth];
        this.type = new short[world.mapHeight][world.mapWidth];
        this.wall = new short[world.mapHeight][world.mapWidth];
        this.wallFrameX = new short[world.mapHeight][world.mapWidth];
        this.wallFrameY = new short[world.mapHeight][world.mapWidth];
        this.wallFrameNumber = new short[world.mapHeight][world.mapWidth];
        this.wire = new boolean[world.mapHeight][world.mapWidth];
        this.liquid = new short[world.mapHeight][world.mapWidth];
        this.checkingLiquid = new boolean[world.mapHeight][world.mapWidth];
        this.skipLiquid = new boolean[world.mapHeight][world.mapWidth];
        this.lava = new boolean[world.mapHeight][world.mapWidth];
        this.frameNumber = new short[world.mapHeight][world.mapWidth];
        this.frameX = new short[world.mapHeight][world.mapWidth];
        this.frameY = new short[world.mapHeight][world.mapWidth];

        this.calculateCorruptionVsHallow();
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

            for (int y = 0; y < this.world.mapHeight; y++) {
                for (int x = 0; x < this.world.mapWidth; x++) {

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
        this.world.worldId = rand.nextInt(Integer.MAX_VALUE);

        ThreadStatusAnnouncer announcer = Server.announcer;

        int num1 = 0;
        int num2 = 0;
        double num3 = (double) this.world.mapHeight * 0.3D * ((double) this.rand(rand, 90, 110) * 0.005D);
        double num4 = (num3 + (double) this.world.mapHeight) * 0.3D * ((double) this.rand(rand, 90, 110) * 0.005D);
        double num5 = num3;
        double num6 = num3;
        double num7 = num4;
        double num8 = num4;
        int num9 = rand.nextBoolean() ? 1 : -1;
        announcer.setMessage("Generating world terrain: 0%", false);
        for (int index1 = 0; index1 < this.world.mapWidth; ++index1) {
            float num10 = (float) index1 / (float) this.world.mapWidth;

            announcer.setMessage("Generating world terrain: " + (int) ((double) num10 * 100.0D + 1.0D) + "%", true);
            if (num3 < num5) {
                num5 = num3;
            }
            if (num3 > num6) {
                num6 = num3;
            }
            if (num4 < num7) {
                num7 = num4;
            }
            if (num4 > num8) {
                num8 = num4;
            }
            if (num2 <= 0) {
                num1 = this.rand(rand, 0, 5);
                num2 = this.rand(rand, 5, 40);
                if (num1 == 0) {
                    num2 *= (int) ((double) this.rand(rand, 5, 30) * 0.2D);
                }
            }
            --num2;
            if (num1 == 0) {
                while (this.rand(rand, 0, 7) == 0) {
                    num3 += (double) this.rand(rand, -1, 2);
                }
            }
            else if (num1 == 1) {
                while (this.rand(rand, 0, 4) == 0) {
                    --num3;
                }
                while (this.rand(rand, 0, 10) == 0) {
                    ++num3;
                }
            }
            else if (num1 == 2) {
                while (this.rand(rand, 0, 4) == 0) {
                    ++num3;
                }
                while (this.rand(rand, 0, 10) == 0) {
                    --num3;
                }
            }
            else if (num1 == 3) {
                while (this.rand(rand, 0, 2) == 0) {
                    --num3;
                }
                while (this.rand(rand, 0, 6) == 0) {
                    ++num3;
                }
            }
            else if (num1 == 4) {
                while (this.rand(rand, 0, 2) == 0) {
                    ++num3;
                }
                while (this.rand(rand, 0, 5) == 0) {
                    --num3;
                }
            }
            if (num3 < (double) this.world.mapHeight * 0.17D) {
                num3 = (double) this.world.mapHeight * 0.17D;
                num2 = 0;
            }
            else if (num3 > (double) this.world.mapHeight * 0.3D) {
                num3 = (double) this.world.mapHeight * 0.3D;
                num2 = 0;
            }
            if ((index1 < 275 || index1 > this.world.mapWidth - 275) && num3 > (double) this.world.mapHeight * 0.25D) {
                num3 = (double) this.world.mapHeight * 0.25D;
                num2 = 1;
            }
            while (this.rand(rand, 0, 3) == 0) {
                num4 += (double) this.rand(rand, -2, 3);
            }
            if (num4 < num3 + (double) this.world.mapHeight * 0.05D) {
                ++num4;
            }
            if (num4 > num3 + (double) this.world.mapHeight * 0.35D) {
                ++num4;
            }
            for (int index2 = 0; index2 < num3; ++index2) {
                this.active[index2][index1] = false;
                this.frameX[index2][index1] = (short) -1;
                this.frameY[index2][index1] = (short) -1;
            }
            for (int index2 = (int) num3; index2 < this.world.mapHeight; ++index2) {
                if ((double) index2 < num4) {
                    this.active[index2][index1] = true;
                    this.type[index2][index1] = 0;
                    this.frameX[index2][index1] = (short) -1;
                    this.frameY[index2][index1] = (short) -1;
                }
                else {
                    this.active[index2][index1] = true;
                    this.type[index2][index1] = 1;
                    this.frameX[index2][index1] = (short) -1;
                    this.frameY[index2][index1] = (short) -1;
                }
            }
        }
        this.world.groundLevelY = num6 + 25.0D;
        this.world.rockLayerY = num8;
        double num11 = (double) ((int) ((this.world.rockLayerY - this.world.groundLevelY) / 6.0D) * 6);
        this.world.rockLayerY = this.world.groundLevelY + num11;
        int waterLine = (int) (this.world.rockLayerY + (double) this.world.mapHeight) / 2;
        waterLine += this.rand(rand, -100, 20);
        int lavaLine = waterLine + this.rand(rand, 50, 80);
        int num12 = 0;
        for (int index1 = 0; index1 < (int) ((double) this.world.mapWidth * 0.0015); ++index1) {
            int[] numArray1 = new int[10];
            int[] numArray2 = new int[10];
            int index2 = this.rand(rand, 450, this.world.mapWidth - 450);
            int index3 = 0;
            for (int index4 = 0; index4 < 10; ++index4) {
                while (!this.active[index3][index2]) {
                    index3++;
                }
                numArray1[index4] = index2;
                numArray2[index4] = index3 - this.rand(rand, 11, 16);
                index2 += this.rand(rand, 5, 11);
            }
            for (int index4 = 0; index4 < 10; ++index4) {

            }
        }
        announcer.setMessage("Adding sand...", false);

        announcer.setMessage(null, true);
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

    public Packet[] sendTerrainSections(NetServerChild child, short sectionX, short sectionY) {
        if (sectionX < 0 || sectionX > this.world.sectionsX) {
            return null;
        }
        if (sectionY < 0 || sectionY > this.world.sectionsY) {
            return null;
        }

        int startX = sectionX * 200;
        int startY = sectionY * 150;

        child.knownTileSections[sectionX][sectionY] = true;

        Packet[] result = new Packet[150];

        for (int i = 0; i < 150; i++) {
            Packet0ATileRowData packet = new Packet0ATileRowData();

            packet.width = 200;

            packet.tileX = startX;
            packet.tileY = startY + i;

            packet.terrain = this;

            result[i] = packet;
        }

        return result;
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
