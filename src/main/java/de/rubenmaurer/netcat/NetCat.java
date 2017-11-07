package de.rubenmaurer.netcat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import de.rubenmaurer.netcat.components.ManifestHelper;
import de.rubenmaurer.netcat.components.ParameterValidator;
import de.rubenmaurer.netcat.components.Reporter;
import de.rubenmaurer.netcat.core.ReaderPrinter;
import de.rubenmaurer.netcat.core.Transceiver;

/**
 * An bidirectional Netcat implementation.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class NetCat {

    /**
     * Reporter reference.
     */
    private static ActorRef reporter;

    /**
     * Transceiver reference.
     */
    private static ActorRef transceiver;

    /**
     * ReaderPrinter reference.
     */
    private static ActorRef readerPrinter;

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        System.out.println(getStartMessage());

        if (params.length == 2) {
            int port = ParameterValidator.validatePort(params[1]);

            if (port != -1) {
                if (!boot(port, params[0])) {
                    System.out.println(">> boot failed!\nNetcat terminates!");
                    System.exit(1);
                }

                return;
            }
        }

        System.out.println(getHelpMessage());
    }

    /**
     * Print the netcat help message
     */
    private static String getHelpMessage() {
        return "Usage:\tjava -jar Netcat-<version>.jar <hostname> <port>\r\n\tjava -jar Netcat-<version>.jar -l <port>";
    }

    /**
     * Print the netcat startup message
     */
    private static String getStartMessage() {
        ManifestHelper mh = ManifestHelper.create();

        return new StringBuilder(String.format("%s v.%s-%s by %s\n",
                mh.get("Implementation-Title"),
                mh.get("Implementation-Version"),
                mh.get("Implementation-Build"),
                mh.get("Author")
        )).append(">> Starting actors/ threads...").toString();
    }
    
    /**
     * <p>Getter for the field <code>reporter</code>.</p>
     *
     * @return a {@link akka.actor.ActorRef} object.
     */
    public static ActorRef getReporter() {
        return reporter;
    }

    /**
     * <p>Getter for the field <code>transceiver</code>.</p>
     *
     * @return a {@link akka.actor.ActorRef} object.
     */
    public static ActorRef getTransceiver() {
        return transceiver;
    }

    /**
     * <p>Getter for the field <code>readerPrinter</code>.</p>
     *
     * @return a {@link akka.actor.ActorRef} object.
     */
    public static ActorRef getReaderPrinter() {
        return readerPrinter;
    }

    /**
     * Starts all needed actors and checks for failing actors.
     *
     * @param port the port to listen to
     * @param host the host to transmit to
     * @return if boot was a success
     */
    private static boolean boot(int port, String host) {
        ActorSystem actorSystem = ActorSystem.apply("root");

        //start reporter actor
        reporter = actorSystem.actorOf(Reporter.getProps(), "reporter");

        //start main components
        transceiver = actorSystem.actorOf(Transceiver.getProps(port, host), "transceiver");
        readerPrinter = actorSystem.actorOf(ReaderPrinter.getProps(), "readerPrinter");

        return !reporter.isTerminated() && !transceiver.isTerminated() && !readerPrinter.isTerminated();
    }
}
