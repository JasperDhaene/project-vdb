package fuzzy.norm;

/**
 * ProbabilisticNorm - Probabilistic t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class ProbabilisticNorm implements Norm {

    @Override
    public double norm(double a, double b) {
        return (a * b);
    }

    @Override
    public double conorm(double a, double b) {
        return (a + b - (a * b));
    }

}
