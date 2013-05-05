
package net.specialattack.ObsidianServer.util.thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import net.specialattack.ObsidianServer.network.NetServerChild;
import net.specialattack.ObsidianServer.network.NetServerManager;
import net.specialattack.ObsidianServer.network.packet.Packet02DisconnectError;
import net.specialattack.ObsidianServer.util.io.LittleEndianOutputStream;

public class ConnectionListenThread extends Thread {
    private final NetServerManager manager;
    public boolean isListening = false;
    private final ServerSocket serverSocket;

    public ConnectionListenThread(NetServerManager manager, int port, InetAddress addr) throws IOException {
        super("Master connection thread");

        this.manager = manager;

        this.serverSocket = new ServerSocket(port, 0, addr);

        this.isListening = true;
    }

    @Override
    public void run() {
        while (this.isListening) {
            try {
                Socket socket = this.serverSocket.accept();

                try {
                    int slot = this.manager.getNextAvailableSlot();

                    if (slot == -1) {
                        Packet02DisconnectError packet = new Packet02DisconnectError();

                        packet.setMessage("Server is full");

                        LittleEndianOutputStream output = new LittleEndianOutputStream(socket.getOutputStream());

                        packet.writePacket(output);

                        output.close();

                        socket.close();

                        continue;
                    }

                    new NetServerChild(socket, this.manager, slot);
                }
                catch (IOException e) {
                    Packet02DisconnectError packet = new Packet02DisconnectError();

                    packet.setMessage("Internal Server Exception");

                    LittleEndianOutputStream output = new LittleEndianOutputStream(socket.getOutputStream());

                    packet.writePacket(output);

                    output.close();

                    socket.close();

                    continue;
                }
            }
            catch (Exception e) {}
        }
    }
}
