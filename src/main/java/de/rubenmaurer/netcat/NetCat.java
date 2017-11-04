package de.rubenmaurer.netcat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import de.rubenmaurer.netcat.components.Reporter;
import de.rubenmaurer.netcat.components.ParameterValidator;
import de.rubenmaurer.netcat.core.ReaderPrinter;
import de.rubenmaurer.netcat.core.Transceiver;

/**
 * An unidirectional Netcat implementation.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class NetCat {

    private static ActorRef actorStatus;

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        ActorSystem actorSystem = ActorSystem.apply("root");
        actorStatus = actorSystem.actorOf(Reporter.getProps(), "actor-status");

        printStartUp();
        if (params.length == 2) {
            int port = ParameterValidator.validatePort(params[1]);

            if (port != -1) {
                if (params[0].equals("-l")) {
                    actorSystem.actorOf(Props.create(Transceiver.class, port), "transceiver");
                    return;
                }

                actorSystem.actorOf(Props.create(ReaderPrinter.class), "readerPrinter");
            }

            return;
        }

        printHelp();
    }

    /**
     * Prints the netcat help.
     */
    private static void printHelp() {
        System.out.println("Usage:\tjava -jar Netcat-<version>.jar <hostname> <port>\r\n\tjava -jar Netcat-<version>.jar -l <port>");
    }

    private static void printStartUp() {
        System.out.println("[INFO] --------------------------");
        System.out.println(String.format("[INFO] version: %s build: %s", "1.2", "136"));
        System.out.println("[INFO] --------------------------");
    }

    /**
     * <p>getActorStat.</p>
     *
     * @return a {@link akka.actor.ActorRef} object.
     */
    public static ActorRef getActorStat() {
        return actorStatus;
    }
}
