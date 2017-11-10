package de.rubenmaurer.netcat.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * The type Parameter validator test.
 */
public class ParameterValidatorTest {

    /**
     * Validate port fails.
     *
     * @throws Exception the exception
     */
    @Test
    public void validatePortFails() throws Exception {
        Assert.assertEquals(-1, ParameterValidator.validatePort("fail"));
    }

    /**
     * Validate port success.
     *
     * @throws Exception the exception
     */
    @Test
    public void validatePortSuccess() throws Exception {
        Assert.assertEquals(6669, ParameterValidator.validatePort("6669"));
    }
}