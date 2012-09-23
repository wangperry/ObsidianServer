package me.heldplayer.ObsidianServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListenThread extends Thread {
	private final NetServerManager manager;
	public boolean isListening = false;
	private final ServerSocket serverSocket;

	public ConnectionListenThread(NetServerManager manager, int port, InetAddress addr) throws IOException {
		this.manager = manager;

		this.serverSocket = new ServerSocket(port, 0, addr);

		isListening = true;
	}

	@Override
	public void run() {
		while (this.isListening) {
			try {
				Socket socket = this.serverSocket.accept();

				NetServerChild child = null;
				try {
					child = new NetServerChild(socket);
				} catch (IOException e) {
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());

					output.writeInt(0);
				}

				synchronized (manager.children) {
					manager.children.add(child);
				}
			} catch (Exception e) {
			}
		}
	}
}
