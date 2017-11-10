package de.rubenmaurer.netcat.core.interfaces;

public interface AbstractSocket {
    String receive(int maxBytes);

    void send(String message);
}
