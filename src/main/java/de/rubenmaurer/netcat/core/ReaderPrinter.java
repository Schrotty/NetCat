package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.netcat.NetCat;

/**
 * A component to read and write on stdout.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class ReaderPrinter extends AbstractActor {

    /**
     * The printer
     */
    private ActorRef printer;

    /**
     * <p>getProps.</p>
     *
     * @return a {@link akka.actor.Props} object.
     */
    public static Props getProps() {
        return Props.create(ReaderPrinter.class);
    }

    /**
     * {@inheritDoc}
     *
     * Gets fired before ReaderPrinter starts
     */
    @Override
    public void preStart() {
        NetCat.getReporter().tell("online", getSelf());

        printer = getContext().actorOf(Props.create(Printer.class), "printer");
        Reader.start();
    }

    @Override
    public void postStop() {
        NetCat.getReporter().tell("offline", getSelf());
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
