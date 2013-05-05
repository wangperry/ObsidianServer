
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet10PlayerHealth extends Packet {
    private int playerSlot = 0;
    private short health = 0;
    private short maxHealth = 0;

    public Packet10PlayerHealth() {
        this.id = 16;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        this.playerSlot = input.readUnsignedByte();
        this.health = input.readShort();
        this.maxHealth = input.readShort();
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(6);

        super.writePacket(output);

        output.writeByte(this.playerSlot);
        output.writeShort(this.health);
        output.writeShort(this.maxHealth);
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Initializing) {
            throw new UnsupportedOperationException("Client cannot send this packet at this time");
        }

        if (this.maxHealth > 400 || this.health > 400) {
            child.disconnect("Invalid health");
        }

        child.player.health = this.health;
        child.player.maxHealth = this.maxHealth;

        child.broadcastPacket(this);
    }

}
