import elements.Car;
import visitors.CarElementDoVisitor;
import visitors.CarElementPrintVisitor;

public class VisitorPatternDemo {
    public static void main(final String[] args) {
        final Car car = new Car();

        car.accept(new CarElementPrintVisitor());
        car.accept(new CarElementDoVisitor());
    }
}