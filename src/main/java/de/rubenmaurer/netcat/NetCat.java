package de.rubenmaurer.netcat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
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
                boot(port);

                if (params[0].equals("-l")) {
                    //transceiver = actorSystem.actorOf(Transceiver.getProps(port), "transceiver");
                    return;
                }

                //actorSystem.actorOf(Props.create(ReaderPrinter.class), "readerPrinter");
            }

            return;
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
        System.out.println(String.format(">> NetCat v.%s_%s", 1.2, 209));
        System.out.println(">> Starting actors/ threads...");
    }

    /**
     * <p>getActorStat.</p>
     *
     * @return a {@link akka.actor.ActorRef} object.
     */
    public static ActorRef getReporter() {
        return reporter;
    }

    public static ActorRef getTransceiver() {
        return transceiver;
    }

    public static ActorRef getReaderPrinter() {
        return readerPrinter;
    }

    private static void boot(int port) {
        ActorSystem actorSystem = ActorSystem.apply("root");

        //start reporter actor
        reporter = actorSystem.actorOf(Reporter.getProps(), "reporter");

        //start main components
        transceiver = actorSystem.actorOf(Transceiver.getProps(port), "transceiver");
        readerPrinter = actorSystem.actorOf(ReaderPrinter.getProps(), "readerPrinter");
    }
}
