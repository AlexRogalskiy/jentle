package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Shameless copy from Apache commons lang and then modified to keep only the interesting stuff for AssertJ.
 * <p>
 * Contains common code for working with Methods/Constructors, extracted and
 * refactored from <code>MethodUtils</code> when it was imported from Commons
 * BeanUtils.
 */
@UtilityClass
public class MemberUtils {

    private static final int ACCESS_TEST = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    /**
     * XXX Default access superclass workaround
     * <p>
     * When a public class has a default access superclass with public members,
     * these members are accessible. Calling them from compiled code works fine.
     * Unfortunately, on some JVMs, using reflection to invoke these members
     * seems to (wrongly) prevent access even when the modifier is public.
     * Calling setAccessible(true) solves the problem but will only work from
     * sufficiently privileged code. Better workarounds would be gratefully
     * accepted.
     *
     * @param o the AccessibleObject to set as accessible
     */
    static void setAccessibleWorkaround(final AccessibleObject o) {
        if (o == null || o.isAccessible()) {
            return;
        }
        Member m = (Member) o;
        if (Modifier.isPublic(m.getModifiers())
            && isPackageAccess(m.getDeclaringClass().getModifiers())) {
            try {
                o.setAccessible(true);
            } catch (SecurityException e) { // NOPMD
                // ignore in favor of subsequent IllegalAccessException
            }
        }
    }

    /**
     * Returns whether a given set of modifiers implies package access.
     *
     * @param modifiers to test
     * @return true unless package/protected/private modifier detected
     */
    static boolean isPackageAccess(int modifiers) {
        return (modifiers & ACCESS_TEST) == 0;
    }

    /**
     * Returns whether a Member is accessible.
     *
     * @param m Member to check
     * @return true if <code>m</code> is accessible
     */
    static boolean isAccessible(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }

}
