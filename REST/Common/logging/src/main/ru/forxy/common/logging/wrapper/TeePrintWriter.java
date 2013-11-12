package ru.forxy.common.logging.wrapper;

import java.io.PrintWriter;

public class TeePrintWriter extends PrintWriter
{
    private final PrintWriter m_secondaryWriter;

    /**
     * Create a new tee writer with the specified primary and secondary
     * writers.
     *
     * @param primary   The primary writer
     * @param secondary The secondary writer
     */
    public TeePrintWriter(final PrintWriter primary, final PrintWriter secondary)
    {
        super(primary);
        m_secondaryWriter = secondary;
    }

    @Override
    public void write(final int c)
    {
        super.write(c);
        m_secondaryWriter.write(c);
        m_secondaryWriter.flush();
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len)
    {
        super.write(cbuf, off, len);
        m_secondaryWriter.write(cbuf, off, len);
        m_secondaryWriter.flush();
    }

    @Override
    public void write(final String str, final int off, final int len)
    {
        super.write(str, off, len);
        m_secondaryWriter.write(str, off, len);
        m_secondaryWriter.flush();
    }

    @Override
    public void flush()
    {
        super.flush();
        m_secondaryWriter.flush();
    }

    @Override
    public void close()
    {
        super.close();
        m_secondaryWriter.close();
    }
}