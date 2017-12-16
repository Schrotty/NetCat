package de.rubenmaurer.netcat;

import akka.actor.ActorSystem;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.util.ParameterValidator;
import org.fusesource.jansi.AnsiConsole;

/**
 * An bidirectional Netcat implementation.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class NetCat {

    /**
     * Silent mode active?
     */
    private static boolean silent = false;

    /**
     * Is Netcat in bidi mode?
     */
    private static boolean bidirectional = true;

    /**
     * Is in client mode?
     */
    private static boolean client = true;

    /**
     * Netcat uses tcp?
     */
    private static boolean tcp = true;

    /**
     * Netcat in mirror mode?
     */
    private static boolean mirror = false;

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        AnsiConsole.systemInstall();

        if (params.length >= 2) {
            for (String param : params) {
                if (param.equals("-s")) silent = true;
                if (param.equals("-l")) client = false;
                if (param.equals("-uni")) bidirectional = false;
                if (param.equals("-udp")) tcp = false;

                //EXPERIMENTAL
                if (param.equals("-m")) {
                    mirror = true;
                    bidirectional = false;
                }
            }

            int port = ParameterValidator.validatePort(params[1]);

            if (port != -1) {
                ActorSystem actorSystem = ActorSystem.apply("netcat");
                actorSystem.actorOf(Guardian.getProps(port, params[0]), "guardian");
                return;
            }
        }

        System.out.println("Usage:\tjava -jar Netcat.jar <hostname> <port>\r\n\tjava -jar Netcat.jar -l <port>");
    }

    /**
     * Is netcat in silent mode?
     *
     * @return is in silent mode?
     */
    public static boolean isSilent() {
        return silent;
    }

    /**
     * Is netcat in bidi mode?
     *
     * @return is in bidi mode?
     */
    public static boolean isBidirectional() {
        return bidirectional;
    }

    /**
     * Is netcat in client mode?
     *
     * @return is in client mode?
     */
    public static boolean isClient() {
        return client;
    }

    /**
     * Is netcat using tcp?
     *
     * @return uses tcp?
     */
    public static boolean isTCP() {
        return tcp;
    }

    /**
     * Is netcat in mirror mode?
     *
     * @return in mirror mode?
     */
    public static boolean isMirror() {
        return mirror;
    }
}
