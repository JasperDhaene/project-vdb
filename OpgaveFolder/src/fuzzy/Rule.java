package fuzzy;

import fuzzy.norm.Norm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Rule - Fuzzy rule
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public class Rule {

    // TODO: evaluate premises as CNF, not as a list of conjunctions (aka implement t-conorm)
    private final Map<String, List<Premise>> variables;
    private Consequence consequence;

    public Rule() {
        this.variables = new HashMap<>();
    }

    public void addPremise(Premise premise) {
        if(!this.variables.containsKey(premise.variable))
            this.variables.put(premise.variable, new ArrayList<>());

        this.variables.get(premise.variable).add(premise);
    }

    public void setConsequence(Consequence consequence) {
        this.consequence = consequence;
    }

    public Consequence evaluate(Map<String, Double> inputs, Norm norm) {
        // Aggregate membership of rule is one by default: no premises means no conditions to adhere to
        double membership = 1;

        // Calculate aggregate membership by looping over needed input variables
        for(String v: variables.keySet()){
            if(!inputs.containsKey(v))
                throw new RuntimeException("No such input value: " + v);

            Iterator<Premise> it = variables.get(v).iterator();
            while(it.hasNext()){
                /**
                 * 1. Evaluation of premises
                 * 2. Aggregation of multiple premises by t-(co-)norm
                 */
                Premise p = it.next();
                norm.norm(membership, p.membership.value(inputs.get(v)));
                System.out.println("Evaluating premise '" + p.variable + "' in variable " +
                        inputs.get(v) + ": " + p.membership.value(inputs.get(v)));
            }
        }

        /**
         * 3. Determine combined output level
         */
        this.consequence.setLimit(membership);
        System.out.println("Rule membership limit: " + membership);

        return this.consequence;
    }

}
