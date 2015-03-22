package fuzzy.norm;

import fuzzy.Norm;

/**
 * ProbabilisticNorm - Probabilistic t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class ProbabilisticNorm implements Norm {

    @Override
    public int norm(int a, int b) {
        return (a * b);
    }

    @Override
    public int conorm(int a, int b) {
        return (a + b - (a * b));
    }

}
