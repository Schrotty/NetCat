package de.rubenmaurer.netcat.core.mirror;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.core.Notice;
import de.rubenmaurer.netcat.core.reporter.Report;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Experimental feature for mirroring messages from multiple hosts.
 * Nearly untested and highly experimental.
 * Naming of incoming connections breaks after 24 connections.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.3
 */
public class Mirror extends AbstractActor {

    /**
     * Current name index.
     */
    private int nameIndex = 0;

    /**
     * Used connection names.
     */
    private List<String> usedName = new ArrayList<>();

    /**
     * The TCP manager.
     */
    private final ActorRef manager;

    /**
     * The used port.
     */
    private final int port;

    /**
     * Create new mirror.
     *
     * @param manager the tcp manager
     * @param port the port
     */
    public Mirror(ActorRef manager, int port) {
        this.manager = manager;
        this.port = port;
    }

    /**
     * Get the actors props.
     *
     * @param manager the tcp manager
     * @param port the port
     * @return the props
     */
    public static Props props(ActorRef manager, int port) {
        return Props.create(Mirror.class, manager, port);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        Guardian.getReporter().tell(Report.create(Report.Type.ONLINE), self());

        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), new InetSocketAddress("localhost", port), 100), getSelf());
        context().parent().tell(Notice.READY, self());
    }

    /**
     * Gets fired after stop.
     */
    @Override
    public void postStop() {
        Guardian.getReporter().tell(Report.create(Report.Type.OFFLINE), self());
    }

    /**
     * Process incoming messages.
     *
     * @return a receive
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Bound.class, msg -> manager.tell(msg, getSelf()))
                .match(Tcp.CommandFailed.class, msg -> getContext().stop(getSelf()))
                .match(Tcp.Connected.class, conn -> {
                    manager.tell(conn, getSelf());
                    final ActorRef handler = getContext().actorOf(MessageHandler.props(), getName(nameIndex));
                    getSender().tell(TcpMessage.register(handler), getSelf());
                })
                .build();
    }

    /**
     * Get a connection name.
     *
     * @param index the index to use
     * @return the connection name
     */
    private String getName(int index) {
        if (index < 25) {
            String[] names = new String[]{
                    "Doctor",
                    "Amy",
                    "Rory",
                    "River",
                    "Rose",
                    "Martha",
                    "Donna",
                    "John",
                    "Rodney",
                    "Jack",
                    "Daniel",
                    "Tealc",
                    "Samantha",
                    "Root",
                    "Schrotty",
                    "Hugo",
                    "/dev/null",
                    "Herman",
                    "Dieter",
                    "Schrottler",
                    "3-Tears",
                    "Charlie",
                    "Danny",
                    "JDog",
                    "Jensen"
            };

            for (String name : usedName) {
                if (names[index].equals(name)) return getName(index + 1);
            }

            nameIndex = new Random().nextInt(24);
            usedName.add(names[index]);
            return names[index];
        }

        return "";
    }
}
