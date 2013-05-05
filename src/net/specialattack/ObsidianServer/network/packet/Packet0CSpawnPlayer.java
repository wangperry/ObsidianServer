
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet0CSpawnPlayer extends Packet {
    private int playerSlot = 0;
    private int spawnX = 0;
    private int spawnY = 0;

    public Packet0CSpawnPlayer() {
        this.id = 12;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        this.playerSlot = input.readUnsignedByte();
        this.spawnX = input.readInt();
        this.spawnY = input.readInt();
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(6);

        super.writePacket(output);

        output.writeByte(this.playerSlot);
        output.writeInt(this.spawnX);
        output.writeInt(this.spawnY);
    }

    @Override
    public void handlePacket(NetServerChild child) {
        //if (child.playerState != PlayerState.Initialized)
        //throw new UnsupportedOperationException("Client cannot send this packet at this time");

        child.playerState = PlayerState.Playing;

        child.broadcastPacket(this);
    }

}
