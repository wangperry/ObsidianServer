
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet25RequestPassword extends Packet {
    public Packet25RequestPassword() {
        this.id = 37;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(1);

        super.writePacket(output);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

}
