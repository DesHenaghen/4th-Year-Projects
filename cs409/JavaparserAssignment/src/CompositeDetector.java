import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CompositeDetector extends VoidVisitorAdapter<CompositeChecker> {

    /**
     * Adds a CompositeChecker to the root CompositeChecker object for the current class
     * @param coi
     * @param cch
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration coi, CompositeChecker cch) {
        CompositeChecker cc = cch.addClass(coi.getNameAsString());
        // Add all extended and implemented classes
        for (ClassOrInterfaceType c  : coi.getExtendedTypes()) {
            cc.extended.add(c.getNameAsString());
        }
        for (ClassOrInterfaceType c  : coi.getImplementedTypes()) {
            cc.extended.add(c.getNameAsString());
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
        cc.currentMethod = m;

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
        cc.fields.add(f);

        super.visit(f, cc);
    }

    @Override
    public void visit(ExpressionStmt e, CompositeChecker cc) {
        cc.currentExpression = e;

        super.visit(e, cc);
    }

    @Override
    public void visit(MethodCallExpr m, CompositeChecker cc) {
            for(com.github.javaparser.ast.expr.Expression e : m.getArguments()) {
                if (cc.currentExpression != null && cc.currentMethod != null) {
                    for (Parameter p : cc.currentMethod.getParameters()) {
                        if (p.getNameAsString().equals(e.toString())) {
//                            System.out.println("BINGO");
//                            System.out.print("CLASS: " + cc.getClassName());
//                            System.out.print("\nMETHOD: " + cc.currentMethod.getDeclarationAsString());
//                            System.out.print("\nexpression: " + cc.currentExpression.toString());
//                            System.out.println("\nvariable: " + e);
                            cc.applicableMethods.add(cc.currentMethod);

                            // If current method is null, no more processing of the expressions in this method
                            // will take place
                            cc.currentMethod = null;
                        }
                    }
                }
            }

        super.visit(m, cc);
    }
}