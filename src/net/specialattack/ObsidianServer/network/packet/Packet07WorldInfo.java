
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.world.World;

public class Packet07WorldInfo extends Packet {
    private World world;

    public Packet07WorldInfo() {
        this.id = 7;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        this.setLength(37 + this.world.name.length());

        super.writePacket(output);

        output.writeInt(this.world.gameTime);
        output.writeByte(this.world.dayTime);
        output.writeByte(this.world.moonPhase);
        output.writeByte(this.world.bloodMoon);
        output.writeInt(this.world.mapWidth);
        output.writeInt(this.world.mapHeight);
        output.writeInt(this.world.spawnTileX);
        output.writeInt(this.world.spawnTileY);
        output.writeInt((int) this.world.groundLevelY);
        output.writeInt((int) this.world.rockLayerY);
        output.writeInt(this.world.worldId);

        byte flags = 0;
        flags &= (this.world.shadowOrbSmashed ? 0x1 : 0);
        flags &= (this.world.killedBoss1 ? 0x2 : 0);
        flags &= (this.world.killedBoss2 ? 0x4 : 0);
        flags &= (this.world.killedBoss3 ? 0x8 : 0);
        flags &= (this.world.hardMode ? 0xF : 0);
        flags &= (this.world.killedBoss4 ? 0x10 : 0);

        output.writeByte(flags);

        output.writeBytes(this.world.name);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

    public void setWorld(World world) {
        this.world = world;
    }

}
