package fuzzy;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Consequence - Output function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Consequence {

    public double limit;
    public UnivariateFunction membership;

    public Consequence(UnivariateFunction membership){
        this.membership = membership;
    }

    public void setLimit(double limit){
        this.limit = limit;
    }

}
