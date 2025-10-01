package com.hrms.app.util;

import com.hrms.app.config.ValidationError;

public final class Validator {

    private Validator() {
    }

    public static void requiresNonNull(final Object obj, final String name) {
        if (obj == null) {
            throw new ValidationError(name + " must not be null");
        }
    }

    public static boolean isNull(final Object obj) {
        return obj == null;
    }

    public static boolean isNonNull(final Object obj) {
        return obj != null;
    }
}
