package examples;

public class Pair {

    public double left, right;

    public Pair(double left, double right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "{" + "left=" + left + ", right=" + right + "}";
    }
}
