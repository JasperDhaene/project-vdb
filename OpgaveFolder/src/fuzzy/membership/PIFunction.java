package fuzzy.membership;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Identity;

/**
 * PIFunction - Large PI-function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class PIFunction implements UnivariateFunction {

    private final int alpha, beta, gamma, delta;
    private final UnivariateFunction pi1, pi3;

    public PIFunction(int alpha, int beta, int gamma, int delta, UnivariateFunction pi1, UnivariateFunction pi3) {
        if(alpha > beta || beta > gamma || gamma > delta)
            throw new RuntimeException(new IllegalArgumentException("Arguments must be serially smaller than or equal to each other."));

        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.delta = delta;

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

        public TrapezoidPIFunction(int alpha, int beta, int gamma, int delta) {
            super(alpha, beta, gamma, delta, new Identity(), new Complement());
        }

    }

    public static class TriangularPIFunction extends TrapezoidPIFunction {

        public TriangularPIFunction(int alpha, int beta, int delta) {
            super(alpha, beta, beta, delta);
        }

    }

}
