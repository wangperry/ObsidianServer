
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet02DisconnectError extends Packet {
    private String message = "";

    public Packet02DisconnectError() {
        this.id = 2;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(this.message.length() + 1);

        super.writePacket(output);

        output.writeBytes(this.message);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

    public void setMessage(String message) {
        this.message = message;
    }

}
