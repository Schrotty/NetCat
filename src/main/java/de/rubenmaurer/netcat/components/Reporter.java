package de.rubenmaurer.netcat.components;

import akka.actor.AbstractActor;
import akka.actor.Props;
import de.rubenmaurer.netcat.NetCat;

/**
 * <p>Reporter class.</p>
 *
 * @author ruben
 * @version $Id: $Id
 */
public class Reporter extends AbstractActor {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";

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
     * @param sender the sender name
     * @param msg the message
     * @return the final message
     */
    private String messageBuilder(String sender, String msg) {
        String color = ANSI_BLUE;

        if (msg.equals("online")) {
            color = ANSI_GREEN;
        }

        if (msg.equals("offline") || msg.equals("error")) {
            color = ANSI_RED;
        }

        return String.format("[%s %s %s] %s", color, msg.toUpperCase(), ANSI_RESET, sender);
    }

    /**
     * Print a message on stdin.
     *
     * @param message the message to print
     */
    private void print(String message) {
        System.out.println(String.format(">> %s", message));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postStop() {
        NetCat.getReporter().tell("offline", getSelf());
    }

    /** {@inheritDoc} */
    @Override
    public void preStart() {
        getSelf().tell("online", getSelf());
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> print(messageBuilder(getSender().toString(), s)))
                .match(Message.class, s -> print(messageBuilder(s.getSender(), s.getMessage())))
                .build();
    }
}
