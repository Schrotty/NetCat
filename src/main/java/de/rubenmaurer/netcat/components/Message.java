package de.rubenmaurer.netcat.components;

/**
 * The type Message.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class Message {

    /**
     * The Message.
     */
    private String message;

    /**
     * The sender name.
     */
    private String sender;

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Create message.
     *
     * @param msg the msg
     * @param sender the sender name
     * @return the message
     */
    public static Message create(String msg, String sender) {
        return new Message(msg, sender);
    }

    /**
     * Instantiates a new Message.
     *
     * @param msg the msg
     * @param sender the sender name
     */
    private Message(String msg, String sender) {
        this.message = msg;
        this.sender = sender;
    }
}
