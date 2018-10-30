package de.rubenmaurer.netcat.core.sockets;

import akka.actor.ActorRef;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.core.reporter.Report;
import de.rubenmaurer.netcat.core.interfaces.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * A helper class for sending and receiving UDP messages.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 * @since 1.0
 */
public class UDPSocket implements AbstractSocket {

    /**
     * The used datagramSocket
     */
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
        this.socket = new DatagramSocket(NetCat.isClient() ? 0 : address.getPort());
    }

    /**
     * Create a new UDPSocket.
     *
     * @param address the address
     * @return the created UDP socket
     * @throws java.net.SocketException if any.
     */
    public static UDPSocket createSocket(InetSocketAddress address) throws SocketException {
        return new UDPSocket(address);
    }

    /**
     * {@inheritDoc}
     *
     * Send a given message.
     */
    public void send(String message) {
        byte[] payload = message.getBytes();

        try {
            if (address.getAddress() == null) throw new Exception("Missing remote address! Wait for initial transmission!");
            socket.send(new DatagramPacket(payload, payload.length, address));
        } catch (Exception e) {
            Guardian.getReporter().tell(Report.create(Report.Type.ERROR, e.getMessage()), ActorRef.noSender());
        }
    }

    /**
     * {@inheritDoc}
     *
     * Receive a UDP-Transmission
     */
    public String receive(int maxBytes) {
        byte[] payload = new byte[maxBytes];
        DatagramPacket packet = new DatagramPacket(payload, payload.length);

        try {
            socket.receive(packet);

            //TODO: Works well...
            if (address.equals(new InetSocketAddress("-l", address.getPort()))) {
                address = new InetSocketAddress(packet.getAddress(), packet.getPort());
                //socket.connect(packet.getSocketAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(packet.getData(), 0, packet.getLength());
    }
}
