package com.wildbeeslabs.jentle.algorithms.matcher;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class IpMatcher {

    private static final Set<String> whiteListedIPs = ImmutableSet.of("127.0.0.1", "0:0:0:0:0:0:0:1");
    private List<String[]> masks = new ArrayList<>();

    public IpMatcher(final String source) {
        String[] parts = source.split("[,;]");
        for (String part : parts) {
            String m = part.trim();
            String[] mask = ipv4(m);
            if (Objects.isNull(mask)) {
                mask = ipv6(m);
                if (Objects.isNull(mask)) {
                    log.warn("Invalid IP mask: '{}'", m);
                    continue;
                }
            }
            masks.add(mask);
        }
    }

    public boolean match(final String ip) {
        if (StringUtils.isBlank(ip) || whiteListedIPs.contains(ip)) {
            return true;
        }
        if (masks.isEmpty()) {
            return true;
        }

        String[] ipv = ipv4(ip);
        if (Objects.isNull(ipv)) {
            ipv = ipv6(ip);
            if (Objects.isNull(ipv)) {
                log.warn("IP format not supported: '{}'", ip);
                return true;
            }
        }

        for (final String[] mask : masks) {
            if (match(mask, ipv))
                return true;
        }
        return false;
    }

    private boolean match(final String[] mask, final String[] ip) {
        if (mask.length != ip.length) {
            return false;
        }
        for (int j = 0; j < mask.length; j++) {
            String mp = mask[j];
            if (!mp.equals("*") && !ip[j].equals(mp)) {
                return false;
            }
        }
        return true;
    }

    private static String[] ipv4(final String ip) {
        final String[] ipp = ip.split("\\.");
        if (ipp.length != 4) {
            return null;
        }
        return ipp;
    }

    private static String[] ipv6(final String ip) {
        final String[] ipp = ip.split(":");
        if (ipp.length != 8) {
            return null;
        }
        return ipp;
    }
}
