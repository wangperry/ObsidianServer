
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;


public class Packet42PlayerMana extends Packet {
    private int playerSlot = 0;
    private short mana = 0;
    private short maxMana = 0;

    public Packet42PlayerMana() {
        this.id = 42;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        this.playerSlot = input.readUnsignedByte();
        this.mana = input.readShort();
        this.maxMana = input.readShort();
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        setLength(6);

        super.writePacket(output);

        output.writeByte(this.playerSlot);
        output.writeShort(this.mana);
        output.writeShort(this.maxMana);
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Initializing)
            throw new UnsupportedOperationException("Client cannot send this packet at this time");

        if (this.maxMana > 460 || this.mana > 460)
            child.disconnect("Invalid mana");

        //if (this.mana > this.maxMana)
        //child.disconnect("Invalid mana");

        child.player.mana = this.mana;
        child.player.maxHealth = this.maxMana;

        child.broadcastPacket(this);
    }

}
