package de.rubenmaurer.netcat.core;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.Props;
import de.rubenmaurer.netcat.core.reporter.Report;

/**
 * <p>ThreadWatch class.</p>
 *
 * @author ruben
 * @version $Id: $Id
 */
public class ThreadWatch extends AbstractActor {

    /**
     * <p>getProps.</p>
     *
     * @return a {@link akka.actor.Props} object.
     */
    public static Props getProps() {
        return Props.create(ThreadWatch.class);
    }

    /** {@inheritDoc} */
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
     * <p>createReceive.</p>
     *
     * @return a Receive object.
     */
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Notice.FINISH, s -> {
                    self().tell(PoisonPill.getInstance(), self());
                })
                .build();
    }
}
