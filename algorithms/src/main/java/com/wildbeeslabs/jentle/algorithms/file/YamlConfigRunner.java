package com.wildbeeslabs.jentle.algorithms.file;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.Configuration;
import java.nio.file.Files;
import java.nio.file.Paths;

public class YamlConfigRunner {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: <file.yml>");
            return;
        }
        final Yaml yaml = new Yaml();
        try (final InputStream in = Files.newInputStream(Paths.get(args[0]))) {
            final Configuration config = yaml.loadAs(in, Configuration.class);
            System.out.println(config.toString());
        }
    }
}
