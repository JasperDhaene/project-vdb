package fuzzy.norm;

/**
 * Norm - t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public interface Norm {

    public double norm(double a, double b);
    public double conorm(double a, double b);

}
