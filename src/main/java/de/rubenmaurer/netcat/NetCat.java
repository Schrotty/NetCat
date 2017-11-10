package de.rubenmaurer.netcat;

import akka.actor.ActorSystem;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.util.ManifestHelper;
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
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        AnsiConsole.systemInstall();
        System.out.println(getStartMessage());

        if (params.length == 2) {
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
}
