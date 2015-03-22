package fuzzy.expression;

import fuzzy.norm.Norm;
import java.util.Map;

/**
 * Expression - Arbitrary CNF/DNF expression
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public interface Expression {

    public double evaluate(Norm norm, Map<String, Double> inputs);

}
