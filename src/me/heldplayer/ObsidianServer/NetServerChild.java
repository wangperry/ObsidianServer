package me.heldplayer.ObsidianServer;

import java.io.IOException;
import java.net.Socket;

import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public class NetServerChild {
	private Socket socket;
	private LittleEndianInputStream input;
	private LittleEndianOutputStream output;
	
	// Byte == writeByte() | byte
	// Int32 == writeInt() | int
	// Int16 == writeShort() | short
	// Single == writeFloat() | float
	// Color == writeBytes() | byte[3]
	// String == writeString() | String

	public NetServerChild(Socket socket) throws IOException {
		this.socket = socket;

		input = new LittleEndianInputStream(this.socket.getInputStream());
		output = new LittleEndianOutputStream(this.socket.getOutputStream());
	}
}
