
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.world.World;


public class Packet08RequestTileData extends Packet {
    private int spawnX = 0;
    private int spawnY = 0;

    public Packet08RequestTileData() {
        this.id = 8;
    }

    @Override
    public void readPacket(LittleEndianInputStream input) throws IOException {
        this.spawnX = input.readInt();
        this.spawnY = input.readInt();
    }

    @Override
    public void writePacket(LittleEndianOutputStream output) throws IOException {
        throw new UnsupportedOperationException("Server cannot send this packet");
    }

    @Override
    public void handlePacket(NetServerChild child) {
        if (child.playerState != PlayerState.Initialized)
            throw new UnsupportedOperationException("Client cannot send this packet at this time");

        Server.log.log(Level.INFO, "SpawnX: " + spawnX + "; SpawnY: " + spawnY);

        World world = Server.getInstance().world;

        Packet09StatusbarText packet = new Packet09StatusbarText();
        packet.messages = 3001;
        packet.text = "Receiving tile data";

        child.addToQeue(packet);

        int spawnX = this.spawnX;
        int spawnY = this.spawnY;

        if (spawnX == -1 || spawnY == -1 || spawnX < 10 || spawnX > world.mapWidth || spawnY < 10 || spawnY > world.mapHeight) {
            spawnX = world.spawnTileX;
            spawnY = world.spawnTileY;
        }

        short sectionX = (short) (spawnX / 200);
        short sectionY = (short) (spawnY / 150);

        Server.log.log(Level.INFO, "Locals, SpawnX: " + spawnX + "; SpawnY: " + spawnY);

        Server.log.log(Level.INFO, "" + "SectionX: " + sectionX + "; SectionY: " + sectionY);

        for (short x = (short) (sectionX - 2); x < sectionX + 3; x++) {
            for (short y = (short) (sectionY - 1); y < sectionY + 2; y++) {
                world.terrainData.sendTerrainSections(child, x, y);
            }
        }

        //world.terrainData.sendTerrainSections(child, sectionX, sectionY);

        Packet11RecalculateAreaUV recalculatePacket = new Packet11RecalculateAreaUV();
        recalculatePacket.startX = sectionX - 2;
        recalculatePacket.startY = sectionY - 1;
        recalculatePacket.endX = sectionX + 2;
        recalculatePacket.endY = sectionY + 1;

        child.addToQeue(recalculatePacket);

        Packet49PlayerFirstSpawn spawnPacket = new Packet49PlayerFirstSpawn();

        child.addToQeue(spawnPacket);
    }

}