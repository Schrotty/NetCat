package de.rubenmaurer.netcat.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Source;
import de.rubenmaurer.netcat.helper.UDPSocket;

import java.net.InetSocketAddress;
import java.util.Scanner;

import static akka.actor.Props.create;

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
        new Reader(new InetSocketAddress(hostname, port)).read();
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
     */
    private void read() {
        final ActorRef ref = system.actorOf(Props.create(Transmitter.class, UDPSocket.createSocket(this.address)));
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                //system.actorOf(Props.create(Transmitter.class,
                //        UDPSocket.createSocket(this.address))).tell(scanner.nextLine(), null);
                ref.tell(scanner.nextLine(), null);
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        } finally {
            system.actorOf(create(Transmitter.class,
                    UDPSocket.createSocket(this.address)), "terminator").tell("\u0004", null);
        }
    }

    /**
     * Read from stdin till EOF
     *
     * @deprecated replaced by {@link #read()} and weird as f***
     */
    private void streamRead() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                Source.single(scanner.nextLine()).runForeach(
                        s -> system.actorOf(create(Transmitter.class,
                                UDPSocket.createSocket(address))).tell(s, null), ActorMaterializer.create(system));
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        } finally {
            system.actorOf(create(Transmitter.class,
                    UDPSocket.createSocket(this.address)), "terminator").tell("\u0004", null);
        }
    }
}
