package fuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator;
import org.apache.commons.math3.analysis.integration.MidPointIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

/**
 * FuzzySystem - Simulates a fuzzy rule-based system
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class FuzzySystem {

    private static final int MAX_EVAL = BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT;
    private static final UnivariateIntegrator integrator = new MidPointIntegrator(1.0e-3, 1.0e-15, 3, 64);
    
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

        /**
         * 1. Evaluation: evaluate each rule for a given variable
         */
        Map<String, List<Consequence>> consequences = new HashMap<>();
        this.rules.forEach((rule) -> {
            Consequence c = rule.evaluate(this.inputs);

            if(!consequences.containsKey(c.variable))
                consequences.put(c.variable, new ArrayList<>());

            consequences.get(c.variable).add(c);
        });

        /**
         * 4. Unification of rules
         * 5. Defuzzification of variables
         */
        Map<String, Double> crisp = new HashMap<>();

        consequences.forEach((string, consequenceList) -> {

            Denominator g = new Denominator(consequenceList);
            Numerator f = new Numerator(g);

            // Numerator and denominator have the same integration boundaries
            double denominator = FuzzySystem.integrator.integrate(MAX_EVAL, g, g.integrationMin, g.integrationMax);
            if(denominator != 0) {
                double numerator = FuzzySystem.integrator.integrate(MAX_EVAL, f, g.integrationMin, g.integrationMax);
                crisp.put(string, (numerator / denominator));
            } else crisp.put(string, 0.0);
        });

        return crisp;
    }

    // f(x) gives the maximum of all aggregate functions
    // @nl: Noemer. Onder de breukstreep.
    private class Denominator implements UnivariateFunction {

        private final List<Consequence> consequences;
        public int integrationMin, integrationMax;

        public Denominator(List<Consequence> functions) {
            this.consequences = functions;
            for (Consequence c: functions) {
                if (c.integrationMin < this.integrationMin)
                    this.integrationMin = c.integrationMin;
                if (c.integrationMax > this.integrationMax)
                    this.integrationMax = c.integrationMax;
            }
        }

        @Override
        public double value(double x) {
            double max = 0;
            // 4. Unification of rules: evaluate to the pointwise maximum.
            // This simulates the unification of various membership functions from
            // different consequences
            for(UnivariateFunction c: consequences){ 
                max = Math.max(c.value(x), max);
            }
            return max;
        }
    }

    // f(x) gives x*f(x)
    // @nl: Teller. Boven de breukstreep.
    private class Numerator implements UnivariateFunction {

        private final UnivariateFunction f;
        //public int integrationMin, integrationMax;

        public Numerator(UnivariateFunction f) {
            this.f = f;
        }

        @Override
        public double value(double x) {
            return x * f.value(x);
        }
    }
}
