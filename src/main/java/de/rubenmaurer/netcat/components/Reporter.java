package de.rubenmaurer.netcat.components;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * <p>Reporter class.</p>
 *
 * @author ruben
 * @version $Id: $Id
 */
public class Reporter extends AbstractActor {

    /**
     * <p>getProps.</p>
     *
     * @return a {@link akka.actor.Props} object.
     */
    public static Props getProps() {
        return Props.create(Reporter.class);
    }

    /**
     * Build new message from sender ref ans message string.
     *
     * @param sender the sender ref
     * @param msg the message
     * @return the final message
     */
    private String messageBuilder(ActorRef sender, String msg) {
        return String.format("%s: %s", sender, msg);
    }

    /**
     * Print a message on stdin.
     *
     * @param message the message to print
     */
    private void print(String message) {
        System.out.println(String.format(">> %s", message));
    }

    /** {@inheritDoc} */
    @Override
    public void preStart() {
        print(messageBuilder(getSelf(), "starting"));
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> print(messageBuilder(getSender(), s)))
                .match(Message.class, s -> print(s.getMessage()))
                .build();
    }
}
