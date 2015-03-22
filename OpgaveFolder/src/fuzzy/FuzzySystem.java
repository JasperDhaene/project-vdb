package fuzzy;

import fuzzy.membership.UnionFunction;
import fuzzy.norm.Norm;
import fuzzy.norm.ZadehNorm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;

/**
 * FuzzySystem - Simulates a fuzzy rule-based system
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class FuzzySystem {

    private final List<Rule> rules;
    private final Map<String, Double> inputs;
    private Norm norm;

    public FuzzySystem() {
        this.rules = new ArrayList<>();
        this.inputs = new HashMap<>();
        // Default to Zadeh t-(co-)norm pair
        this.norm = new ZadehNorm();
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public void addInput(String input, double value) {
        this.inputs.put(input, value);
    }

    public void setNorm(Norm norm){
        this.norm = norm;
    }

    public Map<String, Double> evaluate(){
        List<Consequence> consequences = new ArrayList<>();
        /**
         * 1. Evaluation: evaluate each rule for a given variable
         */
        rules.forEach((r) -> {
            consequences.add(r.evaluate(norm, inputs));
        });

        /**
         * 4. Union of rules
         */
        // Possible integrators: Romberg, Trapezoid, Simpson, LegendreGauss
        UnivariateFunction union = new UnionFunction(consequences, new TrapezoidIntegrator());

        /**
         * 5. Defuzzification of variables
         */
        Map<String, Double> crisp = new HashMap<>();
        for(String s: inputs.keySet()){
            crisp.put(s, union.value(inputs.get(s)));
        }
        return crisp;
    }
}
