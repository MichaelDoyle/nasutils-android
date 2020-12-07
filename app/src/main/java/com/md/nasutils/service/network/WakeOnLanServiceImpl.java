/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.network;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Default implementation for {@link WakeOnLanService}
 * 
 * @see WakeOnLanService
 * 
 * @author michaeldoyle
 */
public class WakeOnLanServiceImpl implements WakeOnLanService {

    private static final String TAG = WakeOnLanServiceImpl.class.getSimpleName();
    
    private static final String BROADCAST = "255.255.255.255";

    public void sendWakeOnLan(String networkAddress, String macAddress, int port, int numberOfPackets) {
        for (int i=1; i<=numberOfPackets; i++) {
            Log.i(TAG, "Sending WOL (" + i + " of " + numberOfPackets + ") to " + macAddress);
            sendWolPacket(networkAddress, macAddress, port);
        }
    }
    
    public void sendWakeOnLan(String macAddress, int port, int numberOfPackets) {
            sendWakeOnLan(BROADCAST, macAddress, port, numberOfPackets);
    }
    
    public void sendWolPacket(String networkAddress, String macAddress, int port) {
        try {
            byte[] macBytes = createMacBytes(macAddress);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            InetAddress address = InetAddress.getByName(networkAddress);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            Log.i(TAG, "Wake-on-LAN packet sent.");
        } catch (UnknownHostException e) {
            Log.e(TAG, "Failed to send Wake-on-LAN packet: " + e.getStackTrace());
        } catch (SocketException e) {
            Log.e(TAG, "Failed to send Wake-on-LAN packet: " + e.getStackTrace());
        } catch (IOException e) {
            Log.e(TAG, "Failed to send Wake-on-LAN packet: " + e.getStackTrace());
        }
    }
    
    /**
     * Create byte array from a properly formatted MAC address
     * @param macStr mac address in the format XX:XX:XX:XX:XX or XX-XX-XX-XX-XX
     * @return byte array representing the MAC address
     * @throws NumberFormatException
     */
    protected byte[] createMacBytes(String macStr) throws NumberFormatException {
        byte[] bytes = new byte[6];
        
        String[] hex = macStr.split("(\\:|\\-)");
        
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
