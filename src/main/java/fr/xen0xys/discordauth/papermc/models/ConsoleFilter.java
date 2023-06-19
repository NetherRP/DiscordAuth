package fr.xen0xys.discordauth.papermc.models;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.NotNull;

public class ConsoleFilter extends AbstractFilter {

    @Override
    public Result filter(@NotNull final LogEvent event) {
        return isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(@NotNull final Logger logger, @NotNull final Level level, @NotNull final Marker marker, @NotNull final Message msg, @NotNull final Throwable t) {
        return isLoggable(msg.getFormattedMessage());
    }

    @Override
    public Result filter(@NotNull final Logger logger, @NotNull final Level level, @NotNull final Marker marker, @NotNull final String msg, @NotNull final Object... params) {
        return isLoggable(msg);
    }

    @Override
    public Result filter(@NotNull final Logger logger, @NotNull final Level level, @NotNull final Marker marker, @NotNull final Object msg, @NotNull final Throwable t) {
        return isLoggable(msg.toString());
    }

    private Result isLoggable(@NotNull final String msg) {
        if (msg.contains("issued server command:"))
            if (msg.contains("/login") || msg.contains("/l") || msg.contains("/account"))
                return Result.DENY;
        else if(msg.contains("was kicked for floating too long!"))
            return Result.DENY;
        return Result.NEUTRAL;
    }
}