package de.rubenmaurer.netcat.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * A helper class for sending and receiving UDP messages.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class UDPSocket {

    /**
     * The address to store the destination/ target.
     */
    private InetSocketAddress address;

    /**
     * Create a new UDPSocket.
     *
     * @param address the create UDP socket
     */
    private UDPSocket(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Create a new UDPSocket.
     *
     * @param address the address
     * @return the created UDP socket
     */
    public static UDPSocket createSocket(InetSocketAddress address) {
        return new UDPSocket(address);
    }

    /**
     * Send a given message.
     *
     * @param message the message to send
     */
    public void send(String message) {
        byte[] payload = message.getBytes();

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(new DatagramPacket(payload, payload.length, address));
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Receive a UDP-Transmission
     *
     * @param maxBytes the max length of the transmission
     * @return the transmission as string
     */
    public String receive(int maxBytes) {
        byte[] payload = new byte[maxBytes];
        DatagramPacket packet = new DatagramPacket(payload, payload.length);

        try (DatagramSocket socket = new DatagramSocket(address.getPort())) {
            socket.receive(packet);
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }

        return new String(packet.getData(), 0, packet.getLength());
    }
}
