package de.rubenmaurer.netcat.core;

import akka.actor.*;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.core.reporter.Report;
import de.rubenmaurer.netcat.core.reporter.Reporter;

/**
 * Guardian actor for managing.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class Guardian extends AbstractActor {

    /**
     * Loaded subsystems
     */
    private int finSubs = 0;

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

    /**
     * The host to "connect" to
     */
    private String hostname;

    /**
     * The port to send on
     */
    private int port;

    /**
     * <p>Getter for the field <code>reporter</code>.</p>
     *
     * @return a {@link akka.actor.ActorRef} object.
     */
    public static ActorRef getReporter() {
        return reporter;
    }

    /**
     * <p>Constructor for Guardian.</p>
     *
     * @param port a int.
     * @param hostname a {@link java.lang.String} object.
     */
    public Guardian(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;
    }

    /**
     * Look after his children. If they are all dead then commit suicide.
     */
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
     * Checks finished sub systems.
     */
    private void checkLoadingSubs() {
        finSubs++;

        if (finSubs == (NetCat.isBidirectional() ? 6 : 4)) {
            reporter.tell(Report.create(Report.Type.NONE, ""), self());
            reporter.tell(Report.create(Report.Type.INFO, "System started!"), self());
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

    /** {@inheritDoc} */
    @Override
    public void preStart() {
        reporter = context().actorOf(Reporter.getProps(), "reporter");

        reporter.tell(Report.create(Report.Type.INFO, String.format("%s v.%s by %s",
                NetCat.class.getPackage().getImplementationTitle(),
                NetCat.class.getPackage().getImplementationVersion(),
                NetCat.class.getPackage().getImplementationVendor()
        )), self());

        reporter.tell(Report.create(Report.Type.INFO, "Starting system!"), self());
        reporter.tell(Report.create(Report.Type.NONE, ""), self());

        transceiver = context().actorOf(Transceiver.getProps(port, hostname), "transceiver");
        readerPrinter = context().actorOf(ReaderPrinter.getProps(), "readerPrinter");

        context().watch(transceiver);
        context().watch(readerPrinter);

        reporter.tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * <p>createReceive.</p>
     *
     * @return a Receive object.
     */
    public Receive createReceive() {
        return receiveBuilder()
                .match(Terminated.class, t -> checkFamily())
                .matchEquals(Notice.READY, s-> checkLoadingSubs())
                .build();
    }
}
