package de.rubenmaurer.netcat.core.reporter;

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
     * @param type the message
     * @return the final message
     */
    private String messageBuilder(String message, Report.Type type, ActorRef sender) {
        String color = ANSI_BLUE;
        String msgType;

        if (type == Report.Type.ONLINE) {
            color = ANSI_GREEN;
        }

        if (type == Report.Type.OFFLINE || type == Report.Type.ERROR) {
            color = ANSI_RED;
        }

        msgType = String.format("[%s %s %s]", color, type, ANSI_RESET);
        return String.format("%s %s", type == Report.Type.NONE ? "" : msgType, message == null ? sender : message);
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
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Report.class, s -> print(messageBuilder(s.getMessage(), s.getType(), sender())))
                .build();
    }
}
