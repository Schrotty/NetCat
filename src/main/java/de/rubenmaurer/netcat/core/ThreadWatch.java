package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;
import de.rubenmaurer.netcat.core.reporter.Report;

public class ThreadWatch extends AbstractActor {

    /**
     * <p>getProps.</p>
     *
     * @return a {@link akka.actor.Props} object.
     */
    public static Props getProps() {
        return Props.create(ThreadWatch.class);
    }

    @Override
    public void preStart() {
        Guardian.reporter.tell(Report.create(Report.Type.ONLINE), self());
    }

    @Override
    public void postStop() {
        Guardian.reporter.tell(Report.create(Report.Type.OFFLINE), self());
    }

    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("finish", s -> {
                    self().tell(PoisonPill.getInstance(), self());
                })
                .build();
    }
}
