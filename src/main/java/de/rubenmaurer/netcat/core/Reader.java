package de.rubenmaurer.netcat.core;

import akka.actor.ActorRef;

import java.util.Scanner;

/**
 * Read lines from the default input until EOF.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version "%I"
 */
public class Reader implements Runnable {

    private final ActorRef transmitter;

    /**
     * Start the reader.
     */
    static void startReader(ActorRef transmitter) {
        new Reader(transmitter);
    }

    /**
     * Create a new Reader
     */
    private Reader(ActorRef transmitter) {
        this.transmitter = transmitter;

        (new Thread(this)).start();
    }

    /**
     * Read from stdin till EOF
     */
    private void read() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                transmitter.tell(scanner.nextLine(), null);
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        } finally {
            transmitter.tell("\u0004", null);
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
