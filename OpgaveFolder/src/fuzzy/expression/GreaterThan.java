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
public class GreaterThan implements Expression {
    
    private final Expression expression;

    public GreaterThan(Expression expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs){
        double value = inputs.get(((Premise) expression).getVariable());
        return (((PIFunction) ((Premise) expression).getMembership()).getUpperLimit() <= value )? 1: expression.evaluate(norm, inputs);
    }
}
