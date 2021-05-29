package common;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Utilities is a general class that contains static methods that can be called within the application
 * to mainly carry out repetitive tasks.
 */

public class Utilities {
    /**
     *
     * @param c Class where the properties are being called from
     * @param filename the filename containing the property file
     * @param name name of the property to be returned
     * @return the value of named property
     * @throws IOException
     */
    public static String getProperty(Class c, String filename, String name) throws IOException  {
        // Will return the property value or null within a properties file.

        String value = "";

        try {
            Properties prop = new Properties();
            InputStream stream = c.getResourceAsStream("/" + filename);
            prop.load(stream);
            value = prop.getProperty(name);
        } catch (IOException e) {
            value = null;
        }
        return value;
    }

    /**
     * Used to provide a  custom log file record entry format
     */
    public static class CustomFormatter extends Formatter {
        private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

        @Override
        public String format(final LogRecord record) {
            return String.format("%1$-10s %2$s %3$s %4$s %5$s %6$s\n",
                    "Thread " + record.getThreadID(),
                    record.getSourceClassName(),
                    record.getSourceMethodName(),
                    new Date(record.getMillis()),
                    record.getLevel().getName(),
                    record.getMessage(), formatMessage(record));
        }
    }

    /**
     * used to check if a string representation of a number is a valid number
     * @param strNum string representing a number
     * @return boolean - success or failure
     */
    public static boolean isNumeric(String strNum) {

        if (strNum == null) {
            return false;
        }

        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param date string representing a date
     * @return boolean - success or failure
     */
    public static boolean isValidDatetime(final String date) {

        boolean valid = false;

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    /**
     *
     * @param timestamp string representing a Timestamp
     * @return boolean - success or failure
     */
    public static boolean isValidTimestamp(final String timestamp) {

        boolean valid = false;

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(timestamp,
                    DateTimeFormatter.ofPattern("uuuu-M-d HH:mm:ss")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    /**
     * Used to check if String is null or empty the string is trimmed
     * to remove and white space.
     * @param str string to be validated as not being NULL oe Empty
     * @return boolean - success or failure
     */
    public static boolean isNullOrEmpty(String str) {
        // Used to check if String is null or empty
        // the string is trimmed to remove and white space.

        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }
}