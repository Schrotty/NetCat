package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class ReaderPrinter extends AbstractActor implements Actor {

    private ActorRef printer;

    private Transceiver transceiver;

    public ReaderPrinter(Transceiver transceiver) {
        this();
        this.transceiver = transceiver;
    }

    private ReaderPrinter() {
        printer = getContext().actorOf(Props.create(Printer.class));
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
        Reader.startReader(transceiver.getTransmitter());
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> printer.tell(s, getSelf()))
                .build();
    }
}
