package de.rubenmaurer.netcat.core;

import akka.actor.*;
import de.rubenmaurer.netcat.core.interfaces.AbstractSocket;
import de.rubenmaurer.netcat.core.reporter.Report;
import de.rubenmaurer.netcat.core.sockets.UDPSocket;

import java.net.InetSocketAddress;

/**
 * Transceiver for receiving and transmitting.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class Transceiver extends AbstractActor {

    /**
     * Transmitter to tell
     */
    private ActorRef transmitter;

    /**
     * Socket for transmitter and receiver
     */
    private AbstractSocket socket;

    /**
     * Get the transmitter
     *
     * @return the transmitter
     */
    ActorRef getTransmitter() {
        return this.transmitter;
    }

    /**
     * Create a new Transceiver.
     *
     * @param port the port to use
     * @param host a {@link java.lang.String} object.
     */
    public Transceiver(int port, String host) {
        try {
            socket = UDPSocket.createSocket(new InetSocketAddress(host, port));
        } catch (Exception exception) {
            Guardian.reporter.tell(Report.create(Report.Type.ERROR, exception.getMessage()), getSelf());
        }
    }

    /**
     * {@inheritDoc}
     *
     * Gets fired before transceiver starts
     */
    @Override
    public void postStop() {
        Guardian.reporter.tell(Report.create(Report.Type.OFFLINE), self());
    }

    /**
     * {@inheritDoc}
     *
     * Gets fired before transceiver starts
     */
    @Override
    public void preStart() {
        Guardian.reporter.tell(Report.create(Report.Type.ONLINE), self());

        if (socket != null) {
            transmitter = getContext().actorOf(Transmitter.getProps(socket), "transmitter");
            ActorRef threadWatch = getContext().actorOf(ThreadWatch.getProps(), "receiver");

            context().watch(transmitter);
            context().watch(threadWatch);

            Receiver.start(socket, threadWatch);
        }
    }

    private void checkFamily() {
        boolean allDead = true;
        for (ActorRef each : getContext().getChildren()) {
            if (!each.isTerminated()) {
                allDead = false;
                break;
            }
        }

        if (allDead) self().tell(PoisonPill.getInstance(), self());
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("\u0004", s -> {
                    transmitter.tell(s, self());
                    transmitter.tell(PoisonPill.getInstance(), self());
                })
                .match(String.class, s -> transmitter.tell(s, self()))
                .match(Terminated.class, t -> checkFamily())
                .build();
    }

    /**
     * Get Props for actor
     *
     * @param port the used port
     * @return props for the actor
     * @param host a {@link java.lang.String} object.
     */
    public static Props getProps(int port, String host) {
        return Props.create(Transceiver.class, port, host);
    }
}
