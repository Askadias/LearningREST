package ru.forxy.common.logging.wrapper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wraps an input stream creating a buffer of maximum size of  maxBytesToBuffer.
 * Buffer is accessible via getBuffer. Resulting stream can be read as normally.
 */
public class BufferingInputStream extends InputStream
{
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	private final InputStream delegate;
	private final byte[] buffer;

	private int m_pos = 0;

	public BufferingInputStream(final InputStream delegate, final Integer maxBytesToBuffer) throws IOException
	{
		this.delegate = delegate;
		buffer = fillBuffer(delegate, maxBytesToBuffer);
	}

	@Override
	public int read() throws IOException
	{
		if (m_pos < buffer.length)
		{
			return buffer[m_pos++];
		}
		else
		{
			return delegate.read();
		}
	}

	public byte[] getBuffer()
	{
		return buffer; //NOPMD
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
            return IOUtils.toByteArray(input);
            //return EMPTY_BYTE_ARRAY;
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