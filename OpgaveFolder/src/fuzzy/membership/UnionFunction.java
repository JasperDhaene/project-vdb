package fuzzy.membership;

import fuzzy.Consequence;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

/**
 * UnionFunction - Union of functions
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class UnionFunction {

    // Maximum number of function evaluations
    public static final int MAX_EVAL = 10000;

    // Integration boundaries
    public static final int MIN_VAL = -100;
    public static final int MAX_VAL = 100;

    private final UnivariateFunction f, g;
    private final UnivariateIntegrator integrator;

    public UnionFunction(List<Consequence> consequences, UnivariateIntegrator integrator){
        this.g = new Denominator(consequences);
        this.f = new Numerator(this.g);
        this.integrator = integrator;
    }

    public double value() {
        double numerator = integrator.integrate(MAX_EVAL, this.f, MIN_VAL, MAX_VAL);
        double denominator = integrator.integrate(MAX_EVAL, this.g, MIN_VAL, MAX_VAL);

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
            double max = Double.MIN_VALUE;
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
