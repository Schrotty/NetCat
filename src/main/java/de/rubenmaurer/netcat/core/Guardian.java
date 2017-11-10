package de.rubenmaurer.netcat.core;

import akka.actor.*;
import de.rubenmaurer.netcat.core.reporter.Report;
import de.rubenmaurer.netcat.core.reporter.Reporter;

public class Guardian extends AbstractActor {

    /**
     * Reporter reference.
     */
    static ActorRef reporter;

    /**
     * Transceiver reference.
     */
    static ActorRef transceiver;

    /**
     * ReaderPrinter reference.
     */
    static ActorRef readerPrinter;

    private String hostname;
    private int port;

    public static ActorRef getReporter() {
        return reporter;
    }

    public Guardian(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;
    }

    private void checkFamily() {
        boolean allDead = true;
        for (ActorRef each : getContext().getChildren()) {
            if (!each.isTerminated() && !each.equals(reporter)) {
                allDead = false;
                break;
            }
        }

        if (allDead) {
            reporter.tell(PoisonPill.getInstance(), self());

            self().tell(PoisonPill.getInstance(), self());
            context().system().terminate();
        }
    }

    /**
     * Get Props for actor
     *
     * @param port the used port
     * @param host a {@link java.lang.String} object.
     * @return props for the actor
     */
    public static Props getProps(int port, String host) {
        return Props.create(Guardian.class, port, host);
    }

    @Override
    public void preStart() {
        reporter = context().actorOf(Reporter.getProps(), "reporter");
        transceiver = context().actorOf(Transceiver.getProps(port, hostname), "transceiver");
        readerPrinter = context().actorOf(ReaderPrinter.getProps(), "readerPrinter");

        //context().watch(reporter);
        context().watch(transceiver);
        context().watch(readerPrinter);

        reporter.tell(Report.create(Report.Type.ONLINE), self());
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Terminated.class, t -> checkFamily())
                .build();
    }
}
