package fuzzy.membership;

import fuzzy.Consequence;
import fuzzy.Utils;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

/**
 * UnionFunction - Union of functions
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class UnionFunction {

    // Integration boundaries
    private static final int MIN_VAL = 0;
    private static final int MAX_VAL = 100;

    private final UnivariateFunction f, g;
    private final UnivariateIntegrator integrator;
    private final int maxEval;

    public UnionFunction(List<Consequence> consequences, UnivariateIntegrator integrator, int maxEval){
        this.g = new Denominator(consequences);
        this.f = new Numerator(this.g);
        this.integrator = integrator;
        this.maxEval = maxEval;

//        Utils.visualizeFunc(this.g);
    }

    public double value() {
        double denominator = integrator.integrate(maxEval, this.g, MIN_VAL, MAX_VAL);
        if(denominator == 0) return 0;

        double numerator = integrator.integrate(maxEval, this.f, MIN_VAL, MAX_VAL);

        return (numerator / denominator);
    }


    // f(x) gives the maximum of all aggregate functions
    private class Denominator implements UnivariateFunction {

        private final List<Consequence> consequences;

        public Denominator(List<Consequence> consequences) {
            this.consequences = consequences;
        }

        @Override
        public double value(double x) {
            double max = 0;
            for(Consequence c: consequences){
                max = Math.max(c.value(x), max);
            }
            return max;
        }
    }

    // f(x) gives x*f(x)
    private class Numerator implements UnivariateFunction {

        private final UnivariateFunction f;

        public Numerator(UnivariateFunction f) {
            this.f = f;
        }

        @Override
        public double value(double x) {
            return x * f.value(x);
        }
    }

}
