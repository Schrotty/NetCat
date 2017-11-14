package de.rubenmaurer.netcat.core;

import akka.actor.*;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.core.reporter.Report;

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

    /** {@inheritDoc} */
    @Override
    public void preStart() {
        ActorRef threadWatch;

        Guardian.reporter.tell(Report.create(Report.Type.ONLINE), self());

        if (NetCat.isBidirectional() || !NetCat.isClient()) {
            printer = getContext().actorOf(Props.create(Printer.class), "printer");
            context().watch(printer);
        }

        if (NetCat.isClient() || NetCat.isBidirectional()) {
            threadWatch = getContext().actorOf(ThreadWatch.getProps(), "reader");
            context().watch(threadWatch);
            Reader.start(threadWatch);
        }
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
                .matchEquals("\u0004", s -> {
                    printer.tell(PoisonPill.getInstance(), getSelf());
                })
                .match(String.class, s -> printer.tell(s, getSelf()))
                .match(Terminated.class, t -> checkFamily())
                .matchEquals(Notice.FINISH, s-> context().parent().tell(Notice.FINISH, self()))
                .build();
    }
}
