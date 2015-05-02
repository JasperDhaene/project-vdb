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

    // TODO: integration interval
    private static final int MIN_VAL = 0;
    private static final int MAX_VAL = 1600;

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
        Map<String, List<UnivariateFunction>> consequences = new HashMap<>();
        this.rules.forEach((r) -> {
            Consequence c = r.evaluate(this.inputs);

            if(!consequences.containsKey(c.variable))
                consequences.put(c.variable, new ArrayList<>());

            consequences.get(c.variable).add(c);
        });

        /**
         * 4. Unification of rules
         * 5. Defuzzification of variables
         */
        Map<String, Double> crisp = new HashMap<>();

        consequences.forEach((s, l) -> {

            UnivariateFunction g = new Denominator(l);
            UnivariateFunction f = new Numerator(g);


//            System.out.println("==> DENOMINATOR");
//            Utils.visualizeFunc(g, 0, 1600, 100);
//            System.out.println("==> END DENOMINATOR");

            Utils.tic();
            double denominator = FuzzySystem.integrator.integrate(MAX_EVAL, g, MIN_VAL, MAX_VAL);
            Utils.toc();
            if(denominator != 0) {
                double numerator = FuzzySystem.integrator.integrate(MAX_EVAL, f, MIN_VAL, MAX_VAL);
                crisp.put(s, (numerator / denominator));
            } else crisp.put(s, 0.0);
        });

        return crisp;
    }

    // f(x) gives the maximum of all aggregate functions
    private class Denominator implements UnivariateFunction {

        private final List<UnivariateFunction> functions;

        public Denominator(List<UnivariateFunction> functions) {
            this.functions = functions;
        }

        @Override
        public double value(double x) {
            double max = 0;
            for(UnivariateFunction f: functions){
                max = Math.max(f.value(x), max);
            }
            return max;
        }
    }

    // f(x) gives x*f(x)
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
