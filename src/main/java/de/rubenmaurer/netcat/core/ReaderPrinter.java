package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * A component to read and write on stdout.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version "%I"
 */
public class ReaderPrinter extends AbstractActor implements Actor {

    /**
     * The printer
     */
    private ActorRef printer;

    /**
     * The transceiver
     */
    private Transceiver transceiver;

    /**
     * Create a new ReaderPrinter
     *
     * @param transceiver the used transceiver
     */
    public ReaderPrinter(Transceiver transceiver) {
        this();
        this.transceiver = transceiver;
    }

    /**
     * Create a new ReaderPrinter
     */
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

    /**
     * Gets fired before ReaderPrinter starts
     */
    @Override
    public void preStart() {
        Reader.startReader(transceiver.getTransmitter());
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> printer.tell(s, getSelf()))
                .build();
    }
}
