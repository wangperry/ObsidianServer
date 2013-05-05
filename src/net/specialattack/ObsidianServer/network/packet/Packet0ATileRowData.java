
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.Constants;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.world.WorldTerrainData;

public class Packet0ATileRowData extends Packet {
    public int tileX = 0;
    public int tileY = 0;
    public short width = 0;
    public WorldTerrainData terrain = null;

    public Packet0ATileRowData() {
        this.id = 10;
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

        for (int i = this.tileX; i < this.width + this.tileX; i++) {
            //Tile currentTile = terrain.tiles[tileY][tileX + i];

            boolean equals = true;

            if (lastX != 0) {
                if (this.terrain.type[this.tileY][i] != this.terrain.type[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.wall[this.tileY][i] != this.terrain.wall[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.wallFrameX[this.tileY][i] != this.terrain.wallFrameX[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.wallFrameY[this.tileY][i] != this.terrain.wallFrameY[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.wallFrameNumber[this.tileY][i] != this.terrain.wallFrameNumber[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.wire[this.tileY][i] != this.terrain.wire[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.liquid[this.tileY][i] != this.terrain.liquid[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.frameNumber[this.tileY][i] != this.terrain.frameNumber[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.frameX[this.tileY][i] != this.terrain.frameX[this.tileY][lastX]) {
                    equals = false;
                }
                if (this.terrain.frameY[this.tileY][i] != this.terrain.frameY[this.tileY][lastX]) {
                    equals = false;
                }
            }

            if (equals && i != this.width + this.tileX) {
                repeatCount++;
            }
            else {
                if (lastX != 0) {
                    outputStream.writeShort(repeatCount);
                    repeatCount = 0;
                }
                byte flags = 0;

                if (this.terrain.active[this.tileY][i]) {
                    flags++;
                }
                if (this.terrain.wall[this.tileY][i] > 0) {
                    flags += 4;
                }
                if (this.terrain.liquid[this.tileY][i] > 0) {
                    flags += 8;
                }
                if (this.terrain.wire[this.tileY][i]) {
                    flags += 16;
                }

                outputStream.writeByte(flags);

                if (this.terrain.active[this.tileY][i]) {
                    outputStream.writeByte(this.terrain.type[this.tileY][i]);
                    if (Constants.tileFrameImportant[this.terrain.type[this.tileY][i]]) {
                        outputStream.writeShort(this.terrain.frameX[this.tileY][i]);
                        outputStream.writeShort(this.terrain.frameY[this.tileY][i]);
                    }
                }

                if (this.terrain.wall[this.tileY][i] > 0) {
                    outputStream.writeByte(this.terrain.wall[this.tileY][i]);
                }

                if (this.terrain.liquid[this.tileY][i] > 0) {
                    outputStream.writeByte(this.terrain.liquid[this.tileY][i]);
                    outputStream.writeBoolean(this.terrain.lava[this.tileY][i]);
                }

            }
            lastX = i;
        }

        this.setLength(1 + 2 + 4 + 4 + inputStream.available());

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
