package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.components.Message;
import de.rubenmaurer.netcat.components.UDPSocket;

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
    private UDPSocket socket;

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
            NetCat.getReporter().tell(Message.create("error", exception.getMessage()), getSelf());
        }
    }

    /**
     * {@inheritDoc}
     *
     * Gets fired before transceiver starts
     */
    @Override
    public void postStop() {
        NetCat.getReporter().tell("offline", getSelf());
    }

    /**
     * {@inheritDoc}
     *
     * Gets fired before transceiver starts
     */
    @Override
    public void preStart() {
        NetCat.getReporter().tell("online", getSelf());

        if (socket != null) {
            transmitter = getContext().actorOf(Transmitter.getProps(socket), "transmitter");
            getContext().watch(transmitter);

            Receiver.start(socket);
        }
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> transmitter.tell(s, getSelf()))
                .match(Integer.class, s -> {
                    transmitter.tell("\u0004", getSelf());

                    context().unwatch(transmitter);
                    transmitter.tell(PoisonPill.getInstance(), getSelf());
                })
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
