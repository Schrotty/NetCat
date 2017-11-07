package de.rubenmaurer.netcat.core;

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

    /**
     * Start the reader.
     */
    static void start() {
        new Reader();
    }

    /**
     * Create a new Reader
     */
    private Reader() {
        new Thread(this).start();
    }

    /**
     * Read from stdin till EOF
     */
    private void read() {
        NetCat.getReporter().tell(Message.create("Reader started!"), null);

        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                NetCat.getTransceiver().tell(scanner.nextLine(), null);
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        } finally {
            NetCat.getTransceiver().tell("\u0004", null);
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
