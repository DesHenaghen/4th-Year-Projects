package visitors;

import elements.Body;
import elements.Car;
import elements.Engine;
import elements.Wheel;

public interface CarElementVisitor {
    void visit(Body body);
    void visit(Car car);
    void visit(Engine engine);
    void visit(Wheel wheel);
}