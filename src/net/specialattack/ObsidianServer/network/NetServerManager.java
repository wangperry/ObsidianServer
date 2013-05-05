
package net.specialattack.ObsidianServer.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.network.packet.Packet;
import net.specialattack.ObsidianServer.util.thread.ConnectionListenThread;

public class NetServerManager {
    private Server server;
    private ConnectionListenThread listenThread;
    protected NetServerChild[] slots;

    public NetServerManager(Server server, int port, InetAddress address) throws IOException {
        this.server = server;
        this.slots = new NetServerChild[this.server.slots];

        this.listenThread = new ConnectionListenThread(this, port, address);
        this.listenThread.setDaemon(true);
        this.listenThread.start();
    }

    public void stopConnection() {
        this.listenThread.isListening = false;
        int index = 0;

        for (NetServerChild child : this.slots) {
            if (child == null) {
                continue;
            }
            if (!child.isConnected()) {
                this.slots[index] = null;
                continue;
            }

            child.disconnect("Server shutting down");

            index++;
        }
    }

    public void processConnections() {
        int index = 0;

        for (NetServerChild child : this.slots) {
            if (child == null) {
                continue;
            }
            if (!child.isConnected()) {
                this.slots[index] = null;
                continue;
            }

            try {
                while (child.hasPackets()) {
                    Packet packet = child.getNextPacket();

                    packet.handlePacket(child);
                }
            }
            catch (Exception e) {
                Server.log.log(Level.WARNING, "Failed reading packet!", e);
            }

            index++;
        }
    }

    public final int getNextAvailableSlot() {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.slots[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void broadcastPacket(Packet packet, NetServerChild origin) {
        int index = 0;

        for (NetServerChild child : this.slots) {
            if (child == null) {
                continue;
            }
            if (!child.isConnected()) {
                this.slots[index] = null;
                continue;
            }
            if (child == origin) {
                continue;
            }

            child.addToQeue(packet);

            index++;
        }
    }
}
