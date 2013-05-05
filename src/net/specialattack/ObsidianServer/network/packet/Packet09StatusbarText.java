
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet09StatusbarText extends Packet {
    public int messages = 0;
    public String text = "";

    public Packet09StatusbarText() {
        this.id = 9;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(5 + this.text.length());

        super.writePacket(output);

        output.writeInt(this.messages);

        output.writeBytes(this.text);

        Server.log.log(Level.FINER, "Sending status bar text '" + this.text + "' with max " + this.messages);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

}
