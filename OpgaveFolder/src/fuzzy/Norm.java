package fuzzy;

/**
 * Norm - t-(co-)norm pair
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public interface Norm {

    public int norm(int a, int b);
    public int conorm(int a, int b);

}
