package fuzzy;

/**
 * Premise - fuzzy premise
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public abstract class Premise {

    public String variable;

    public Premise(String variable) {
        this.variable = variable;
    }

    public abstract int membership(int input);

}
