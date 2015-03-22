package fuzzy;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Premise - Fuzzy premise
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public class Premise {

    public String variable;
    public UnivariateFunction membership;

    public Premise(String variable, UnivariateFunction membership) {
        this.variable = variable;
        this.membership = membership;
    }

}
