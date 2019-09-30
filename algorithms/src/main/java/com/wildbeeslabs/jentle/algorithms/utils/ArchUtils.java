package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.lowerCase;

@UtilityClass
public class ArchUtils {

    public static String normalize(String archName) {
        if (StringUtils.isBlank(archName)) {
            throw new IllegalStateException("No architecture detected");
        }

        String arch = lowerCase(archName).replaceAll("[^a-z0-9]+", "");

        if (arch.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return "x86_64";
        }
        if (arch.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return "x86_32";
        }
        if (arch.matches("^(ia64w?|itanium64)$")) {
            return "itanium_64";
        }
        if ("ia64n".equals(arch)) {
            return "itanium_32";
        }
        if (arch.matches("^(sparcv9|sparc64)$")) {
            return "sparc_64";
        }
        if (arch.matches("^(sparc|sparc32)$")) {
            return "sparc_32";
        }
        if (arch.matches("^(aarch64|armv8|arm64).*$")) {
            return "arm_64";
        }
        if (arch.matches("^(arm|arm32).*$")) {
            return "arm_32";
        }
        if (arch.matches("^(mips|mips32)$")) {
            return "mips_32";
        }
        if (arch.matches("^(mipsel|mips32el)$")) {
            return "mipsel_32";
        }
        if ("mips64".equals(arch)) {
            return "mips_64";
        }
        if ("mips64el".equals(arch)) {
            return "mipsel_64";
        }
        if (arch.matches("^(ppc|ppc32)$")) {
            return "ppc_32";
        }
        if (arch.matches("^(ppcle|ppc32le)$")) {
            return "ppcle_32";
        }
        if ("ppc64".equals(arch)) {
            return "ppc_64";
        }
        if ("ppc64le".equals(arch)) {
            return "ppcle_64";
        }
        if ("s390".equals(arch)) {
            return "s390_32";
        }
        if ("s390x".equals(arch)) {
            return "s390_64";
        }

        throw new IllegalStateException("Unsupported architecture: " + archName);
    }
}
