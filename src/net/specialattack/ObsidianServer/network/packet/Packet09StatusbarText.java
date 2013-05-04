
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;


public class Packet09StatusbarText extends Packet {
    public int messages = 0;
    public String text = "";

    public Packet09StatusbarText() {
        id = 9;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        setLength(5 + text.length());

        super.writePacket(output);

        output.writeInt(messages);

        output.writeBytes(text);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

}
