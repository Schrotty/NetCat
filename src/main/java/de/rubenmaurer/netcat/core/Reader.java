package de.rubenmaurer.netcat.core;

import akka.actor.ActorSystem;
import akka.actor.Props;
import de.rubenmaurer.netcat.helper.UDPSocket;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Read lines from the default input until EOF.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class Reader {

    /**
     * The address of the target.
     */
    private InetSocketAddress address;

    /**
     * Start the reader.
     *
     * @param hostname the target hostname
     * @param port the targets port
     */
    public static void startReader(String hostname, int port) {
        new Reader(new InetSocketAddress(hostname, port)).read();
    }

    /**
     * Create a new Reader
     *
     * @param address the target address
     */
    private Reader(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Read from console till EOF
     */
    private void read() {
        String line;

        ActorSystem system = ActorSystem.apply("reader");
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            system.actorOf(Props.create(Transmitter.class,
                    UDPSocket.createSocket(this.address))).tell(line, null);
        }

        scanner.close();

        system.actorOf(Props.create(Transmitter.class,
                UDPSocket.createSocket(this.address)), "terminator").tell("\u0004", null);
    }
}
