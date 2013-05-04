
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;


public class Packet03ContinueConnecting extends Packet {
    private int slot = 0;

    public Packet03ContinueConnecting() {
        id = 3;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        setLength(2);

        super.writePacket(output);

        output.writeByte(slot);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

    public void setSlot(int slot) {
        this.slot = slot;
    }

}
