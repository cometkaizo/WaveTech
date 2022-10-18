package me.cometkaizo.util;

@SuppressWarnings("unused")
public abstract class MathUtils {

    public static final int BINARY = 2;
    public static final int DECIMAL = 10;
    public static final int HEXADECIMAL = 16;



    /**
     * Adds (or subtracts) an amount, but whenever the number goes outside the boundaries it loops back to the other side
     * <br><br>
     * <blockquote>
     * For example:
     * <br>
     * loopAdd(5, 5, 2, 7) would return 5 because adding 2 to 5 makes 7, which is the ceiling, so the remaining 3 to add loops back to the floor and gives 2 + 3
     * </blockquote>
     * @param var the number to add to
     * @param amt the number to add
     * @param floor the lower boundary
     * @param ceiling the upper boundary
     * @return var + amt if it is within floor and ceiling, otherwise it loops until the operation is done
     */
    public static double loopAdd(double var, double amt, double floor, double ceiling) {

        double distance = Math.max(floor, ceiling) - Math.min(floor, ceiling);
        if (distance == 0) {
            return floor;
        }

        if (isInRange(var + amt, floor, ceiling))
            return var + amt;

        double result = var + amt;
        if (result > 0) {
            while (!isInRange(result, floor, ceiling)) {
                result -= distance;
            }
        } else if (result < 0) {
            while (!isInRange(result, floor, ceiling)) {
                result += distance;
            }
        }
        return result - 1;
    }

    /**
     * Checks whether a number is within two other numbers (inclusive)
     * @param var the number to check
     * @param floor the lower boundary
     * @param ceiling the upper boundary
     * @return {@code true} if the number is within the two boundaries, otherwise {@code false}
     */
    public static boolean isInRange(double var, double floor, double ceiling) {
        return var >= floor && var <= ceiling;
    }

    public static double clamp(double var, double floor, double ceiling) {

        if (var <= floor)
            return floor;
        return Math.min(var, ceiling);

    }

    public static double alternate(double var, double first, double second) {
        if (var == first)
            return second;
        return first;
    }

    public static int alternate(double var) {
        if (var == 0)
            return 1;
        return 0;
    }
    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty())
            return false;
        // for each character in the string
        for (int i = 0; i < s.length(); i++) {
            // if the first character is a hyphen and is not the only character then continue
            if (i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1)
                    return false;
                else continue;
            }
            // if this non-hyphen character is a digit
            if(Character.digit(s.charAt(i), radix) < 0)
                return false;
        }
        return true;
    }
}
