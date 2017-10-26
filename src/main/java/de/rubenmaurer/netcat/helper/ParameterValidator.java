package de.rubenmaurer.netcat.helper;

/**
 * Validate the entered port
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class ParameterValidator {

    /**
     * Tries to validate a string. On success the parsed port will
     * returned. On failure -1 will returned.
     *
     * @param port the port to validate
     * @return the validated port
     */
    public static int validatePort(String port) {
        int valPort = -1;

        try {
            valPort = Integer.parseInt(port);
            if (valPort > 65536 || valPort < 0) throw new Exception("Port out of range!");
        } catch (Exception exception){
            System.err.println("The port you specified is rubbish! Message: " + exception.getMessage());
        }

        return valPort;
    }
}
