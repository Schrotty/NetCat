package de.rubenmaurer.netcat.core;

import akka.actor.ActorRef;

import java.util.Scanner;

/**
 * Read lines from the default input until EOF.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class Reader implements Runnable {

    private ActorRef threadWatch;

    /**
     * Start the reader.
     */
    static void start(ActorRef threadWatch) {
        new Reader(threadWatch);
    }

    /**
     * Create a new Reader
     */
    private Reader(ActorRef threadWatch) {
        this.threadWatch = threadWatch;

        new Thread(this).start();
    }

    /**
     * Read from stdin till EOF
     */
    private void read() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                Guardian.transceiver.tell(scanner.nextLine(), ActorRef.noSender());
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        } finally {
            Guardian.transceiver.tell("\u0004", ActorRef.noSender());
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
        read();
    }
}
