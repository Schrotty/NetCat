package de.rubenmaurer.netcat.core.interfaces;

/**
 * <p>AbstractSocket interface.</p>
 *
 * @author ruben
 * @version $Id: $Id
 */
public interface AbstractSocket {
    /**
     * <p>receive.</p>
     *
     * @param maxBytes a int.
     * @return a {@link java.lang.String} object.
     */
    String receive(int maxBytes);

    /**
     * <p>send.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    void send(String message);
}
