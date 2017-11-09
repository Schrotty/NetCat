package de.rubenmaurer.netcat.components;

import org.junit.Test;

/**
 * The type Udp socket test.
 */
public class UDPSocketTest {

    /**
     * Create socket success.
     *
     * @throws Exception the exception
     */
    @Test
    public void createSocketSuccess() throws Exception {
        //Assert.assertTrue(UDPSocket.createSocket(new InetSocketAddress(0)) != null);
    }

    /**
     * Create socket failure.
     *
     * @throws Exception the exception
     */
    @Test
    public void createSocketFailure() throws Exception {
        //UDPSocket.createSocket(new InetSocketAddress("localhost", 6665));
        //Assert.assertEquals(UDPSocket.createSocket(new InetSocketAddress("localhost", 6665)), null); //will fail
    }

    /**
     * Send.
     *
     * @throws Exception the exception
     */
    @Test
    public void send() throws Exception {
        //UDPSocket.createSocket(new InetSocketAddress(37069)).send("Hello World!");
    }

    /**
     * Receive.
     *
     * @throws Exception the exception
     */
    @Test
    public void receive() throws Exception {
        //
    }
}