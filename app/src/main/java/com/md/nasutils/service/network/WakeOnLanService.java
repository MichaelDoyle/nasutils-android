/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.network;

/**
 * Provides functionality to send Wake-On-LAN packets
 * 
 * @author michaeldoyle
 */
public interface WakeOnLanService {

    /**
     * Send a number of WOL packets targeted at a specific hostname or IP address 
     * using the specified MAC address and port.
     * 
     * @param networkAddress hostname or IP address
     * @param macAddress MAC address in the format XX:XX:XX:XX:XX or XX-XX-XX-XX-XX
     * @param port port number
     * @param numberOfPackets number of packets to send
     */
    void sendWakeOnLan(String networkAddress, String macAddress, int port, int numberOfPackets);
    
    /**
     * Send a number of WOL packets via the broadcast address using the 
     * specified MAC address and port.
     * 
     * @param macAddress MAC address in the format XX:XX:XX:XX:XX or XX-XX-XX-XX-XX
     * @param port port number
     * @param numberOfPackets number of packets to send
     */
    void sendWakeOnLan(String macAddress, int port, int numberOfPackets);
    
    /**
     * Send a single WOL packet targeted at a specific hostname or IP address 
     * using the specified MAC address and port.
     * 
     * @param networkAddress hostname or IP address
     * @param macAddress MAC address in the format XX:XX:XX:XX:XX or XX-XX-XX-XX-XX
     * @param port port number
     */
    void sendWolPacket(String networkAddress, String macAddress, int port);
}
