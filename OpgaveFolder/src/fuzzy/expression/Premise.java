package fuzzy.expression;

import fuzzy.norm.Norm;
import java.util.Map;
import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Premise - Fuzzy premise
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public class Premise implements Expression {

    public String variable;
    public UnivariateFunction membership;

    public Premise(String variable, UnivariateFunction membership) {
        this.variable = variable;
        this.membership = membership;
    }

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs) {
        if(!inputs.containsKey(variable))
            throw new RuntimeException("No such variable: " + variable);

        return membership.value(inputs.get(variable));
    }

}
