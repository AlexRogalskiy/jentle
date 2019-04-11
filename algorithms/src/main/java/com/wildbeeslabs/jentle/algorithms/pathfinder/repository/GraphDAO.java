package com.wildbeeslabs.jentle.algorithms.pathfinder.repository;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class GraphDAO {

    public List<String> listLocations() {
        return Lists.newArrayList(
            "CNHKG", "AUMEL", "SESTO", "FIHEL", "USCHI", "JNTKO", "DEHAM", "CNSHA", "NLRTM", "SEGOT", "CNHGH", "USNYC", "USDAL"
        );
    }

    public String getVoyageNumber(final String from, final String to) {
        final int i = RandomUtils.nextInt(0, 5);
        if (i == 0) return "0100S";
        if (i == 1) return "0200T";
        if (i == 2) return "0300A";
        if (i == 3) return "0301S";
        return "0400S";
    }
}
