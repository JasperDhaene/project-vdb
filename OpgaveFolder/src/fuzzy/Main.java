package fuzzy;

import fuzzy.membership.SFunction;
import fuzzy.norm.LukasiewicsNorm;
import org.apache.commons.math3.analysis.function.Identity;

/**
 * fuzzy.Main - Simulates a fuzzy rule-based system
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public class Main {

	public static void main(String[] args) {
            /**
             * Setup: create a new fuzzy system and add rules and inputs
             *
             * Example: IF (temperature is "low") THEN (heater is "high")
             *
             */
	    FuzzySystem system = new FuzzySystem();
            Rule rule = new Rule();
            rule.addPremise(new Premise("temperature", new SFunction(20, 25)));
            rule.setConsequence(new Consequence(new Identity()));

            system.setNorm(new LukasiewicsNorm());
            system.addRule(rule);
            system.addInput("temperature", 22);

            System.out.println("system => " + system.evaluate());

	}

}
