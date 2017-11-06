package de.rubenmaurer.netcat.components;

import org.junit.Assert;

import java.util.Objects;

public class MessageTest {

    @org.junit.Test
    public void getMessage() throws Exception {
        Assert.assertTrue(Message.create("Schrottler").getMessage().equals("Schrottler"));
    }

    @org.junit.Test
    public void create() throws Exception {
        Assert.assertTrue(!Objects.equals(Message.create(""), null));
    }

}
