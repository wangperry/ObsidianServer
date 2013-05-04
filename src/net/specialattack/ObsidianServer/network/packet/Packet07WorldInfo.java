
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.world.World;


public class Packet07WorldInfo extends Packet {
    private World world;

    public Packet07WorldInfo() {
        id = 7;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        throw new UnsupportedOperationException("Server cannot recieve this packet");
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        setLength(37 + world.name.length());

        super.writePacket(output);

        output.writeInt(world.gameTime);
        output.writeByte(world.dayTime);
        output.writeByte(world.moonPhase);
        output.writeByte(world.bloodMoon);
        output.writeInt(world.mapWidth);
        output.writeInt(world.mapHeight);
        output.writeInt(world.spawnTileX);
        output.writeInt(world.spawnTileY);
        output.writeInt((int) world.groundLevelY);
        output.writeInt((int) world.rockLayerY);
        output.writeInt(world.worldId);

        byte flags = 0;
        flags &= (world.shadowOrbSmashed ? 0x1 : 0);
        flags &= (world.killedBoss1 ? 0x2 : 0);
        flags &= (world.killedBoss2 ? 0x4 : 0);
        flags &= (world.killedBoss3 ? 0x8 : 0);
        flags &= (world.hardMode ? 0xF : 0);
        flags &= (world.killedBoss4 ? 0x10 : 0);

        output.writeByte(flags);

        output.writeBytes(world.name);
    }

    @Override
    public void handlePacket(NetServerChild child) {}

    public void setWorld(World world) {
        this.world = world;
    }

}
