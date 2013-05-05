
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet26PasswordResponse extends Packet {
    private String password = "";

    public Packet26PasswordResponse() {
        this.id = 38;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        byte[] buffer = new byte[this.length - 1];

        input.read(buffer);

        this.password = new String(buffer);
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        throw new UnsupportedOperationException("Server cannot send this packet");
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Connected) {
            throw new UnsupportedOperationException("Client cannot send this packet at this time");
        }

        String password = Server.getInstance().password;

        if (!password.equalsIgnoreCase(this.password)) {
            child.disconnect("Invalid Password!");
        }
        else {
            Packet03ContinueConnecting packet = new Packet03ContinueConnecting();

            packet.setSlot(child.getSlot());

            child.addToQeue(packet);

            child.playerState = PlayerState.Initializing;
        }
    }

}
