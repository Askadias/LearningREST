package ru.forxy.common.logging.wrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wraps an input stream creating a buffer of maximum size of  maxBytesToBuffer.
 * Buffer is accessible via getBuffer. Resulting stream can be read as normally.
 */
public class BufferingInputStream extends InputStream
{
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	private final InputStream m_delegate;
	private final byte[] m_buffer;

	private int m_pos = 0;

	public BufferingInputStream(final InputStream delegate, final Integer maxBytesToBuffer) throws IOException
	{
		m_delegate = delegate;
		m_buffer = fillBuffer(delegate, maxBytesToBuffer);
	}

	@Override
	public int read() throws IOException
	{
		if (m_pos < m_buffer.length)
		{
			return m_buffer[m_pos++];
		}
		else
		{
			return m_delegate.read();
		}
	}

	public byte[] getBuffer()
	{
		return m_buffer; //NOPMD
	}

	/**
	 * Read bytes from input stream
	 *
	 * @param input            input stream to read
	 * @param maxBytesToBuffer maximum number of bytes to read
	 * @return array of bytes read from input stream, with size less or equals maxBytesToBuffer,
	 *         empty array flyweight if nothing read
	 * @throws IOException in case of error
	 */
	private byte[] fillBuffer(final InputStream input, final Integer maxBytesToBuffer) throws IOException
	{
		if (maxBytesToBuffer == null || maxBytesToBuffer == -1)
		{
			//TODO return IOUtils.toByteArray(input);
            return new byte[0];
		}
		if (maxBytesToBuffer == 0)
		{
			return EMPTY_BYTE_ARRAY; //NOPMD
		}
		// read at maximum maxBytesToBuffer from stream
		final byte[] lb = new byte[maxBytesToBuffer];
		final int bytesRead = input.read(lb);
		if (bytesRead <= 0)
		{
			return EMPTY_BYTE_ARRAY; //NOPMD
		}
		if (bytesRead == maxBytesToBuffer)
		{
			return lb;
		}
		else
		{
			final byte[] rb = new byte[bytesRead];
			System.arraycopy(lb, 0, rb, 0, bytesRead);
			return rb;
		}
	}
}