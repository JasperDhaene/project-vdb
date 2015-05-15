package fuzzy.membership;

/**
 * SFunction - S-shaped membership function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SFunction implements Membership {

    private final double alpha, beta, gamma;

    public SFunction(double alpha, double gamma){
        this.alpha = alpha;
        this.gamma = gamma;
        this.beta = (alpha + gamma) / 2;
    }

    @Override
    public double value(double value) {
        if(value < alpha){
            return 0;
        } else if(value >= alpha && value <= beta){
            return 2 * Math.pow(((value - alpha)/(gamma - alpha)), 2);
        } else if(value > beta && value <= gamma){
            return 1 - (2 * Math.pow(((value - gamma)/(gamma - alpha)), 2));
        } else return 1;
    }

    @Override
    public double getUpperLimit() {
        return this.gamma;
    }

    @Override
    public double getLowerLimit() {
        return this.alpha;
    }
}
