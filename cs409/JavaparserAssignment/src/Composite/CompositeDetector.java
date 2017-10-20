package Composite;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CompositeDetector extends VoidVisitorAdapter<CompositeChecker> {

    /**
     * Adds a ClassCompositeChecker to the RootCompositeChecker object for the current class
     * @param coi
     * @param cch
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration coi, CompositeChecker cch) {
        CompositeChecker cc = cch.addClass(coi.getNameAsString());
        // Add all extended and implemented classes
        for (ClassOrInterfaceType c  : coi.getExtendedTypes()) {
            cc.getExtended().add(c.getNameAsString());
        }
        for (ClassOrInterfaceType c  : coi.getImplementedTypes()) {
            cc.getExtended().add(c.getNameAsString());
        }

        super.visit(coi, cc);
    }

    /**
     * If MethodDeclaration has a static modifier and has a type that matches the current class, marks the static method
     * as being found in the CompositeChecker for this class
     * @param m
     * @param cc
     */
    @Override
    public void visit(MethodDeclaration m, CompositeChecker cc) {
        cc.setCurrentMethod(m);

        super.visit(m, cc);
    }

    /**
     * If the FieldDeclaration declares a variable of the same type as the current class, marks the static field as
     * being found in the CompositeChecker
     * @param f
     * @param cc
     */
    @Override
    public void visit(FieldDeclaration f, CompositeChecker cc) {
        cc.getFields().add(f);

        super.visit(f, cc);
    }

    @Override
    public void visit(ExpressionStmt e, CompositeChecker cc) {
        cc.setCurrentExpression(e);

        super.visit(e, cc);
    }

    @Override
    public void visit(MethodCallExpr m, CompositeChecker cc) {
        // Loop through all arguments in the method call
        for(com.github.javaparser.ast.expr.Expression e : m.getArguments()) {
            if (cc.getCurrentExpression() != null && cc.getCurrentMethod() != null) {

                // Check if the argument is one of the parameters passed in
                for (Parameter p : cc.getCurrentMethod().getParameters()) {
                    if (p.getNameAsString().equals(e.toString())) {
                        // This method is possibly a composite method, so save for post-processing
                        cc.getApplicableMethods().add(cc.getCurrentMethod());

                        // If current method is null, no more processing of the expressions in this method
                        // will take place
                        cc.setCurrentMethod(null);
                    }
                }
            }
        }

        super.visit(m, cc);
    }
}