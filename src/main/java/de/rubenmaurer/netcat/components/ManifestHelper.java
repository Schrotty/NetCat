package de.rubenmaurer.netcat.components;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestHelper {

    public static ManifestHelper create() {

        return new ManifestHelper();
    }

    public String get(String value) {
        Manifest manifest = new Manifest();
        String result = "";

        try {
            manifest.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
            Attributes atts = manifest.getMainAttributes();

            result = atts.getValue(value);
        }catch (Exception e) {
            System.err.println("Mist");
        }

        return result;
    }
}
