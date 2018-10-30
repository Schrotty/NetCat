package de.rubenmaurer.netcat.util;

import de.rubenmaurer.netcat.core.reporter.Report;
import org.junit.Assert;

import java.util.Objects;

/**
 * The type Report test.
 */
public class ReportTest {

    /**
     * Gets message.
     *
     * @throws Exception the exception
     */
    @org.junit.Test
    public void getMessage() throws Exception {
        Assert.assertTrue(Report.create(Report.Type.INFO, "").getType().equals(Report.Type.INFO));
    }

    /**
     * Create.
     *
     * @throws Exception the exception
     */
    @org.junit.Test
    public void create() throws Exception {
        Assert.assertTrue(!Objects.equals(Report.create(Report.Type.INFO, ""), null));
    }

}
