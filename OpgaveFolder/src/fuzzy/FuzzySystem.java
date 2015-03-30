package fuzzy;

import fuzzy.membership.UnionFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;

/**
 * FuzzySystem - Simulates a fuzzy rule-based system
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class FuzzySystem {

    private final List<Rule> rules;
    private final Map<String, Double> inputs;

    public FuzzySystem() {
        this.rules = new ArrayList<>();
        this.inputs = new HashMap<>();
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public void addInput(String input, double value) {
        this.inputs.put(input, value);
    }

    public Map<String, Double> evaluate(){
        if(this.rules.isEmpty()) System.err.println("WARNING: No rules were defined in the system");
        List<Consequence> consequences = new ArrayList<>();
        /**
         * 1. Evaluation: evaluate each rule for a given variable
         * 2. Aggregation of multiple conditions
         */
        this.rules.forEach((r) -> {
            consequences.add(r.evaluate(this.inputs));
        });

        /**
         * 4. Union of rules
         *
         */
        UnionFunction union = new UnionFunction(consequences,
                new SimpsonIntegrator(),
                10000);
//                SimpsonIntegrator.SIMPSON_MAX_ITERATIONS_COUNT);

        /**
         * 5. Defuzzification of variables
         */
        Map<String, Double> crisp = new HashMap<>();
        this.inputs.keySet().forEach((s) -> {
            crisp.put(s, union.value());
        });
        return crisp;
    }
}
