package fuzzy;

import fuzzy.norm.ZadehNorm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FuzzySystem - Simulates a fuzzy rule-based system
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class FuzzySystem {

    private final List<Rule> rules;
    private final Map<String, Integer> inputs;
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

    public void addInput(String input, int value) {
        this.inputs.put(input, value);
    }

    public void setNorm(Norm norm){
        this.norm = norm;
    }

    public int evaluate(){
        List<Consequence> consequences = new ArrayList<>();
        /**
         * 1. Evaluation: evaluate each rule for a given variable
         */
        rules.forEach((r) -> {
            consequences.add(r.evaluate(inputs, norm));
        });

        /**
         * 4. Union of rules
         */


        /**
         * 5. Sharpening (defuzzification)
         */
        return 0;
    }

}
