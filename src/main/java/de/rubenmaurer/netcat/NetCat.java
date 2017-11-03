package de.rubenmaurer.netcat;

import akka.actor.ActorSystem;
import akka.actor.Props;
import de.rubenmaurer.netcat.core.ReaderPrinter;
import de.rubenmaurer.netcat.core.Transceiver;
import de.rubenmaurer.netcat.helper.ParameterValidator;

/**
 * An unidirectional Netcat implementation.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class NetCat {

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        ActorSystem actorSystem = ActorSystem.apply("net-cat");

        if (params.length == 2) {
            int port = ParameterValidator.validatePort(params[1]);

            if (port != -1) {
                if (params[0].equals("-l")) {
                    actorSystem.actorOf(Props.create(Transceiver.class, port));
                    return;
                }

                actorSystem.actorOf(Props.create(ReaderPrinter.class));
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
}
