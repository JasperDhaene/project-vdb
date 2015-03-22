package fuzzy.norm;

import fuzzy.Norm;

/**
 * LukasiewicsNorm - Lukasiewics t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class LukasiewicsNorm implements Norm {

    @Override
    public int norm(int a, int b) {
        return Math.max((a + b - 1), 0);
    }

    @Override
    public int conorm(int a, int b) {
        return Math.min(a + b, 1);
    }

}
