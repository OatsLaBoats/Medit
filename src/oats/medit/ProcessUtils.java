package oats.medit;

import javax.swing.*;

public class ProcessUtils
{
    static
    {
        try
        {
            System.loadLibrary("ProcessUtils");
        }
        catch(UnsatisfiedLinkError e)
        {
            JOptionPane.showMessageDialog(null, "Could not load DLL.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static MemoryBlock getNextMemoryBlock(long processHandle, MemoryBlock oldBlock)
    {
        if(oldBlock == null)
        {
            oldBlock = new MemoryBlock();
        }

        long addr = oldBlock.baseAddress + oldBlock.size;

        long regionAddress = getNextRegionAddress(processHandle, addr);
        if(regionAddress == -1)
        {
            return null;
        }

        long regionSize = getNextRegionSize(processHandle, addr);
        if(regionSize == 0)
        {
            return null;
        }

        byte[] regionMemory = readRegionMemory(processHandle, regionAddress, regionSize);
        if(regionMemory == null)
        {
            MemoryBlock mb = new MemoryBlock();
            mb.baseAddress = regionAddress;
            mb.size = regionSize;
            return mb;
        }

        return new MemoryBlock(regionMemory, regionAddress);
    }

    public native static int[] getActiveProcessList();
    public native static String getProcessName(int pid);
    public native static long getProcessHandle(int pid);
    public native static void closeProcessHandle(long processHandle);

    public native static void writeByteAddress(long processHandle, long address, byte data);
    public native static void writeShortAddress(long processHandle, long address, short data);
    public native static void writeIntAddress(long processHandle, long address, int data);
    public native static void writeLongAddress(long processHandle, long address, long data);
    public native static void writeFloatAddress(long processHandle, long address, float data);
    public native static void writeDoubleAddress(long processHandle, long address, double data);

    public native static byte readByteAddress(long processHandle, long address);
    public native static short readShortAddress(long processHandle, long address);
    public native static int readIntAddress(long processHandle, long address);
    public native static long readLongAddress(long processHandle, long address);
    public native static float readFloatAddress(long processHandle, long address);
    public native static double readDoubleAddress(long processHandle, long address);
    private native static long getNextRegionAddress(long processHandle, long baseAddress);
    private native static long getNextRegionSize(long processHandle, long baseAddress);
    private native static byte[] readRegionMemory(long processHandle, long regionAddress, long regionSize);

    /*
    //Native
    public static String getProcessName(int pid)
    {
        return "test_process_" + pid + ".exe";
    }

    //Native
    public static int[] getActiveProcessList()
    {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }

    //Native
    public static long getProcessHandle(int pid)
    {
        return pid * 3415;
    }

    //Native
    public static void closeProcessHandle(long processHandle)
    {
        System.out.println("Process: " + processHandle);
    }

    //Native
    public static void writeByteAddress(long processHandle, long address, byte data)
    {
        System.out.println("Wrote 1 byte to memory: " + data);
    }

    //Native
    public static void writeShortAddress(long processHandle, long address, short data)
    {
        System.out.println("Wrote 2 bytes to memory: " + data);
    }

    //Native
    public static void writeIntAddress(long processHandle, long address, int data)
    {
        System.out.println("Wrote 4 bytes to memory: " + data);
    }

    //Native
    public static void writeLongAddress(long processHandle, long address, long data)
    {
        System.out.println("Wrote 8 bytes to memory: " + data);
    }

    //Native
    public static void writeFloatAddress(long processHandle, long address, float data)
    {
        System.out.println("Wrote 4 bytes to memory: " + data);
    }

    //Native
    public static void writeDoubleAddress(long processHandle, long address, double data)
    {
        System.out.println("Wrote 8 bytes to memory: " + data);
    }

    //Native
    public static byte readByteAddress(long processHandle, long address)
    {
        return 1;
    }

    //Native
    public static short readShortAddress(long processHandle, long address)
    {
        return 1;
    }

    //Native
    public static int readIntAddress(long processHandle, long address)
    {
        return 1;
    }
    //Native
    public static long readLongAddress(long processHandle, long address)
    {
        return 1;
    }

    //Native
    public static float readFloatAddress(long processHandle, long address)
    {
        return 1;
    }

    //Native
    public static double readDoubleAddress(long processHandle, long address)
    {
        return 1;
    }

    //Native
    private static long getNextRegionAddress(long baseAddress)
    {
        if(baseAddress > 4000)
        {
            return 0;
        }

        return baseAddress + 1000;
    }

    //Native
    private static long getNextRegionSize(long baseAddress)
    {
        return 1000;
    }

    //Native
    private static byte[] readRegionMemory(long processHandle, long regionAddress, long regionSize)
    {
        if(regionAddress > 4000)
        {
            return null;
        }

        byte[] arr = new byte[(int)regionSize];

        for(int i = 0; i < arr.length; ++i)
        {
            arr[i] = (byte)(i % 50);
        }

        return arr;
    }
     */
}
