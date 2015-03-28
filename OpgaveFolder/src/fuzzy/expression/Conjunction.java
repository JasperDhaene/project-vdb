package fuzzy.expression;

import fuzzy.norm.Norm;
import java.util.Map;

/**
 * Conjunction - CNF expression
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Conjunction implements Expression {

    private final Expression left, right;

    public Conjunction(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs){
        return norm.norm(this.left.evaluate(norm, inputs), this.right.evaluate(norm, inputs));
    }

}
