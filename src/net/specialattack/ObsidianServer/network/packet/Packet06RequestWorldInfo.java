
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet06RequestWorldInfo extends Packet {
    public Packet06RequestWorldInfo() {
        this.id = 6;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {}

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        throw new UnsupportedOperationException("Server cannot send this packet");
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Initializing) {
            throw new UnsupportedOperationException("Client cannot send this packet at this time");
        }

        Packet07WorldInfo packet = new Packet07WorldInfo();
        packet.setWorld(Server.getInstance().world);

        child.addToQeue(packet);

        child.playerState = PlayerState.Initialized;
    }

}
