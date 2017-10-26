package de.rubenmaurer.netcat.core;

import akka.stream.*;
import akka.stream.javadsl.*;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Props;
import de.rubenmaurer.netcat.helper.UDPSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Read lines from the default input until EOF.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.1
 */
public class Reader {

    /**
     * The reader actor system.
     */
    private ActorSystem system;

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
        new Reader(new InetSocketAddress(hostname, port)).streamRead();
    }

    /**
     * Create a new Reader
     *
     * @param address the target address
     */
    private Reader(InetSocketAddress address) {
        system = ActorSystem.apply("reader");

        this.address = address;
    }

    /**
     * Read from stdin till EOF
     *
     * @deprecated
     */
    private void read() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                system.actorOf(Props.create(Transmitter.class,
                        UDPSocket.createSocket(this.address))).tell(scanner.nextLine(), null);
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

        system.actorOf(Props.create(Transmitter.class,
                UDPSocket.createSocket(this.address)), "terminator").tell("\u0004", null);
    }

    private void streamRead() {
        final Source<Integer, NotUsed> source = Source.range(1, 100);

        source.runForeach(i -> system.actorOf(Props.create(Transmitter.class, UDPSocket.createSocket(address))).tell("Herman", null), ActorMaterializer.create(system));
    }
}
