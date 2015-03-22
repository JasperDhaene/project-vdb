package fuzzy;

/**
 * Consequence - Output function
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public abstract class Consequence {

    private int limit;

    public void setLimit(int limit){
        this.limit = limit;
    }

    public abstract int membership(int input);

}
