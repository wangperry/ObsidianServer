
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.Player;
import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class Packet04PlayerAppearance extends Packet {
    private int playerSlot = 0;
    private int hairStyle = 0;
    private int gender = 0;
    private int[] hairColor = new int[3];
    private int[] skinColor = new int[3];
    private int[] eyeColor = new int[3];
    private int[] shirtColor = new int[3];
    private int[] undershirtColor = new int[3];
    private int[] pantsColor = new int[3];
    private int[] shoeColor = new int[3];
    private int difficulty = 0;
    private String playerName = "";

    public Packet04PlayerAppearance() {
        this.id = 4;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        this.playerSlot = input.readUnsignedByte();
        this.hairStyle = input.readUnsignedByte();
        this.gender = input.readUnsignedByte();

        for (int i = 0; i < 3; i++) {
            this.hairColor[i] = input.readUnsignedByte();
        }
        for (int i = 0; i < 3; i++) {
            this.skinColor[i] = input.readUnsignedByte();
        }
        for (int i = 0; i < 3; i++) {
            this.eyeColor[i] = input.readUnsignedByte();
        }
        for (int i = 0; i < 3; i++) {
            this.shirtColor[i] = input.readUnsignedByte();
        }
        for (int i = 0; i < 3; i++) {
            this.undershirtColor[i] = input.readUnsignedByte();
        }
        for (int i = 0; i < 3; i++) {
            this.pantsColor[i] = input.readUnsignedByte();
        }
        for (int i = 0; i < 3; i++) {
            this.shoeColor[i] = input.readUnsignedByte();
        }

        this.difficulty = input.readUnsignedByte();

        byte[] buffer = new byte[this.length - 26];

        input.read(buffer);

        this.playerName = new String(buffer);
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(26 + this.playerName.length());

        super.writePacket(output);

        output.writeByte(this.playerSlot);
        output.writeByte(this.hairStyle);
        output.writeByte(this.gender);

        for (int i = 0; i < 3; i++) {
            output.writeByte(this.hairColor[i]);
        }
        for (int i = 0; i < 3; i++) {
            output.writeByte(this.skinColor[i]);
        }
        for (int i = 0; i < 3; i++) {
            output.writeByte(this.eyeColor[i]);
        }
        for (int i = 0; i < 3; i++) {
            output.writeByte(this.shirtColor[i]);
        }
        for (int i = 0; i < 3; i++) {
            output.writeByte(this.undershirtColor[i]);
        }
        for (int i = 0; i < 3; i++) {
            output.writeByte(this.pantsColor[i]);
        }
        for (int i = 0; i < 3; i++) {
            output.writeByte(this.shoeColor[i]);
        }

        output.writeByte(this.difficulty);

        output.writeBytes(this.playerName);
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Initializing) {
            throw new UnsupportedOperationException("Client cannot send this packet at this time");
        }

        child.player = new Player(child);

        child.player.playerSlot = this.playerSlot;
        child.player.hairStyle = this.hairStyle;
        child.player.gender = this.gender;
        child.player.hairColor = this.hairColor;
        child.player.skinColor = this.skinColor;
        child.player.eyeColor = this.eyeColor;
        child.player.shirtColor = this.shirtColor;
        child.player.undershirtColor = this.undershirtColor;
        child.player.pantsColor = this.pantsColor;
        child.player.shoeColor = this.shoeColor;
        child.player.difficulty = this.difficulty;
        child.player.playerName = this.playerName;

        Server.getInstance().world.setPlayer(this.playerSlot, child.player);

        child.broadcastPacket(this);
    }

}
