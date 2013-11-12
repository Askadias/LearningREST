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
    private final Integer m_maxBytesToBuffer;

    private final ByteArrayOutputStream m_bos = new ByteArrayOutputStream(4096);
    private int m_pos = 0;

    public BufferingOutputStream(final Integer maxBytesToBuffer)
    {
        m_maxBytesToBuffer = maxBytesToBuffer;
    }

    @Override
    public void write(final int b) throws IOException
    {
        if (m_maxBytesToBuffer == null || m_maxBytesToBuffer == -1 || m_pos++ < m_maxBytesToBuffer)
        {
            m_bos.write(b);
        }
    }

    public byte[] getBuffer()
    {
        return m_bos.toByteArray();
    }
}