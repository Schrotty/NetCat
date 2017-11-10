package de.rubenmaurer.netcat.core.reporter;

/**
 * The type Report.
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class Report {

    public enum Type {
        INFO,
        ERROR,
        ONLINE,
        OFFLINE
    }

    /**
     * The Report.
     */
    private Type type;

    /**
     * The sender name.
     */
    private String message;

    /**
     * Gets type.
     *
     * @return the message
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets message.
     *
     * @return the sender
     */
    public String getMessage() {
        return message;
    }

    /**
     * Create message.
     *
     * @param type the msg
     * @param message the sender name
     * @return the message
     */
    public static Report create(Type type, String message) {
        return new Report(type, message);
    }

    /**
     * Create message.
     *
     * @param type the msg
     * @return the message
     */
    public static Report create(Type type) {
        return new Report(type);
    }

    /**
     * Instantiates a new Report.
     *
     * @param type the msg
     */
    private Report(Type type) {
        this.type = type;
    }

    /**
     * Instantiates a new Report.
     *
     * @param type the msg
     * @param message the sender name
     */
    private Report(Type type, String message) {
        this(type);
        this.message = message;
    }
}
