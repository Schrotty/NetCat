package de.rubenmaurer.netcat.components;

import org.junit.Assert;
import org.junit.Test;

public class ParameterValidatorTest {

    @Test
    public void validatePortFails() throws Exception {
        Assert.assertEquals(-1, ParameterValidator.validatePort("fail"));
    }

    @Test
    public void validatePortSuccess() throws Exception {
        Assert.assertEquals(6669, ParameterValidator.validatePort("6669"));
    }
}