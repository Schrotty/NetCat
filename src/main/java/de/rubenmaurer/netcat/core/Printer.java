package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import de.rubenmaurer.netcat.core.reporter.Report;

/**
 * Receives messages and print them.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version ${buildNumber}
 * @since 1.0
 */
public class Printer extends AbstractActor {

    /**
     * {@inheritDoc}
     *
     * Gets fired before printer starts.
     */
    @Override
    public void preStart() {
        Guardian.reporter.tell(Report.create(Report.Type.ONLINE), self());
        context().parent().tell(Notice.FINISH, self());
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
                .matchEquals("\u0004", s -> context().stop(self()))
                .match(String.class, s -> System.out.println(String.format(">> %s", s)))
                .build();
    }
}
