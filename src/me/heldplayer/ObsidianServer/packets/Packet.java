package me.heldplayer.ObsidianServer.packets;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import me.heldplayer.ObsidianServer.NetServerChild;
import me.heldplayer.ObsidianServer.util.LittleEndianInputStream;
import me.heldplayer.ObsidianServer.util.LittleEndianOutputStream;

public abstract class Packet {
	protected byte id;
	protected int length = 0;

	// Byte == writeByte() | byte
	// Int32 == writeInt() | int
	// Int16 == writeShort() | short
	// Single == writeFloat() | float
	// Color == writeBytes() | byte[3]
	// String == writeString() | String

	public abstract void readPacket(LittleEndianInputStream input) throws IOException;

	public void writePacket(LittleEndianOutputStream output) throws IOException {
		output.writeInt(length);
		output.writeByte(id);
	}

	public abstract void handlePacket(NetServerChild child);

	private static final HashMap<Integer, Class<? extends Packet>> packetMapping = new HashMap<Integer, Class<? extends Packet>>();

	public final void setLength(int length) {
		this.length = length;
	}

	public byte getId() {
		return id;
	}

	public static Packet getPacket(int id) {
		if (packetMapping.containsKey(id)) {
			Class<? extends Packet> clazz = packetMapping.get(id);

			try {
				Constructor<? extends Packet> constructor = clazz.getConstructor();

				Packet packet = constructor.newInstance();

				return packet;
			} catch (InstantiationException e) {
				e.printStackTrace();

				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();

				return null;
			} catch (SecurityException e) {
				e.printStackTrace();

				return null;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();

				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();

				return null;
			} catch (InvocationTargetException e) {
				e.printStackTrace();

				return null;
			}
		} else {
			// TODO: unknown packet ID!
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
		packetMapping.put(16, Packet16PlayerHealth.class);
		packetMapping.put(37, Packet37RequestPassword.class);
		packetMapping.put(38, Packet38PasswordResponse.class);
		packetMapping.put(42, Packet42PlayerMana.class);
		packetMapping.put(50, Packet50PlayerBuffs.class);
	}
}
