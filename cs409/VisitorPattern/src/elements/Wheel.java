package elements;

import visitors.CarElementVisitor;

public class Wheel implements CarElement {
    private String name;

    public Wheel(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void accept(final CarElementVisitor visitor) {
        /*
         * accept(CarElementVisitor) in elements.Wheel implements
         * accept(CarElementVisitor) in CarElement, so the call
         * to accept is bound at run time. This can be considered
         * the *first* dispatch. However, the decision to call
         * visit(elements.Wheel) (as opposed to visit(elements.Engine) etc.) can be
         * made during compile time since 'this' is known at compile
         * time to be a elements.Wheel. Moreover, each implementation of
         * CarElementVisitor implements the visit(elements.Wheel), which is
         * another decision that is made at run time. This can be
         * considered the *second* dispatch.
         */
        visitor.visit(this);
    }
}
