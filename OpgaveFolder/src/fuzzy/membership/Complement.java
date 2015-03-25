package fuzzy.membership;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Complement implements UnivariateFunction {

    @Override
    public double value(double x) {
        return (1 - x);
    }

}
