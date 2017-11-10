package de.rubenmaurer.netcat.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * The type Manifest helper test.
 */
public class ManifestHelperTest {

    /**
     * Create.
     *
     * @throws Exception the exception
     */
    @Test
    public void create() throws Exception {
        Assert.assertTrue(ManifestHelper.create() != null);
    }

    /**
     * Get.
     *
     * @throws Exception the exception
     */
    @Test
    public void get() throws Exception {
        Assert.assertEquals(ManifestHelper.create().get("nothing"), "");
    }
}