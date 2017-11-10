package de.rubenmaurer.netcat.util;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * <p>ManifestHelper class.</p>
 *
 * @author Ruben 'Schrotty' Maurer
 * @version $Id: $Id
 */
public class ManifestHelper {

    /**
     * <p>Create new ManifestHelper.</p>
     *
     * @return a {@link de.rubenmaurer.netcat.util.ManifestHelper} object.
     */
    public static ManifestHelper create() {
        return new ManifestHelper();
    }

    /**
     * <p>Get value from manifest.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String get(String value) {
        Manifest manifest = new Manifest();
        String result = "";

        try {
            manifest.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
            Attributes atts = manifest.getMainAttributes();

            String val = atts.getValue(value);
            result = val != null ? val : "";
        }catch (Exception e) {
            System.err.println("Mist");
        }

        return result;
    }
}
