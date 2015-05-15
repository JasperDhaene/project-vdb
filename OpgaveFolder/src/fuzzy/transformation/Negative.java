package fuzzy.transformation;

import fuzzy.membership.Membership;

/**
 * Negative
 * @author florian
 */
public class Negative implements Membership {

    private final Membership f;

    public Negative(Membership f) {
        this.f = f;
    }

    @Override
    public double value(double d) {
        return (-1) * Math.abs(f.value(d));
    }

    @Override
    public double getUpperLimit() {
        return (-1) * Math.abs(f.getLowerLimit());
    }

    @Override
    public double getLowerLimit() {
        return (-1) * Math.abs(f.getUpperLimit());
    }

}
