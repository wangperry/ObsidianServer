
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.Constants;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.world.WorldTerrainData;


public class Packet10TileRowData extends Packet {
    public int tileX = 0;
    public int tileY = 0;
    public short width = 0;
    public WorldTerrainData terrain = null;

    public Packet10TileRowData() {
        id = 10;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {

        PipedInputStream inputPStr = new PipedInputStream(4096);
        PipedOutputStream outputPStr = new PipedOutputStream(inputPStr);

        LittleEndianInputStream inputStream = new LittleEndianInputStream(inputPStr);
        LittleEndianOutputStream outputStream = new LittleEndianOutputStream(outputPStr);

        //Tile lastTile = null;
        int lastX = 0;
        int repeatCount = 0;

        for (int i = tileX; i < width + tileX; i++) {
            //Tile currentTile = terrain.tiles[tileY][tileX + i];

            boolean equals = true;

            if (lastX != 0) {
                if (terrain.type[tileY][i] != terrain.type[tileY][lastX])
                    equals = false;
                if (terrain.wall[tileY][i] != terrain.wall[tileY][lastX])
                    equals = false;
                if (terrain.wallFrameX[tileY][i] != terrain.wallFrameX[tileY][lastX])
                    equals = false;
                if (terrain.wallFrameY[tileY][i] != terrain.wallFrameY[tileY][lastX])
                    equals = false;
                if (terrain.wallFrameNumber[tileY][i] != terrain.wallFrameNumber[tileY][lastX])
                    equals = false;
                if (terrain.wire[tileY][i] != terrain.wire[tileY][lastX])
                    equals = false;
                if (terrain.liquid[tileY][i] != terrain.liquid[tileY][lastX])
                    equals = false;
                if (terrain.frameNumber[tileY][i] != terrain.frameNumber[tileY][lastX])
                    equals = false;
                if (terrain.frameX[tileY][i] != terrain.frameX[tileY][lastX])
                    equals = false;
                if (terrain.frameY[tileY][i] != terrain.frameY[tileY][lastX])
                    equals = false;
            }

            if (equals) {
                repeatCount++;
            }
            else {
                if (lastX != 0) {
                    outputStream.writeShort(repeatCount);
                    repeatCount = 0;
                }
                byte flags = 0;

                if (terrain.active[tileY][i])
                    flags++;
                if (terrain.wall[tileY][i] > 0)
                    flags += 4;
                if (terrain.liquid[tileY][i] > 0)
                    flags += 8;
                if (terrain.wire[tileY][i])
                    flags += 16;

                outputStream.writeByte(flags);

                if (terrain.active[tileY][i]) {
                    outputStream.writeByte(terrain.type[tileY][i]);
                    if (Constants.tileFrameImportant[terrain.type[tileY][i]]) {
                        outputStream.writeShort(terrain.frameX[tileY][i]);
                        outputStream.writeShort(terrain.frameY[tileY][i]);
                    }
                }

                if (terrain.wall[tileY][i] > 0) {
                    outputStream.writeByte(terrain.wall[tileY][i]);
                }

                if (terrain.liquid[tileY][i] > 0) {
                    outputStream.writeByte(terrain.liquid[tileY][i]);
                    outputStream.writeBoolean(terrain.lava[tileY][i]);
                }

            }
            lastX = i;
        }

        setLength(1 + 2 + 4 + 4 + inputStream.available());

        super.writePacket(output);

        while (inputStream.available() > 0) {
            output.writeByte(inputStream.readByte());
        }

        try {
            outputStream.close();
            inputStream.close();
            outputPStr.close();
            inputPStr.close();
        }
        catch (Exception ex) {}

    }

    @Override
    public void handlePacket(NetServerChild child) {}

}
