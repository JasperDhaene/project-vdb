package fuzzy.expression;

import fuzzy.membership.Membership;
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
    public Membership membership;

    public Premise(String variable, Membership membership) {
        this.variable = variable;
        this.membership = membership;
    }

    @Override
    public double evaluate(Norm norm, Map<String, Double> inputs) {
        if(!inputs.containsKey(variable))
            throw new RuntimeException("No such variable: " + variable);

        return membership.value(inputs.get(variable));
    }
    
    public double getLimitValue(){
        return membership.getLimitValue();
    }
    
    public String getVariable(){
        return this.variable;
    }
    public UnivariateFunction getMembership(){
        return this.membership;
    }

}
