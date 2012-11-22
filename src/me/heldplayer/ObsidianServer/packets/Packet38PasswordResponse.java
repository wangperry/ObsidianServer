
package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.Server;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;
import me.heldplayer.ObsidianServer.util.PlayerState;

public class Packet38PasswordResponse extends Packet {
    private String password = "";

    public Packet38PasswordResponse() {
        id = 38;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        byte[] buffer = new byte[length - 1];

        input.read(buffer);

        password = new String(buffer);
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        throw new UnsupportedOperationException("Server cannot send this packet");
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Connected)
            throw new UnsupportedOperationException("Client cannot send this packet at this time");

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
