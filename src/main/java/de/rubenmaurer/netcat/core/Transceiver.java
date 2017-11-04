package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.netcat.core.interfaces.Actor;
import de.rubenmaurer.netcat.helper.UDPSocket;

import java.net.InetSocketAddress;

/**
 * Transceiver for receiving and transmitting.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version "%I"
 */
public class Transceiver extends AbstractActor implements Actor {

    /**
     * readerPrinter to tell
     */
    private ActorRef readerPrinter;

    /**
     * Transmitter to tell
     */
    private ActorRef transmitter;

    /**
     * Socket for transmitter and receiver
     */
    private UDPSocket socket;

    /**
     * Get the socket
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
     */
    public Transceiver(int port) {
        socket = UDPSocket.createSocket(new InetSocketAddress(port));

        transmitter = getContext().actorOf(Props.create(Transmitter.class, socket));
        readerPrinter = getContext().actorOf(Props.create(ReaderPrinter.class, this));
    }

    /**
     * Tell an another actor an message.
     *
     * @param message the message to tell
     * @param sender  the sender of the message
     */
    public void tell(String message, Actor sender) {

    }

    /**
     * Shutdowns the actor.
     */
    public void shutdown() {

    }

    /**
     * Gets fired before transceiver starts
     */
    @Override
    public void preStart() {
        Receiver.startReceiver(socket, readerPrinter);
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
                .build();
    }
}
