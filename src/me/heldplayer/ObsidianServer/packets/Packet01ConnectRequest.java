
package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.Server;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

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
