package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.netcat.core.interfaces.Actor;
import de.rubenmaurer.netcat.helper.UDPSocket;

import java.net.InetSocketAddress;

public class Transceiver extends AbstractActor implements Actor {

    private ActorRef readerPrinter;

    private ActorRef transmitter;

    private UDPSocket socket;

    ActorRef getTransmitter() {
        return this.transmitter;
    }

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

    @Override
    public void preStart() {
        Receiver.startReceiver(socket, readerPrinter);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> transmitter.tell(s, getSelf()))
                .build();
    }
}
