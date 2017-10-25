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
        if (params.length == 0 || params.length > 2) {
            printHelp();
        }

        if (params.length == 1) {
            if (params[0].equals("-l")) {
                Receiver.startReceiver(6669);
            }

            printHelp();
        }

        if (params.length == 2) {
            int port = ParameterValidator.validatePort(params[1]);
            if (port != -1) {
                Reader.startReader(params[0], port);
            }
        }
    }

    /**
     * Prints the Netcat help.
     */
    private static void printHelp() {
        System.out.println("Usage:\tjava -jar Netcat.jar <hostname> <port>\r\n\t\tjava -jar Netcat.jar -l");
        System.exit(0);
    }
}
