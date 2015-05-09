/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fuzzy.expression;

import fuzzy.norm.Norm;
import java.util.Map;

/**
 *
 * @author jasper
 */
public class Not implements Expression{
    
    private final Expression expression;

    public Not(Expression expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs){
        return 1 - expression.evaluate(norm, inputs);
    }
}
