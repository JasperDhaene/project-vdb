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

    //TODO: nut en gebruik?
    @Override
    public double getLimitValue() {
        return (-1) * Math.abs(f.getLimitValue());
    }

}
