package de.rubenmaurer.netcat.components;

import akka.actor.AbstractActor;
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
     * Receives a message an process it.
     * After processing the actor stops itself.
     *
     * @return a Receive object
     */
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> System.out.println(String.format("[INFO] %s: %s", getSender().toString(), s)))
                .build();
    }
}
