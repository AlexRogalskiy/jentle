package com.wildbeeslabs.jentle.algorithms.utils;

import ch.qos.logback.classic.Level;

import java.util.ArrayList;
import java.util.List;

public class LoggingHelper {

    public static List<Level> getLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(Level.ALL);
        levels.add(Level.TRACE);
        levels.add(Level.DEBUG);
        levels.add(Level.INFO);
        levels.add(Level.WARN);
        levels.add(Level.ERROR);
        levels.add(Level.OFF);
        return levels;
    }

    public static Level getLevelFromString(String levelString) {
        for (Level logLevel : getLevels()) {
            if (logLevel.toString().equalsIgnoreCase(levelString)) {
                return logLevel;
            }
        }
        return null;
    }
}
