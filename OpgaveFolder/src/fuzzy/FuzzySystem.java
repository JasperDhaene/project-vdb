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

    /**
     * Minimum and maximum integration evaluations
     */
    private static final int MIN_EVAL = MidPointIntegrator.DEFAULT_MIN_ITERATIONS_COUNT;
    private static final int MAX_EVAL = BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT;
    /**
     * integrator - used to integrate the union of all rules
     * Integration efficiency is not the most important aspect in this project,
     * therefore the most simple implementation is chosen: the midpoint integration
     * method. Relative accuracy can be kept relatively high (10^-3).
     */
    private static final UnivariateIntegrator integrator = new MidPointIntegrator(
            1.0e-3,     // relative accuracy
            1.0e-15,    // absolute accuracy
            MidPointIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,   // minimalIterationCount
            MidPointIntegrator.MIDPOINT_MAX_ITERATIONS_COUNT);  // maximalIterationCount

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
        if(this.rules.isEmpty())
            System.err.println("WARNING: No rules were defined in the system");

        /**
         * 1. Evaluation: evaluate each rule for a given variable
         */
        Map<String, List<Consequence>> consequences = new HashMap<>();
        this.rules.forEach((rule) -> {
            Consequence c = rule.evaluate(this.inputs);

            // Create or append to list of consequences for a given variable
            if(!consequences.containsKey(c.variable))
                consequences.put(c.variable, new ArrayList<>());

            consequences.get(c.variable).add(c);
        });

        Map<String, Double> crisp = new HashMap<>();

        consequences.forEach((string, consequenceList) -> {

            /**
             * 4. Unification of rules
             *
             * */
            Denominator g = new Denominator(consequenceList);
            Numerator f = new Numerator(g);

            /**
             * 5. Defuzzification of variables
             */
            // Numerator and denominator have the same integration boundaries
            double denominator = FuzzySystem.integrator.integrate(MAX_EVAL, g, g.integrationMin, g.integrationMax);
            if(denominator != 0) {
                double numerator = FuzzySystem.integrator.integrate(MAX_EVAL, f, g.integrationMin, g.integrationMax);
                crisp.put(string, (numerator / denominator));
            } else crisp.put(string, 0.0);
        });

        return crisp;
    }

    /**
     * Denominator (noemer): g(x) = max(c_1, c_2, ..., c_n)
     *
     */
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
            double max = -Double.MAX_VALUE;
            for(UnivariateFunction c: consequences)
                max = Math.max(c.value(x), max);
            return max;
        }
    }

    /**
     * Numerator (teller): f(x) = x*g(x)
     *
     */
    private class Numerator implements UnivariateFunction {

        private final UnivariateFunction f;

        public Numerator(UnivariateFunction f) {
            this.f = f;
        }

        @Override
        public double value(double x) {
            return x * f.value(x);
        }
    }
}
