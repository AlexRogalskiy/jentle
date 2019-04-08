package com.wildbeeslabs.jentle.algorithms.pathfinder.repository;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class GraphDAO {

    /**
     * Default {@link Random} generator
     */
    private static final Random random = new Random();

    public List<String> listLocations() {
        return Lists.newArrayList(
            "CNHKG", "AUMEL", "SESTO", "FIHEL", "USCHI", "JNTKO", "DEHAM", "CNSHA", "NLRTM", "SEGOT", "CNHGH", "USNYC", "USDAL"
        );
    }

    public String getVoyageNumber(final String from, final String to) {
        final int i = this.random.nextInt(5);
        if (i == 0) return "0100S";
        if (i == 1) return "0200T";
        if (i == 2) return "0300A";
        if (i == 3) return "0301S";
        return "0400S";
    }
}
