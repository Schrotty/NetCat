package de.rubenmaurer.netcat.components;

/**
 * The type Message.
 *
 * @author ruben
 * @version $Id: $Id
 */
public class Message {
    /**
     * The Message.
     */
    private String message;

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Create message.
     *
     * @param msg the msg
     * @return the message
     */
    public static Message create(String msg) {
        return new Message(msg);
    }

    /**
     * Instantiates a new Message.
     *
     * @param msg the msg
     */
    private Message(String msg) {
        this.message = msg;
    }
}
