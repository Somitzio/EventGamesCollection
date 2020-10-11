package eventgamescollection.logging;

import java.util.logging.Logger;

/**
 * A class which provides easy and basic access to the logger.
 */
public class Log {
    private static Logger logger = null;

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Log.logger = logger;
    }

    public static void info(String message) {
        getLogger().info(message);
    }

    public static void warning(String message) {
        getLogger().warning(message);
    }
}
