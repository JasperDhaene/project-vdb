package control;

/**
 * Ratio - ratio calculator
 * @author florian
 */
public class Ratio {

    public static double calc(double left, double right) {
        if(left == 0 && right == 0)
            return 1;

        if(left == 0)
            return Double.MAX_VALUE;

        if(right == 0)
            return -Double.MAX_VALUE;

        return (left / right);
    }

}
