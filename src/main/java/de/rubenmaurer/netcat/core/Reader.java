package de.rubenmaurer.netcat.core;

import akka.actor.ActorRef;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.components.Message;

import java.util.Scanner;

/**
 * Read lines from the default input until EOF.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class Reader implements Runnable {

    private final ActorRef transceiver;

    /**
     * Start the reader.
     *
     * @param transmitter the used transmitter
     */
    static void start(ActorRef transmitter) {
        new Reader(transmitter);
    }

    /**
     * Create a new Reader
     *
     * @param transceiver the used transmitter
     */
    private Reader(ActorRef transceiver) {
        this.transceiver = transceiver;

        new Thread(this).start();
    }

    /**
     * Read from stdin till EOF
     */
    private void read() {
        NetCat.getReporter().tell(Message.create("Reader started!"), null);

        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                transceiver.tell(scanner.nextLine(), null);
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        } finally {
            transceiver.tell("\u0004", null);
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
        read();
    }
}
