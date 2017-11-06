package de.rubenmaurer.netcat.components;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * A helper class for sending and receiving UDP messages.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class UDPSocket {

    private DatagramSocket socket;

    /**
     * The address to store the destination/ target.
     */
    private InetSocketAddress address;

    /**
     * Create a new UDPSocket.
     *
     * @param address the create UDP socket
     * @throws SocketException when something goes wrong
     */
    private UDPSocket(InetSocketAddress address) throws SocketException {
        this.address = address;
        this.socket = new DatagramSocket(address.getPort());
    }

    /**
     * Create a new UDPSocket.
     *
     * @param address the address
     * @return the created UDP socket
     */
    public static UDPSocket createSocket(InetSocketAddress address) {
        try {
            return new UDPSocket(address);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

        return null;
    }

    /**
     * Send a given message.
     *
     * @param message the message to send
     */
    public void send(String message) {
        byte[] payload = message.getBytes();

        try {
            if (address.getAddress().isAnyLocalAddress()) throw new Exception("Missing remote address! Wait for initial transmission!");

            socket.send(new DatagramPacket(payload, payload.length, address));
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Receive a UDP-Transmission
     *
     * @param maxBytes the max length of the transmission
     * @return the transmission as string
     * @throws java.lang.Exception if any.
     */
    public String receive(int maxBytes) throws Exception {
        byte[] payload = new byte[maxBytes];
        DatagramPacket packet = new DatagramPacket(payload, payload.length);

        try {
            socket.receive(packet);

            //TODO: Works well... but why?!
            if (address.equals(new InetSocketAddress("-l", address.getPort()))) {
                address = new InetSocketAddress(packet.getAddress(), packet.getPort());
                socket.connect(packet.getSocketAddress());
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

        return new String(packet.getData(), 0, packet.getLength());
    }
}
