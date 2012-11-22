
package me.heldplayer.ObsidianServer.util;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LittleEndianOutputStream extends OutputStream implements DataOutput {

    private DataOutputStream dataOutputSteam;
    private OutputStream outputStream;

    public LittleEndianOutputStream(OutputStream out) {
        this.outputStream = out;
        this.dataOutputSteam = new DataOutputStream(out);
    }

    public final void close() throws IOException {
        dataOutputSteam.close();
    }

    @Override
    public final void writeBoolean(boolean arg) throws IOException {
        dataOutputSteam.writeBoolean(arg);
    }

    @Override
    public final void writeByte(int arg) throws IOException {
        dataOutputSteam.writeByte(arg & 0xff);
    }

    @Override
    public final void writeBytes(String arg) throws IOException {
        dataOutputSteam.writeBytes(arg);
    }

    public final void writeBytes(byte[] arg) throws IOException {
        for (byte theByte : arg)
            dataOutputSteam.writeByte(theByte);
    }

    @Override
    public final void writeChar(int arg) throws IOException {
        byte[] buffer = new byte[2];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeChars(String arg) throws IOException {
        char[] chars = arg.toCharArray();

        for (char theChar : chars)
            writeChar(theChar);
    }

    @Override
    public final void writeDouble(double arg) throws IOException {
        writeLong(Double.doubleToLongBits(arg));
    }

    @Override
    public final void writeFloat(float arg) throws IOException {
        writeInt(Float.floatToIntBits(arg));
    }

    @Override
    public final void writeInt(int arg) throws IOException {
        byte[] buffer = new byte[4];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeLong(long arg) throws IOException {
        byte[] buffer = new byte[8];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeShort(int arg) throws IOException {
        byte[] buffer = new byte[2];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeUTF(String arg) throws IOException {
        dataOutputSteam.writeUTF(arg);
    }

    @Override
    public final void write(int b) throws IOException {
        outputStream.write(b);
    }
}
