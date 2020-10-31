package oats.medit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MemoryBlock
{
    public ByteBuffer memory;
    public long size;
    public long baseAddress;

    public MemoryBlock(byte[] bytes, long baseAddress)
    {
        memory = ByteBuffer.wrap(bytes);
        memory.order(ByteOrder.LITTLE_ENDIAN);
        size = bytes.length;
        this.baseAddress = baseAddress;
    }

    public MemoryBlock()
    {
        memory = null;
        size = 0;
        baseAddress = 0;
    }
}
