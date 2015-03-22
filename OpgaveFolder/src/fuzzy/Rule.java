package fuzzy;

import fuzzy.expression.Expression;
import fuzzy.norm.Norm;
import java.util.Map;

/**
 * Rule - Fuzzy rule
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public class Rule {

    private final Expression expression;
    private Consequence consequence;

    public Rule(Expression expression) {
        this.expression = expression;
    }

    public void setConsequence(Consequence consequence) {
        this.consequence = consequence;
    }

    public Consequence evaluate(Map<String, Double> inputs, Norm norm) {

        double membership = expression.evaluate(norm, inputs);

        /**
         * 3. Determine combined output level
         */
        this.consequence.setLimit(membership);
        System.out.println("Rule membership limit: " + membership);

        return this.consequence;
    }

}
