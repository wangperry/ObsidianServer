
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet32PlayerBuffs extends Packet {
    private int playerSlot = 0;
    private int[] buffs = new int[10];

    public Packet32PlayerBuffs() {
        this.id = 50;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        this.playerSlot = input.readUnsignedByte();

        for (int i = 0; i < 10; i++) {
            this.buffs[i] = input.readUnsignedByte();
        }
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(12);

        super.writePacket(output);

        output.writeByte(this.playerSlot);

        for (int i = 0; i < 10; i++) {
            output.writeByte(this.buffs[i]);
        }
    }

    @Override
    public void handlePacket(NetServerChild child) {
        for (int i = 0; i < 10; i++) {
            //System.out.println("Buff " + i + ": " + buffs[i]);
        }

        //child.broadcastPacket(this);
    }

}
