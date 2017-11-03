package de.rubenmaurer.netcat.core;

import akka.actor.ActorRef;
import de.rubenmaurer.netcat.helper.UDPSocket;

/**
 * Receives UDP messages on a given port.
 * For each received message a 'Printer' actor will created, which handle the message.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class Receiver implements Runnable {

    private final ActorRef readerPrinter;

    /**
     * The udp socket used for transmission
     */
    private UDPSocket socket;

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param port the port to listen to
     */
    static void startReceiver(UDPSocket socket, ActorRef readerPrinter) {
        try {
            new Receiver(socket, readerPrinter);
        } catch (Exception exception) {
            System.err.println(String.format("Cannot start receiver: %s", exception.getMessage()));
        }
    }

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param port the port to listen to
     */
    private Receiver(UDPSocket socket, ActorRef readerPrinter) {
        this.socket = socket;
        this.readerPrinter = readerPrinter;

        (new Thread(this)).start();
    }

    /**
     * Waits for a incoming UDP-Transmission and then create
     * a 'Printer' actor to process it.
     */
    private void receive() throws Exception {
        String data = "";
        while (!data.equals("\u0004")) {
            data = socket.receive(1024);
            readerPrinter.tell(data, null);
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        System.out.println("Receiver: online");

        try {
            receive();
        } catch(Exception exception) {
            System.err.println(exception.getMessage());
        }

    }
}
