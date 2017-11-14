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
    private static boolean isSilent = false;

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        AnsiConsole.systemInstall();

        if (params.length >= 2) {
            int port = ParameterValidator.validatePort(params[1]);
            if (params.length >= 3 && params[2].equals("-s")) isSilent = true;


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
    public static boolean runsSilent() {
        return isSilent;
    }
}
