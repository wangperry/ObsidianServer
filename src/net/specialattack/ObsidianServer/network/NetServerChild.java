
package net.specialattack.ObsidianServer.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

import net.specialattack.ObsidianServer.Server;
import net.specialattack.ObsidianServer.entities.Player;
import net.specialattack.ObsidianServer.entities.PlayerState;
import net.specialattack.ObsidianServer.network.packet.Packet;
import net.specialattack.ObsidianServer.network.packet.Packet02DisconnectError;
import net.specialattack.ObsidianServer.util.io.LittleEndianInputStream;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;
import net.specialattack.ObsidianServer.util.thread.AnonymousThread;


public class NetServerChild {
    private Socket socket;
    private LittleEndianInputStream input;
    private LittleEndianOutputStream output;
    private volatile ArrayList<Packet> inQeue;
    private volatile ArrayList<Packet> outQeue;
    private boolean isConnected = true;
    private AnonymousThread inThread;
    private AnonymousThread outThread;
    private final NetServerManager manager;
    private final int slot;
    public PlayerState playerState = PlayerState.Connected;
    public Player player = null;
    public final boolean[][] knownTileSections;

    public NetServerChild(Socket socket, NetServerManager manager, int slot) throws IOException {
        this.socket = socket;
        this.inQeue = new ArrayList<Packet>();
        this.outQeue = new ArrayList<Packet>();
        this.manager = manager;
        this.slot = slot;

        this.knownTileSections = new boolean[Server.getInstance().world.sectionsX][Server.getInstance().world.sectionsY];

        this.input = new LittleEndianInputStream(this.socket.getInputStream());
        this.output = new LittleEndianOutputStream(this.socket.getOutputStream());

        this.inThread = new AnonymousThread("Network Reader Thread; slot " + slot) {

            @Override
            public void runTask() {
                try {
                    readPackets();
                }
                catch (IOException e) {
                    disconnect("Internal Server Error");
                }
            }
        };
        this.outThread = new AnonymousThread("Network Writer Thread; slot " + slot) {

            @Override
            public void runTask() {
                writePackets();
            }
        };

        this.inThread.setDaemon(true);
        this.outThread.setDaemon(true);
        this.inThread.start();
        this.outThread.start();

        this.manager.slots[this.slot] = this;
    }

    public boolean hasPackets() {
        return inQeue.size() > 0;
    }

    public Packet getNextPacket() {
        return inQeue.remove(0);
    }

    public void readPackets() throws IOException {
        while (input.available() > 0) {
            int size = input.readInt();
            int id = input.readUnsignedByte();

            Packet packet = Packet.getPacket(id);

            Server.log.log(Level.INFO, "> " + id);

            if (packet == null) {
                Server.log.log(Level.WARNING, "Unkown Packet ID: " + id + " (" + Integer.toHexString(id).toUpperCase() + ")");
                disconnect("Unknown Packet ID: " + id + " (" + Integer.toHexString(id).toUpperCase() + ")");
            }
            else {
                packet.setLength(size);

                try {
                    packet.readPacket(input);

                    inQeue.add(packet);
                }
                catch (Exception e) {
                    Server.log.log(Level.WARNING, "Internal server error", e);
                    disconnect("Internal Server Error");
                }
            }
        }
    }

    public void addToQeue(Packet packet) {
        outQeue.add(packet);
    }

    public void writePackets() {
        try {
            while (outQeue.size() > 0) {
                Packet packet = outQeue.remove(0);
                Server.log.log(Level.INFO, "< " + packet.getId());
                packet.writePacket(output);
            }
        }
        catch (IOException e) {
            if (disconnect("Internal Server Error")) {
                Server.log.log(Level.SEVERE, "Internal Server Error", e);
            }
        }
        catch (UnsupportedOperationException e) {
            disconnect("Internal Server Error");
        }

    }

    public final boolean disconnect(String message) {
        Packet02DisconnectError packet = new Packet02DisconnectError();
        packet.setMessage(message);

        inThread.stopThread();
        outThread.stopThread();

        isConnected = false;

        boolean flag = false;

        try {
            packet.writePacket(output);
            flag = true;
        }
        catch (IOException e) {
            Server.log.log(Level.INFO, "Client lost connection to server");
        }
        finally {
            try {
                input.close();
                output.close();
                socket.close();
            }
            catch (IOException e) {}
        }

        return flag;
    }

    public final boolean isConnected() {
        return isConnected;
    }

    public int getSlot() {
        return slot;
    }

    public void broadcastPacket(Packet packet) {
        this.manager.broadcastPacket(packet, this);
    }

}
