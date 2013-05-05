
package net.specialattack.ObsidianServer.util.io;

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
        this.buffer = new byte[8];
    }

    @Override
    public int available() throws IOException {
        return this.dataInputSteam.available();
    }

    @Override
    public final short readShort() throws IOException {
        this.dataInputSteam.readFully(this.buffer, 0, 2);
        return (short) ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        this.dataInputSteam.readFully(this.buffer, 0, 2);
        return ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    @Override
    public final char readChar() throws IOException {
        this.dataInputSteam.readFully(this.buffer, 0, 2);
        return (char) ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    @Override
    public final int readInt() throws IOException {
        this.dataInputSteam.readFully(this.buffer, 0, 4);
        return (this.buffer[3]) << 24 | (this.buffer[2] & 0xff) << 16 | (this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff);
    }

    @Override
    public final long readLong() throws IOException {
        this.dataInputSteam.readFully(this.buffer, 0, 8);
        return (long) (this.buffer[7]) << 56 | (long) (this.buffer[6] & 0xff) << 48 | (long) (this.buffer[5] & 0xff) << 40 | (long) (this.buffer[4] & 0xff) << 32 | (long) (this.buffer[3] & 0xff) << 24 | (long) (this.buffer[2] & 0xff) << 16 | (long) (this.buffer[1] & 0xff) << 8 | (long) (this.buffer[0] & 0xff);
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public final int read(byte b[], int off, int len) throws IOException {
        return this.inputSteam.read(b, off, len);
    }

    @Override
    public final void readFully(byte b[]) throws IOException {
        this.dataInputSteam.readFully(b, 0, b.length);
    }

    @Override
    public final void readFully(byte b[], int off, int len) throws IOException {
        this.dataInputSteam.readFully(b, off, len);
    }

    @Override
    public final int skipBytes(int n) throws IOException {
        return this.dataInputSteam.skipBytes(n);
    }

    @Override
    public final boolean readBoolean() throws IOException {
        return this.dataInputSteam.readBoolean();
    }

    @Override
    public final byte readByte() throws IOException {
        return this.dataInputSteam.readByte();
    }

    @Override
    public int read() throws IOException {
        return this.inputSteam.read();
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        return this.dataInputSteam.readUnsignedByte();
    }

    @Override
    @Deprecated
    public final String readLine() throws IOException {
        return this.dataInputSteam.readLine();
    }

    @Override
    public final String readUTF() throws IOException {
        return this.dataInputSteam.readUTF();
    }

    @Override
    public final void close() throws IOException {
        this.dataInputSteam.close();
    }
}
