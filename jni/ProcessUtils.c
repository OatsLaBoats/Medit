#include "oats_medit_ProcessUtils.h"

#include <stdlib.h>
#include <windows.h>
#include <psapi.h>

JNIEXPORT jintArray JNICALL Java_oats_medit_ProcessUtils_getActiveProcessList(JNIEnv *env, jclass class)
{
    DWORD processes[2048];
    DWORD bytesNeeded = 0;
    DWORD size = sizeof(processes);

    if(!EnumProcesses(processes, size, &bytesNeeded))
    {
        return NULL;
    }

    jsize arraySize = (jsize)(bytesNeeded / sizeof(DWORD));
    jintArray result = (*env)->NewIntArray(env, arraySize);
    if(result == 0)
    {
        return NULL;
    }

    (*env)->SetIntArrayRegion(env, result, 0, arraySize, (jint *)processes);

    return result;
}

JNIEXPORT jstring JNICALL Java_oats_medit_ProcessUtils_getProcessName (JNIEnv *env, jclass class, jint pid)
{
    HANDLE process = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, pid);
    if(process == 0)
    {
        return NULL;
    }

    HMODULE baseModule;
    DWORD needed;
    if(!EnumProcessModules(process, &baseModule, sizeof(baseModule), &needed))
    {
        CloseHandle(process);
        return NULL;
    }

    char name[1024];
    if(!GetModuleBaseNameA(process, baseModule, name, sizeof(name)))
    {
        CloseHandle(process);
        return NULL;
    }

    CloseHandle(process);

    jstring result = (*env)->NewStringUTF(env, name);
    if(result == 0)
    {
        return NULL;
    }

    return result;
}

JNIEXPORT jlong JNICALL Java_oats_medit_ProcessUtils_getProcessHandle(JNIEnv *env, jclass class, jint pid)
{
    jlong result = (jlong)OpenProcess(PROCESS_ALL_ACCESS, FALSE, pid);
    return result;
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_closeProcessHandle(JNIEnv *env, jclass class, jlong processHandle)
{
    CloseHandle((HANDLE)processHandle);
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_writeByteAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address, jbyte data)
{
    WriteProcessMemory((HANDLE)processHandle, (void *)address, &data, sizeof(data), 0);
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_writeShortAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address, jshort data)
{
    WriteProcessMemory((HANDLE)processHandle, (void *)address, &data, sizeof(data), 0);
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_writeIntAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address, jint data)
{
    WriteProcessMemory((HANDLE)processHandle, (void *)address, &data, sizeof(data), 0);
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_writeLongAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address, jlong data)
{
    WriteProcessMemory((HANDLE)processHandle, (void *)address, &data, sizeof(data), 0);
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_writeFloatAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address, jfloat data)
{
    WriteProcessMemory((HANDLE)processHandle, (void *)address, &data, sizeof(data), 0);
}

JNIEXPORT void JNICALL Java_oats_medit_ProcessUtils_writeDoubleAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address, jdouble data)
{
    WriteProcessMemory((HANDLE)processHandle, (void *)address, &data, sizeof(data), 0);
}

JNIEXPORT jbyte JNICALL Java_oats_medit_ProcessUtils_readByteAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address)
{
    jbyte result = 0;
    
    ReadProcessMemory((HANDLE)processHandle, (void *)address, &result, sizeof(result), 0);
    
    return result;
}

JNIEXPORT jshort JNICALL Java_oats_medit_ProcessUtils_readShortAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address)
{
    jshort result = 0;
    
    ReadProcessMemory((HANDLE)processHandle, (void *)address, &result, sizeof(result), 0);
    
    return result;
}

JNIEXPORT jint JNICALL Java_oats_medit_ProcessUtils_readIntAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address)
{
    jint result = 0;
    
    ReadProcessMemory((HANDLE)processHandle, (void *)address, &result, sizeof(result), 0);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_oats_medit_ProcessUtils_readLongAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address)
{
    jlong result = 0;
    
    ReadProcessMemory((HANDLE)processHandle, (void *)address, &result, sizeof(result), 0);
    
    return result;
}

JNIEXPORT jfloat JNICALL Java_oats_medit_ProcessUtils_readFloatAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address)
{
    jfloat result = 0;
    
    ReadProcessMemory((HANDLE)processHandle, (void *)address, &result, sizeof(result), 0);
    
    return result;
}

JNIEXPORT jdouble JNICALL Java_oats_medit_ProcessUtils_readDoubleAddress(JNIEnv *env, jclass class, jlong processHandle, jlong address)
{
    jdouble result = 0;
    
    ReadProcessMemory((HANDLE)processHandle, (void *)address, &result, sizeof(result), 0);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_oats_medit_ProcessUtils_getNextRegionAddress(JNIEnv *env, jclass class, jlong processHandle, jlong baseAddress)
{
    MEMORY_BASIC_INFORMATION mbi;
    if(VirtualQueryEx((HANDLE)processHandle, (void *)baseAddress, &mbi, sizeof(mbi)) == 0)
    {
        return -1;
    }

    return (jlong)mbi.BaseAddress;
}

JNIEXPORT jlong JNICALL Java_oats_medit_ProcessUtils_getNextRegionSize(JNIEnv *env, jclass class, jlong processHandle, jlong baseAddress)
{
    MEMORY_BASIC_INFORMATION mbi;
    if(VirtualQueryEx((HANDLE)processHandle, (void *)baseAddress, &mbi, sizeof(mbi)) == 0)
    {
        return 0;
    }

    return (jlong)mbi.RegionSize;
}

JNIEXPORT jbyteArray JNICALL Java_oats_medit_ProcessUtils_readRegionMemory(JNIEnv *env, jclass class, jlong processHandle, jlong regionAddress, jlong regionSize)
{
    jbyte *buffer = malloc(regionSize);
    if(buffer == 0)
    {
        //printf("Buffer allocation failed %lld\n", regionSize);
        return NULL;
    }

    //printf("%p  %p %lld\n", (void *)(regionAddress), (void*)(regionAddress + regionSize), regionSize);

    SIZE_T read;
    if(ReadProcessMemory((HANDLE)processHandle, (void *)regionAddress, buffer, (SIZE_T)regionSize, &read) == 0)
    {        
        //printf("ERROR READ\n");
        free(buffer);
        return NULL;
    }

    jbyteArray result = (*env)->NewByteArray(env, (jsize)regionSize);
    if(result == 0)
    {
        //printf("JArray creation failed\n");
        free(buffer);
        return NULL;
    }

    (*env)->SetByteArrayRegion(env, result, 0, (jsize)regionSize, buffer);
    free(buffer);

    return result;
}