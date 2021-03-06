
package net.specialattack.ObsidianServer.network.packet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public abstract class Packet {
    protected int id;
    protected int length = 0;

    // Byte == writeByte() | byte
    // Int32 == writeInt() | int
    // Int16 == writeShort() | short
    // Single == writeFloat() | float
    // Color == writeBytes() | byte[3]
    // String == writeString() | String

    public abstract void readPacket(LittleEndianInputStream input) throws IOException;

    public void writePacket(LittleEndianOutputStream output) throws IOException {
        output.writeInt(this.length);
        output.writeByte(this.id);
    }

    public abstract void handlePacket(NetServerChild child);

    private static final HashMap<Integer, Class<? extends Packet>> packetMapping = new HashMap<Integer, Class<? extends Packet>>();

    public final void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return this.id;
    }

    public static Packet getPacket(int id) {
        if (packetMapping.containsKey(id)) {
            Class<? extends Packet> clazz = packetMapping.get(id);

            try {
                Constructor<? extends Packet> constructor = clazz.getConstructor();

                Packet packet = constructor.newInstance();

                return packet;
            }
            catch (Exception e) {
                Server.log.log(Level.WARNING, "Couldn't make packet", e);

                return null;
            }
        }
        else {
            return null;
        }
    }

    static {
        packetMapping.put(1, Packet01ConnectRequest.class);
        packetMapping.put(2, Packet02DisconnectError.class);
        packetMapping.put(3, Packet03ContinueConnecting.class);
        packetMapping.put(4, Packet04PlayerAppearance.class);
        packetMapping.put(5, Packet05PlayerInventory.class);
        packetMapping.put(6, Packet06RequestWorldInfo.class);
        packetMapping.put(7, Packet07WorldInfo.class);
        packetMapping.put(8, Packet08RequestTileData.class);
        packetMapping.put(9, Packet09StatusbarText.class);
        packetMapping.put(10, Packet0ATileRowData.class);
        packetMapping.put(11, Packet0BRecalculateAreaUV.class);
        packetMapping.put(12, Packet0CSpawnPlayer.class);
        packetMapping.put(16, Packet10PlayerHealth.class);
        packetMapping.put(37, Packet25RequestPassword.class);
        packetMapping.put(38, Packet26PasswordResponse.class);
        packetMapping.put(42, Packet2APlayerMana.class);
        packetMapping.put(49, Packet31PlayerFirstSpawn.class);
        packetMapping.put(50, Packet32PlayerBuffs.class);
    }
}
