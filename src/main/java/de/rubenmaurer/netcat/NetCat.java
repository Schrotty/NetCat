package de.rubenmaurer.netcat;

import de.rubenmaurer.netcat.core.Reader;
import de.rubenmaurer.netcat.core.Receiver;
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
        if (params.length == 2) {
            int port = ParameterValidator.validatePort(params[1]);

            if (port != -1) {
                if (params[0].equals("-l")) {
                    Receiver.startReceiver(port);
                    System.exit(0);
                }

                Reader.startReader(params[0], port);
                System.exit(0);
            }
        }

        printHelp();
    }

    /**
     * Prints the netcat help.
     */
    private static void printHelp() {
        System.out.println("Usage:\tjava -jar Netcat.jar <hostname> <port>\r\n\tjava -jar Netcat.jar -l <port>");
    }
}
