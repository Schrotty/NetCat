package de.rubenmaurer.netcat;

import akka.actor.ActorSystem;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.util.ParameterParser;
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
     * Is Netcat in bidi mode?
     */
    private static boolean isBidirectional = true;

    /**
     * Is in client mode?
     */
    private static boolean isClient = true;

    /**
     * Netcat uses tcp?
     */
    private static boolean useTcp = true;

    /**
     * Main entry point of this application.
     *
     * @param params start parameter
     */
    public static void main(String[] params) {
        AnsiConsole.systemInstall();

        if (params.length >= 2) {
            ParameterParser.parse(params);
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
    public static boolean runsSilent() {
        return isSilent;
    }

    /**
     * Is netcat in bidi mode?
     *
     * @return is in bidi mode?
     */
    public static boolean isBidirectional() {
        return isBidirectional;
    }

    /**
     * Is netcat in client mode?
     *
     * @return is in client mode?
     */
    public static boolean isClient() {
        return isClient;
    }

    /**
     * Is netcat using tcp?
     *
     * @return uses tcp?
     */
    public static boolean isTCP() {
        return useTcp;
    }

    public static void setSilentMode(boolean isSilent) {
        NetCat.isSilent = isSilent;
    }

    public static void setUniMode(boolean uniMode) {
        NetCat.isBidirectional = uniMode;
    }

    public static void setUDPMode(boolean UDPMode) {
        NetCat.useTcp = UDPMode;
    }

    public static void setClientMode(boolean clientMode) {
        NetCat.isClient = clientMode;
    }
}
