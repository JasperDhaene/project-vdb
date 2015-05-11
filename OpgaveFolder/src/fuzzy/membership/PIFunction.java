package fuzzy.membership;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Identity;

/**
 * PIFunction - Large PI-function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class PIFunction implements UnivariateFunction {

    /**
     * eps - Arbitrary small number. See below.
     * 
     */
    public final double eps = Math.pow(10, -10);

    private final double alpha, beta, gamma, delta;
    private final UnivariateFunction pi1, pi3;

    public PIFunction(double alpha, double beta, double gamma, double delta, UnivariateFunction pi1, UnivariateFunction pi3) {
        if(alpha > beta || beta > gamma || gamma > delta)
            throw new RuntimeException(new IllegalArgumentException("Arguments must be serially smaller than or equal to each other."));

        /**
         * FP-math doesn't like it when alpha == beta or gamma == delta.
         * Apply a small fix to make sure the functions are rendered correctly.
         */
        this.alpha = (alpha == beta ? (alpha - eps) : alpha);
        this.beta = beta;
        this.gamma = gamma;
        this.delta = (gamma == delta ? (delta + eps) : delta);

        System.out.println(String.format("%f, %f, %f, %f", this.alpha, this.beta, this.gamma, this.delta));

        // pi1, pi3 validity testing ommitted

        this.pi1 = pi1;
        this.pi3 = pi3;
    }

    @Override
    public double value(double x) {
        if(x < alpha) {
            // alpha == inf(U) ommitted
            if(alpha == beta) return 1;
            else if(alpha < beta) return 0;
        } else if(x >= alpha && x < beta && alpha < beta) {
            return pi1.value((x - alpha) / (beta - alpha));
        } else if(x >= beta && x <= gamma) {
            return 1;
        } else if(x > gamma && x <= delta) {
            return pi3.value((x - gamma)/(delta - gamma));
        } else if(x > delta) {
            if(gamma < delta) return 0;
            // gamma == sup(U) ommitted
            else if(gamma == delta) return 1;
        }
        return 0;
    }

    public static class TrapezoidPIFunction extends PIFunction {

        public TrapezoidPIFunction(double alpha, double beta, double gamma, double delta) {
            super(alpha, beta, gamma, delta, new Identity(), new Complement());
        }

    }

    public static class TriangularPIFunction extends TrapezoidPIFunction {

        public TriangularPIFunction(double alpha, double beta, double delta) {
            super(alpha, beta, beta, delta);
        }

    }

}
