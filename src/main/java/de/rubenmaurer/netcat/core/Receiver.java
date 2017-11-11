package de.rubenmaurer.netcat.core;

import akka.actor.ActorRef;
import de.rubenmaurer.netcat.core.interfaces.AbstractSocket;
import de.rubenmaurer.netcat.core.reporter.Report;

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
    private AbstractSocket socket;

    /**
     *
     */
    private ActorRef threadWatch;

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param socket the port to listen to
     */
    static void start(AbstractSocket socket, ActorRef threadWatch) {
        new Receiver(socket, threadWatch);
    }

    /**
     * Start waiting for a UDP-Transmission.
     *
     * @param socket the port to listen to
     */
    private Receiver(AbstractSocket socket, ActorRef threadWatch) {
        this.threadWatch = threadWatch;
        this.socket = socket;

        new Thread(this).start();
    }

    /**
     * Waits for a incoming UDP-Transmission and then create
     * a 'Printer' actor to process it.
     */
    private void receive() {
        String data = "";

        try {
            while (!data.equals("\u0004")) {
                data = socket.receive(1024);
                Guardian.readerPrinter.tell(data, ActorRef.noSender());
            }
        } catch (Exception exception) {
            Guardian.reporter.tell(Report.create(Report.Type.ERROR, exception.getMessage()), ActorRef.noSender());
        } finally {
            threadWatch.tell(Notice.FINISH, ActorRef.noSender());
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
        receive();
    }
}
