package examples;

public class Pair {

    public double speed, distanceFront;

    public Pair(double left, double right) {
        this.speed = left;
        this.distanceFront = right;
    }

    @Override
    public String toString() {
        return "{" + "speed=" + speed + ", distFront=" + distanceFront + "}";
    }
}
