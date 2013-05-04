
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;


public class Packet01ConnectRequest extends Packet {
    private String version = "";

    public Packet01ConnectRequest() {
        this.id = 1;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        byte[] buffer = new byte[length - 1];

        input.read(buffer);

        this.version = new String(buffer);
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        throw new UnsupportedOperationException("Server cannot send this packet");
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Connected)
            throw new UnsupportedOperationException("Client cannot send this packet at this time");

        if (!this.version.equalsIgnoreCase("Terraria39")) {
            child.disconnect("You are not using the same version as this server.");
        }
        else {
            String password = Server.getInstance().password;

            if (password.equalsIgnoreCase("")) {
                Packet03ContinueConnecting packet = new Packet03ContinueConnecting();

                packet.setSlot(child.getSlot());

                child.addToQeue(packet);

                child.playerState = PlayerState.Initializing;
            }
            else {
                Packet37RequestPassword packet = new Packet37RequestPassword();

                child.addToQeue(packet);
            }
        }
    }

}
