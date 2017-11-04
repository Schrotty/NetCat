package de.rubenmaurer.netcat.core.interfaces;

/**
 * 'Actor' interface for telling and shutdown.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 * @since 1.0
 * @deprecated because of akka
 */
@Deprecated
public interface Actor {

    /**
     * Sends actor a message.
     *
     * @param message the message to tell
     * @param sender the sender of the message
     */
    void tell(String message, Actor sender);

    /**
     * Shutdown actor.
     */
    void shutdown();
}
