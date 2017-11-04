package de.rubenmaurer.netcat.components;

public class Message {
    private String message;

    public String getMessage() {
        return message;
    }

    public static Message create(String msg) {
        return new Message(msg);
    }

    private Message(String msg) {
        this.message = msg;
    }
}
