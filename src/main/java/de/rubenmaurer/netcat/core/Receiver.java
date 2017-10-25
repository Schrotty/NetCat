package de.rubenmaurer.netcat.core;

import akka.actor.ActorSystem;
import akka.actor.Props;
import de.rubenmaurer.netcat.helper.UDPSocket;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Receives UDP messages on a given port.
 * For each received message a 'Printer' actor will created, which handle the message.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class Receiver {

    /**
     * The port to listen to.
     */
    private int port;

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param port the port to listen to
     */
    public static void startReceiver(int port) {
        new Receiver(port).receive();
    }

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param port the port to listen to
     */
    private Receiver(int port) {
        this.port = port;
    }

    /**
     * Waits for a incoming UDP-Transmission and then create
     * a 'Printer' actor to process it.
     */
    private void receive() {
        ActorSystem system = ActorSystem.apply("receiver");

        UDPSocket socket = UDPSocket.createSocket(new InetSocketAddress(port));

        String data = "";
        while(!Objects.equals(data, "\u0004")) {
            data = socket.receive(1024);

            system.actorOf(Props.create(Printer.class)).tell(data, null);
        }

        System.exit(0);
    }
}
