package de.rubenmaurer.netcat.core.sockets;

import akka.actor.ActorRef;
import de.rubenmaurer.netcat.NetCat;
import de.rubenmaurer.netcat.core.Guardian;
import de.rubenmaurer.netcat.core.interfaces.AbstractSocket;
import de.rubenmaurer.netcat.core.reporter.Report;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocket implements AbstractSocket {

    private ServerSocket serverSocket;
    private BufferedReader input;

    private Socket socket;
    private PrintWriter output;

    private TCPSocket(String hostname, int port) throws IOException {
        if (!NetCat.isClient()) {
            serverSocket = new ServerSocket(port);
            return;
        }

        socket = new Socket(hostname, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void connect() throws IOException {
        socket = serverSocket.accept();
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Wait for receiving a message.
     *
     * @param maxBytes max bytes of the incoming message
     * @return a {@link String} object.
     */
    public String receive(int maxBytes) {
        String val = null;
        StringBuilder builder = new StringBuilder();

        try {
            if (socket == null) connect();

            while (val == null) {
                val = input.readLine();
            }

            builder.append(val);
        } catch (Exception exception) {
            Guardian.getReporter().tell(Report.create(Report.Type.ERROR, exception.getMessage()), ActorRef.noSender());
        }

        return builder.toString();
    }

    /**
     * Send a given message.
     *
     * @param message the given message
     */
    public void send(String message) {
        try {
            if (output == null) output = new PrintWriter(socket.getOutputStream(), true);
            output.println(message);
        } catch (Exception exception) {
            Guardian.getReporter().tell(Report.create(Report.Type.ERROR, exception.getMessage()), ActorRef.noSender());
        }
    }

    public static AbstractSocket createSocket(String host, int port) throws IOException {
        return new TCPSocket(host, port);
    }
}
