package de.rubenmaurer.netcat.core;

import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.components.Message;
import de.rubenmaurer.netcat.components.UDPSocket;

/**
 * Receives UDP messages on a given port.
 * For each received message a 'Printer' actor will created, which handle the message.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class Receiver implements Runnable {

    /**
     * The udp socket used for transmission
     */
    private UDPSocket socket;

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param socket the port to listen to
     */
    static void start(UDPSocket socket) {
        new Receiver(socket);
    }

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param socket the port to listen to
     */
    private Receiver(UDPSocket socket) {
        this.socket = socket;

        new Thread(this).start();
    }

    /**
     * Waits for a incoming UDP-Transmission and then create
     * a 'Printer' actor to process it.
     *
     * @throws Exception when something breaks
     */
    private void receive() throws Exception {
        String data = "";

        NetCat.getReporter().tell(Message.create("Receiver started!"), null);
        while (!data.equals("\u0004")) {
            data = socket.receive(1024);
            NetCat.getReaderPrinter().tell(data, null);
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
        try {
            receive();
        } catch(Exception exception) {
            System.err.println(exception.getMessage());
        }

    }
}
