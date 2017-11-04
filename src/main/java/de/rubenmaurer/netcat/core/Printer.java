package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import de.rubenmaurer.netcat.core.interfaces.Actor;

/**
 * Receives messages and print them.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version "%I%"
 */
public class Printer extends AbstractActor implements Actor {

    /**
     * Prints the given message.
     *
     * @param message the message to tell
     * @param sender the sender of the message
     */
    public void tell(String message, Actor sender) {
        System.out.println(message);
    }

    /**
     * Shutdown this actor.
     */
    public void shutdown() {
        context().stop(self());
    }

    /**
     * Gets fired before printer starts.
     */
    @Override
    public void preStart() {
        System.out.println("Printer: online");
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("\u0004", s -> shutdown())
                .match(String.class, s -> tell(s, this))
                .build();
    }
}
