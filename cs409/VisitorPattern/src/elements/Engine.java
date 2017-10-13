package elements;

import visitors.CarElementVisitor;

public class Engine implements CarElement {
    public void accept(final CarElementVisitor visitor) {
        visitor.visit(this);
    }
}
