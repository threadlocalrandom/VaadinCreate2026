package org.tlr;

import java.time.LocalTime;

/**
 * Simple service for generating greeting messages.
 */
public class GreetService {

    /**
     * Returns a greeting message based on the current time of day.
     *
     * @param name the name to greet
     * @return a personalized greeting message
     */
    public String greet(String name) {
        LocalTime now = LocalTime.now();
        String greeting;

        if (now.getHour() < 12) {
            greeting = "Good morning";
        } else if (now.getHour() < 18) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }

        if (name != null && !name.isEmpty()) {
            return greeting + ", " + name + "!";
        }
        return greeting + "!";
    }
}
