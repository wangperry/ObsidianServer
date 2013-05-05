
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet0BRecalculateAreaUV extends Packet {
    public int startX = 0;
    public int startY = 0;
    public int endX = 0;
    public int endY = 0;

    public Packet0BRecalculateAreaUV() {
        this.id = 11;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(17);

        super.writePacket(output);

        output.writeInt(this.startX);
        output.writeInt(this.startY);
        output.writeInt(this.endX);
        output.writeInt(this.endY);

    }

    @Override
    public void handlePacket(NetServerChild child) {}

}
