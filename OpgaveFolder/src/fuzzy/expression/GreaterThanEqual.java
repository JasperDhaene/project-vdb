package fuzzy.expression;

import fuzzy.norm.Norm;
import java.util.Map;

/**
 *
 * @author jasper
 */
public class GreaterThanEqual implements Expression {

    private final Premise expression;

    public GreaterThanEqual(Premise expression) {
        this.expression = expression;
    }
    
     /**
     * 
     * All values greater than the limitValue evaluate to 1
     * The limitValue represents the mean value of the membership function where
     * input evaluates to 1. 
     * For a PIFunction this is the average between beta and gamma,
     * but for all other membership functions this can be represented by a single value.
     */

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs) {
        if(!inputs.containsKey(expression.getVariable()))
            throw new RuntimeException("No such variable: " + expression.getVariable());

        double value = inputs.get(expression.getVariable());
        return expression.getLimitValue() <= value ? 1: expression.evaluate(norm, inputs);
    }


}
