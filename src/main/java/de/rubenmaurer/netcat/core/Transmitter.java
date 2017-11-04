package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.components.UDPSocket;

/**
 * Transmit message using UDP.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version 1.0
 */
public class Transmitter extends AbstractActor {

    /**
     * The used udp socket
     */
    private UDPSocket socket;

    /**
     * <p>Constructor for Transmitter.</p>
     *
     * @param udpSocket a {@link de.rubenmaurer.netcat.components.UDPSocket} object.
     */
    public Transmitter(UDPSocket udpSocket) {
        socket = udpSocket;
    }

    /** {@inheritDoc} */
    @Override
    public void preStart() {
        NetCat.getActorStat().tell("starting", getSelf());
    }

    /** {@inheritDoc} */
    @Override
    public void postStop() {
        NetCat.getActorStat().tell("stopped", getSelf());
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, socket::send)
                .matchEquals("\u0004", s -> getContext().stop(self()))
                .build();
    }
}
