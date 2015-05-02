package fuzzy;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Utils - various utilities
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Utils {

    private static long time;

    public static String visMem(double membership) {
        if(membership == 0) {
            return "-----";
        } else if(membership <= 0.2) {
            return "--#--";
        } else if(membership <= 0.4) {
            return "--##-";
        } else if(membership <= 0.6) {
            return "-###-";
        } else if(membership <= 0.8) {
            return "-####";
        } else return "#####";
    }

    public static void visualizeFunc(UnivariateFunction f) {
        visualizeFunc(f, 0, 100, 3);
    }

    public static void visualizeFunc(UnivariateFunction f, double lower, double upper, double step) {
        for(double i = lower; i < upper; i += step) {
            System.out.print(String.format("f(%3f) = %3.1f\t|", i, f.value(i)));
            for(int j = 0; j < (100*f.value(i)); j++)
                System.out.print("#");
            System.out.println();
        }
    }

    public static void tic() {
        Utils.time = System.currentTimeMillis();
    }

    public static void toc() {
        if (Utils.time == 0)
            return;

        System.out.println("Elapsed time: " + String.format("%f ms", ((double) (System.currentTimeMillis() - Utils.time))));
        Utils.time = 0;
    }
}
