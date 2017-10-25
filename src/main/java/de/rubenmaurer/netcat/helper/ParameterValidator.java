package de.rubenmaurer.netcat.helper;

/**
 * Validate the entered port
 */
public class ParameterValidator {
    public static int validatePort(String port) {
        try {
            int valPort = Integer.parseInt(port);
            if (valPort < 65536 && valPort > 0) {
                return valPort;
            }
        } catch (Exception ignored){
            System.err.println("The port you specified is rubbish!");
        }

        return -1;
    }
}
