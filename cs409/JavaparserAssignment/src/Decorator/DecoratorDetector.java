package Decorator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class DecoratorDetector extends VoidVisitorAdapter<DecoratorChecker> {

    /**
     * Adds a DecoratorChecker to the root DecoratorChecker object for the current class
     * @param coi
     * @param cch
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration coi, DecoratorChecker cch) {
        DecoratorChecker cc = cch.addClass(coi.getNameAsString());
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
     * as being found in the DecoratorChecker for this class
     * @param m
     * @param cc
     */
    @Override
    public void visit(MethodDeclaration m, DecoratorChecker cc) {
        cc.getApplicableMethods().add(m);

        super.visit(m, cc);
    }

    /**
     * If the FieldDeclaration declares a variable of the same type as the current class, marks the static field as
     * being found in the DecoratorChecker
     * @param f
     * @param cc
     */
    @Override
    public void visit(FieldDeclaration f, DecoratorChecker cc) {
        cc.getFields().add(f);

        super.visit(f, cc);
    }
}