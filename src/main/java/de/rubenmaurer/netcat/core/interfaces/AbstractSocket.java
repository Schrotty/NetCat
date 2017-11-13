package de.rubenmaurer.netcat.core.interfaces;

/**
 * The abstract socket interface
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public interface AbstractSocket {
    /**
     * Wait for receiving a message.
     *
     * @param maxBytes max bytes of the incoming message
     * @return a {@link java.lang.String} object.
     */
    String receive(int maxBytes);

    /**
     * Send a given message.
     *
     * @param message the given message
     */
    void send(String message);
}
