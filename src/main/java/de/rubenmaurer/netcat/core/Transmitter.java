package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import de.rubenmaurer.netcat.core.interfaces.Actor;
import de.rubenmaurer.netcat.helper.UDPSocket;

/**
 * Transmit message using UDP.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class Transmitter extends AbstractActor implements Actor {

    /**
     * The used UDP socket
     */
    private UDPSocket socket;

    /**
     * Create a new Transmitter with a given socket.
     *
     * @param socket the socket
     */
    public Transmitter(UDPSocket socket) {
        this.socket = socket;
    }

    /**
     * Send the given message using the UDP socket.
     *
     * @param message the message to tell
     * @param sender the sender of the message
     */
    public void tell(String message, Actor sender) {
        socket.send(message);
    }

    /**
     * Send termination message and shuts down system.
     */
    public void shutdown() {
        this.tell("\u0004", this);
        System.exit(0);
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public Receive createReceive() {
        return receiveBuilder().matchEquals("\u0004", s -> this.shutdown()).match(String.class, s -> {
            tell(s, this);
            context().stop(self());
        }).build();
    }
}
