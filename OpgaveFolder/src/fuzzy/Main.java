package fuzzy;

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
             * Practical example: IF (temperature is "low") THEN (heater is "high")
             *
             */
	    FuzzySystem system = new FuzzySystem();
            Rule rule = new Rule();
            rule.addPremise(new Premise("temperature") {

                @Override
                public int membership(int input) {
                    if((input > 20) && (input < 25))
                        return 1;
                    return 0;
                }

            });
            rule.setConsequence(new Consequence(){

                // Returns the power value for the heater in W
                @Override
                public int membership(int input) {
                    if(input > 0.5)
                        return 1000;
                    return 0;
                }

            });
            system.addRule(rule);
            system.addInput("temperature", 22);

            System.out.println("system => " + system.evaluate());

	}

}
