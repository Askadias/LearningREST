package ru.forxy.common.logging.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * When writing to this stream writes to a buffer if not exceed maxBytesToBuffer
 * Buffer is accessible via getBuffer.
 */
public class BufferingOutputStream extends OutputStream
{
    private final Integer maxBytesToBuffer;

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
    private int pos = 0;

    public BufferingOutputStream(final Integer maxBytesToBuffer)
    {
        this.maxBytesToBuffer = maxBytesToBuffer;
    }

    @Override
    public void write(final int b) throws IOException
    {
        if (maxBytesToBuffer == null || maxBytesToBuffer == -1 || pos++ < maxBytesToBuffer)
        {
            bos.write(b);
        }
    }

    public byte[] getBuffer()
    {
        return bos.toByteArray();
    }
}