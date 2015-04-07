package fuzzy;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Utils - various utilities
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Utils {

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
        for(int i = 0; i < 100; i+=3) {
            System.out.print(String.format("f(%3d) = %3.1f |", i, f.value(i)));
            for(int j = 0; j < (100*f.value(i)); j++)
                System.out.print("#");
            System.out.println();
        }
    }
}
