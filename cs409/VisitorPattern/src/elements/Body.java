package elements;

import visitors.CarElementVisitor;

public class Body implements CarElement {
    public void accept(final CarElementVisitor visitor) {
        visitor.visit(this);
    }
}
