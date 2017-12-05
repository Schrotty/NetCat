package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.Props;
import de.rubenmaurer.netcat.core.interfaces.AbstractSocket;
import de.rubenmaurer.netcat.core.reporter.Report;

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
    private AbstractSocket socket;

    /**
     * <p>Constructor for Transmitter.</p>
     *
     * @param udpSocket a {@link de.rubenmaurer.netcat.core.interfaces.AbstractSocket} object.
     */
    public Transmitter(AbstractSocket udpSocket) {
        socket = udpSocket;
    }

    /**
     * <p>getProps.</p>
     *
     * @param socket a {@link de.rubenmaurer.netcat.core.sockets.UDPSocket} object.
     * @return a {@link akka.actor.Props} object.
     */
    public static Props getProps(AbstractSocket socket) {
        return Props.create(Transmitter.class, socket);
    }

    /** {@inheritDoc} */
    @Override
    public void preStart() {
        Guardian.reporter.tell(Report.create(Report.Type.ONLINE), self());
        context().parent().tell(Notice.READY, self());
    }

    /** {@inheritDoc} */
    @Override
    public void postStop() {
        Guardian.reporter.tell(Report.create(Report.Type.OFFLINE), self());
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
                .build();
    }
}
