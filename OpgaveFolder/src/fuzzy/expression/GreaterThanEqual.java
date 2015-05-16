/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzy.expression;

import fuzzy.membership.PIFunction;
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

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs) {
        if(!inputs.containsKey(expression.getVariable()))
            throw new RuntimeException("No such variable: " + expression.getVariable());

        double value = inputs.get(expression.getVariable());
        return expression.getUpperLimit() <= value ? 1: expression.evaluate(norm, inputs);
    }


}
