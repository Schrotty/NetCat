package de.rubenmaurer.netcat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import de.rubenmaurer.netcat.components.ManifestHelper;
import de.rubenmaurer.netcat.components.ParameterValidator;
import de.rubenmaurer.netcat.components.Reporter;
import de.rubenmaurer.netcat.core.ReaderPrinter;
import de.rubenmaurer.netcat.core.Transceiver;

/**
 * An unidirectional Netcat implementation.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class NetCat {

    private static ActorRef reporter;

    private static ActorRef transceiver;

    private static ActorRef readerPrinter;

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        printStartUp();

        if (params.length == 2) {
            int port = ParameterValidator.validatePort(params[1]);

            if (port != -1) {
                boot(port, params[0]);
                return;
            }
        }

        printHelp();
    }

    /**
     * Print the netcat help message
     */
    private static void printHelp() {
        System.out.println("Usage:\tjava -jar Netcat-<version>.jar <hostname> <port>\r\n\tjava -jar Netcat-<version>.jar -l <port>");
    }

    /**
     * Print the netcat startup message
     */
    private static void printStartUp() {
        ManifestHelper mh = ManifestHelper.create();

        System.out.println(String.format("%s v.%s-%s by %s",
                mh.get("Implementation-Title"),
                mh.get("Implementation-Version"),
                mh.get("Implementation-Build"),
                mh.get("Author")
        ));
        System.out.println(">> Starting actors/ threads...");
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

    private static void boot(int port, String host) {
        ActorSystem actorSystem = ActorSystem.apply("root");

        //start reporter actor
        reporter = actorSystem.actorOf(Reporter.getProps(), "reporter");

        //start main components
        transceiver = actorSystem.actorOf(Transceiver.getProps(port, host), "transceiver");
        readerPrinter = actorSystem.actorOf(ReaderPrinter.getProps(), "readerPrinter");
    }
}
