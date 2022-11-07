package me.cometkaizo.util;

import java.time.LocalDateTime;

public interface LogFormatter {

    String VARIABLE_CHAR = "&";

    String format(LocalDateTime now, Class<?> caller, String message, Object... args);

}
