package fuzzy.norm;

/**
 * ZadehNorm - Zadeh t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class ZadehNorm implements Norm {

    @Override
    public double norm(double a, double b) {
        return Math.min(a, b);
    }

    @Override
    public double conorm(double a, double b) {
        return Math.max(a, b);
    }

}
