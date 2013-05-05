
package net.specialattack.ObsidianServer.util.io;

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

    @Override
    public final void close() throws IOException {
        this.dataOutputSteam.close();
    }

    @Override
    public final void writeBoolean(boolean arg) throws IOException {
        this.dataOutputSteam.writeBoolean(arg);
    }

    @Override
    public final void writeByte(int arg) throws IOException {
        this.dataOutputSteam.writeByte(arg & 0xff);
    }

    @Override
    public final void writeBytes(String arg) throws IOException {
        this.dataOutputSteam.writeBytes(arg);
    }

    public final void writeBytes(byte[] arg) throws IOException {
        for (byte theByte : arg) {
            this.dataOutputSteam.writeByte(theByte);
        }
    }

    @Override
    public final void writeChar(int arg) throws IOException {
        byte[] buffer = new byte[2];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        this.dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeChars(String arg) throws IOException {
        char[] chars = arg.toCharArray();

        for (char theChar : chars) {
            this.writeChar(theChar);
        }
    }

    @Override
    public final void writeDouble(double arg) throws IOException {
        this.writeLong(Double.doubleToLongBits(arg));
    }

    @Override
    public final void writeFloat(float arg) throws IOException {
        this.writeInt(Float.floatToIntBits(arg));
    }

    @Override
    public final void writeInt(int arg) throws IOException {
        byte[] buffer = new byte[4];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        this.dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeLong(long arg) throws IOException {
        byte[] buffer = new byte[8];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        this.dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeShort(int arg) throws IOException {
        byte[] buffer = new byte[2];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((arg >> (i * 8)) & 0xff);
        }

        this.dataOutputSteam.write(buffer);
    }

    @Override
    public final void writeUTF(String arg) throws IOException {
        this.dataOutputSteam.writeUTF(arg);
    }

    @Override
    public final void write(int b) throws IOException {
        this.outputStream.write(b);
    }
}
