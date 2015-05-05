package fuzzy;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Consequence - Output function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Consequence implements UnivariateFunction {

    private double limit;
    public final String variable;
    public final UnivariateFunction membership;

    // Integration interval
    public final int integrationMin, integrationMax;

    public Consequence(String variable, UnivariateFunction membership, int integrationMin, int integrationMax) {
        this.variable = variable;
        this.limit = Double.MAX_VALUE;
        this.membership = membership;
        this.integrationMin = integrationMin;
        this.integrationMax = integrationMax;
    }

    public void setLimit(double limit){
        this.limit = limit;
    }

    @Override
    public double value(double x){
        return Math.min(this.limit, this.membership.value(x));  //TODO: this is never used. It's necessary as a univariateFunction,
                                                                //but never called for in code, so we have no idea how it behaves.
    }

}
