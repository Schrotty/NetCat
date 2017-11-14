package de.rubenmaurer.netcat.util;

import de.rubenmaurer.netcat.NetCat;

public class ParameterParser {
    public static void parse(String[] params) {
        for (String param : params) {
            if (param.equals("-s")) NetCat.setSilentMode(true);
            if (param.equals("-l")) NetCat.setClientMode(false);
            if (param.equals("-uni")) NetCat.setUniMode(false);
            if (param.equals("-udp")) NetCat.setUDPMode(false);
        }
    }
}
