package de.rubenmaurer.netcat.core.interfaces;

/**
 * Actor interface for telling and shutdown 'Netcat'.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public interface Actor {

    /**
     * Tell an another actor an message.
     *
     * @param message the message to tell
     * @param sender the sender of the message
     */
    void tell(String message, Actor sender);

    /**
     * Shutdowns the system.
     */
    void shutdown();
}
