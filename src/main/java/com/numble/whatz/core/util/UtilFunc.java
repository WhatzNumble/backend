package com.numble.whatz.core.util;

public class UtilFunc {
    public static <T> void checkNull(T obj) {
        if (obj == null)
            throw new RuntimeException();
    }
}
