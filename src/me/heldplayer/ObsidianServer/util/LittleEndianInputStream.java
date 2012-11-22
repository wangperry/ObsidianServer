
package me.heldplayer.ObsidianServer.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianInputStream extends InputStream implements DataInput {

    private DataInputStream dataInputSteam;
    private InputStream inputSteam;
    private byte buffer[];

    public LittleEndianInputStream(InputStream in) {
        this.inputSteam = in;
        this.dataInputSteam = new DataInputStream(in);
        buffer = new byte[8];
    }

    @Override
    public int available() throws IOException {
        return dataInputSteam.available();
    }

    @Override
    public final short readShort() throws IOException {
        dataInputSteam.readFully(buffer, 0, 2);
        return (short) ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        dataInputSteam.readFully(buffer, 0, 2);
        return ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final char readChar() throws IOException {
        dataInputSteam.readFully(buffer, 0, 2);
        return (char) ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final int readInt() throws IOException {
        dataInputSteam.readFully(buffer, 0, 4);
        return (buffer[3]) << 24 | (buffer[2] & 0xff) << 16 | (buffer[1] & 0xff) << 8 | (buffer[0] & 0xff);
    }

    @Override
    public final long readLong() throws IOException {
        dataInputSteam.readFully(buffer, 0, 8);
        return (long) (buffer[7]) << 56 | (long) (buffer[6] & 0xff) << 48 | (long) (buffer[5] & 0xff) << 40 | (long) (buffer[4] & 0xff) << 32 | (long) (buffer[3] & 0xff) << 24 | (long) (buffer[2] & 0xff) << 16 | (long) (buffer[1] & 0xff) << 8 | (long) (buffer[0] & 0xff);
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final int read(byte b[], int off, int len) throws IOException {
        return inputSteam.read(b, off, len);
    }

    @Override
    public final void readFully(byte b[]) throws IOException {
        dataInputSteam.readFully(b, 0, b.length);
    }

    @Override
    public final void readFully(byte b[], int off, int len) throws IOException {
        dataInputSteam.readFully(b, off, len);
    }

    @Override
    public final int skipBytes(int n) throws IOException {
        return dataInputSteam.skipBytes(n);
    }

    @Override
    public final boolean readBoolean() throws IOException {
        return dataInputSteam.readBoolean();
    }

    @Override
    public final byte readByte() throws IOException {
        return dataInputSteam.readByte();
    }

    @Override
    public int read() throws IOException {
        return inputSteam.read();
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        return dataInputSteam.readUnsignedByte();
    }

    @Override
    @Deprecated
    public final String readLine() throws IOException {
        return dataInputSteam.readLine();
    }

    @Override
    public final String readUTF() throws IOException {
        return dataInputSteam.readUTF();
    }

    @Override
    public final void close() throws IOException {
        dataInputSteam.close();
    }
}
