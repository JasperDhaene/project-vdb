package fuzzy.membership;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * PiFunction - Small PI-function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class PiFunction implements UnivariateFunction {

    private int beta, gamma;
    private UnivariateFunction sf1, sf2;

    public PiFunction(int beta, int gamma) {
        this.beta = beta;
        this.gamma = gamma;
        sf1 = new SFunction((gamma - beta), gamma);
        sf2 = new SFunction(gamma, (gamma + beta));
    }

    @Override
    public double value(double x) {
        if(x < gamma) {
            return sf1.value(x);
        } else {
            return (1 - sf2.value(x));
        }
    }

}
