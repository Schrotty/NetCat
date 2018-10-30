package de.rubenmaurer.netcat.core.mirror;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.io.Tcp;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.core.reporter.Report;

/**
 * Actor for handling incoming tcp messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.3
 */
public class MessageHandler extends AbstractActor {

    /**
     * Get props for actor.
     *
     * @return the props
     */
    public static Props props() {
        return Props.create(MessageHandler.class);
    }

    /**
     * Process an incoming message.
     *
     * @param message the message
     */
    private void messageProcess(String message) {
        if (message.equals("\u0004") || message.equals("\u0004\r\n")) {
            getContext().stop(getSelf());
            return;
        }

        System.out.print(String.format(">> [ %s@localhost ] %s", getSelf().path().name(), message));
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        Guardian.getReporter().tell(Report.create(Report.Type.INFO, String.format("%s is now online!", getSelf().path().name())), self());
    }

    /**
     * Gets fired after stop
     */
    @Override
    public void postStop() {
        Guardian.getReporter().tell(Report.create(Report.Type.INFO, String.format("%s is now offline!", getSelf().path().name())), self());
    }

    /**
     * Process incoming messages.
     *
     * @return a receive.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, msg -> messageProcess(msg.data().decodeString("US-ASCII")))
                .match(Tcp.ConnectionClosed.class, msg -> getContext().stop(getSelf()))
                .build();
    }
}
