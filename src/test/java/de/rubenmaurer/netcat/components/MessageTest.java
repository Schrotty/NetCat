package de.rubenmaurer.netcat.components;

import org.junit.Assert;

import java.util.Objects;

/**
 * The type Message test.
 */
public class MessageTest {

    /**
     * Gets message.
     *
     * @throws Exception the exception
     */
    @org.junit.Test
    public void getMessage() throws Exception {
        Assert.assertTrue(Message.create("Schrottler").getMessage().equals("Schrottler"));
    }

    /**
     * Create.
     *
     * @throws Exception the exception
     */
    @org.junit.Test
    public void create() throws Exception {
        Assert.assertTrue(!Objects.equals(Message.create(""), null));
    }

}
