package fuzzy;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Consequence - Output function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Consequence implements UnivariateFunction {

    public double limit;
    public UnivariateFunction membership;

    public Consequence(UnivariateFunction membership){
        this.limit = Double.MIN_VALUE;
        this.membership = membership;
    }

    public void setLimit(double limit){
        this.limit = limit;
    }

    public double value(double x){
        return Math.min(this.limit, this.membership.value(x));
    }

}
