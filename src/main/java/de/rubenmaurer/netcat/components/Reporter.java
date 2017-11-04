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

    private String messageBuilder(ActorRef sender, String msg) {
        return String.format("%s: %s", sender, msg);
    }

    private void call(String message) {
        System.out.println(String.format(">> %s", message));
    }

    @Override
    public void preStart() {
        call(messageBuilder(getSelf(), "starting"));
    }

    /**
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> call(messageBuilder(getSender(), s)))
                .match(Message.class, s -> call(s.getMessage()))
                .build();
    }
}
