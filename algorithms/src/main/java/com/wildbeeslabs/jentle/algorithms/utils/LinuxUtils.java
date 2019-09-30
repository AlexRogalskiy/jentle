package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@UtilityClass
public final class LinuxUtils {

    private static final Logger logger = LoggerFactory.getLogger(LinuxUtils.class);

    private static final String DISTRIBUTION_NAME = resolveDistributionName();

    public static String getDistributionName() {
        return DISTRIBUTION_NAME;
    }

    private static String resolveDistributionName() {
        if (!SystemUtils.IS_OS_LINUX) {
            return null;
        }

        try {
            Path target;
            try (InputStream source = LinuxUtils.class.getResourceAsStream("/sh/detect_linux_distribution.sh")) {
                target = Files.createTempFile("detect_linux_distribution_", ".sh");
                Files.copy(source, target, REPLACE_EXISTING);
            }

            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", target.toFile().getAbsolutePath());

            Process process = builder.start();
            process.waitFor();

            if (process.exitValue() != 0) {
                throw new IOException("Execution of the script to detect the Linux distribution failed with error code: '" + process.exitValue() + "'");
            }

            String distributionName;
            try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))) {
                distributionName = outputReader.readLine();
            }

            if (StringUtils.isBlank(distributionName)) {
                logger.warn("It's not possible to detect the name of the Linux distribution, the detection script returned empty output");
                return null;
            }

            if (distributionName.startsWith("Debian")) {
                distributionName = "Debian";
            }
            if (distributionName.equals("openSUSE project")) {
                distributionName = "openSUSE";
            }

            return distributionName;
        } catch (Exception e) {
            logger.error("It's not possible to detect the name of the Linux distribution", e);
            return null;
        }
    }
}
