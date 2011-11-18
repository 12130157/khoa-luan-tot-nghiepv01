package uit.cnpm02.dkhp.access.JDBC;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public final class LangUtils extends StringUtils {

    /**
     * @param boolValue The bool string ("true" or ...)
     * @return parsed boolean value, if value is "true" then true is returned else false.
     */
    public static boolean parBoolean(String boolValue) {
        if (StringUtils.isEmpty(boolValue)) {
            return false;
        }
        return boolValue.equalsIgnoreCase("true");
    }

    /**
     * @param lst The list to check.
     * @return if the list if empty or null.
     */
    public static boolean isEmpty(List<?> lst) {
        return (lst == null || lst.size() < 1);
    }

    /**
     * 
     * @param lst The set to check.
     * @return if the set if empty or null.
     */
    public static boolean isEmpty(Set<?> lst) {
        return (lst == null || lst.size() < 1);
    }

    /**
     * Get long value from a simple multiply expression, for example: "3*5*10" -> 150
     * @param expr The expression.
     * @return parsed value.
     */
    public static long getLongFromMultiplyExpression(String expr) {
        StringTokenizer st = new StringTokenizer(expr, "* ");
        if (st.countTokens() <= 1) {
            return Long.valueOf(st.nextToken());
        }
        long result = 1;
        while (st.hasMoreTokens()) {
            result *= Long.parseLong(st.nextToken());
        }
        return result;
    }

    /**
     * Get long value from a simple multiply expression, for example: "3*5*10" -> 150
     * @param expr The expression.
     * @return parsed value.
     */
    public static int getIntFromMultiplyExpression(String expr) {
        StringTokenizer st = new StringTokenizer(expr, "* ");
        if (st.countTokens() <= 1) {
            return Integer.valueOf(st.nextToken());
        }
        int result = 1;
        while (st.hasMoreTokens()) {
            result *= Integer.parseInt(st.nextToken());
        }
        return result;
    }

    /**
     * @param input The input string which to be split.
     * @param delimiter The delimiter to split the input.
     * @param trim The flag to know if the outputs should be trimmed.
     * @return split strings.
     */
    public static String[] splitString(final String input, String delimiter, boolean trim) {
        if (trim) {
            delimiter += " ";
        }
        StringTokenizer st = new StringTokenizer(input, delimiter);
        if (st.countTokens() <= 1) {
            return new String[]{input};
        }
        final String[] result = new String[st.countTokens()];
        int count = 0;
        while (st.hasMoreTokens()) {
            result[count++] = st.nextToken();
        }
        return result;
    }

    /**
     * @param input The input string which to be split.
     * @param delimitors The delimiter to split the input.
     * @return split strings.
     */
    public static String[] splitString(final String input, String delimitors) {
        return splitString(input, delimitors, true);
    }

    /**
     * Bind the given message's substitution locations with the given string value.
     * 
     * @param message the message to be manipulated
     * @param binding the object to be inserted into the message
     * @return the manipulated String
     * @throws IllegalArgumentException if the text appearing within curly braces in the given message does not map to an integer 
     */
    public static String bind(String message, Object binding) {
        return internalBind(message, null, String.valueOf(binding), null);
    }

    /**
     * Bind the given message's substitution locations with the given string values.
     * 
     * @param message the message to be manipulated
     * @param binding1 An object to be inserted into the message
     * @param binding2 A second object to be inserted into the message
     * @return the manipulated String
     * @throws IllegalArgumentException if the text appearing within curly braces in the given message does not map to an integer
     */
    public static String bind(String message, Object binding1, Object binding2) {
        return internalBind(message, null, String.valueOf(binding1), String.valueOf(binding2));
    }

    /**
     * Bind the given message's substitution locations with the given string values.
     * 
     * @param message the message to be manipulated
     * @param bindings An array of objects to be inserted into the message
     * @return the manipulated String
     * @throws IllegalArgumentException if the text appearing within curly braces in the given message does not map to an integer
     */
    public static String bind(String message, Object[] bindings) {
        return internalBind(message, bindings, null, null);
    }
    private static final Object[] EMPTY_ARGS = new Object[0];
    /*
     * Perform the string substitution on the given message with the specified args.
     * See the class comment for exact details.
     */

    private static String internalBind(String message, Object[] args, String argZero, String argOne) {
        if (message == null) {
            return "No message available."; //$NON-NLS-1$
        }
        if (args == null || args.length == 0) {
            args = EMPTY_ARGS;
        }

        int length = message.length();
        //estimate correct size of string buffer to avoid growth
        int bufLen = length + (args.length * 5);
        if (argZero != null) {
            bufLen += argZero.length() - 3;
        }
        if (argOne != null) {
            bufLen += argOne.length() - 3;
        }
        StringBuffer buffer = new StringBuffer(bufLen < 0 ? 0 : bufLen);
        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            switch (c) {
                case '{':
                    int index = message.indexOf('}', i);
                    // if we don't have a matching closing brace then...
                    if (index == -1) {
                        buffer.append(c);
                        break;
                    }
                    i++;
                    if (i >= length) {
                        buffer.append(c);
                        break;
                    }
                    // look for a substitution
                    int number = -1;
                    try {
                        number = Integer.parseInt(message.substring(i, index));
                    } catch (NumberFormatException e) {
                        throw (IllegalArgumentException) new IllegalArgumentException().initCause(e);
                    }
                    if (number == 0 && argZero != null) {
                        buffer.append(argZero);
                    } else if (number == 1 && argOne != null) {
                        buffer.append(argOne);
                    } else {
                        if (number >= args.length || number < 0) {
                            buffer.append("<missing argument>"); //$NON-NLS-1$
                            i = index;
                            break;
                        }
                        buffer.append(args[number]);
                    }
                    i = index;
                    break;
                case '\'':
                    // if a single quote is the last char on the line then skip it
                    int nextIndex = i + 1;
                    if (nextIndex >= length) {
                        buffer.append(c);
                        break;
                    }
                    char next = message.charAt(nextIndex);
                    // if the next char is another single quote then write out one
                    if (next == '\'') {
                        i++;
                        buffer.append(c);
                        break;
                    }
                    // otherwise we want to read until we get to the next single quote
                    index = message.indexOf('\'', nextIndex);
                    // if there are no more in the string, then skip it
                    if (index == -1) {
                        buffer.append(c);
                        break;
                    }
                    // otherwise write out the chars inside the quotes
                    buffer.append(message.substring(nextIndex, index));
                    i = index;
                    break;
                default:
                    buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * Assert if an object is null.
     * @param o object to be asserted.
     */
    public static void assertNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }
}
