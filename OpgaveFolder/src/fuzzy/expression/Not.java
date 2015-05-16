package fuzzy.expression;

import fuzzy.norm.Norm;
import java.util.Map;

/**
 * Not - Evaluates to the negation of an expression
 * @author jasper
 */
public class Not implements Expression {
    
    private final Expression expression;

    public Not(Expression expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs) {
        return (1 - expression.evaluate(norm, inputs));
    }

}
