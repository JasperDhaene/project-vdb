package fuzzy.norm;

import fuzzy.Norm;

/**
 * ZadehNorm - Zadeh t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class ZadehNorm implements Norm {

    @Override
    public int norm(int a, int b) {
        return Math.min(a, b);
    }

    @Override
    public int conorm(int a, int b) {
        return Math.max(a, b);
    }

}
