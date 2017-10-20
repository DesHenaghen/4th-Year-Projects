package Adapter;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;

public class AdapterDetector extends VoidVisitorAdapter<AdapterChecker> {

    /**
     * Adds a Adapter.AdapterChecker to the root Adapter.AdapterChecker object for the current class
     * @param coi
     * @param ac
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration coi, AdapterChecker ac) {
        NodeList<ClassOrInterfaceType> implemented = coi.getImplementedTypes();
        NodeList<ClassOrInterfaceType> extended = coi.getExtendedTypes();

        ac.getAllClasses().add(coi.getNameAsString());

        if (coi.isInterface() || coi.isAbstract()) {
            AdapterChecker newac = ac.addInterface(coi.getNameAsString());

            super.visit(coi, newac);
        } else if (implemented.size() > 0 || extended.size() > 0) {
            AdapterChecker newac = ac.addClass(coi.getNameAsString());

            // Add all interfaces to class Adapter.AdapterChecker
            for (ClassOrInterfaceType c : implemented)
                newac.addImplementedClass(c);

            // Add all extended classes to class Adapter.AdapterChecker
            for (ClassOrInterfaceType c : extended)
                newac.addImplementedClass(c);

            super.visit(coi, newac);
        } else {
            super.visit(coi, ac);
        }
    }

    /**
     * If MethodDeclaration has a static modifier and has a type that matches the current class, marks the static method
     * as being found in the Adapter.AdapterChecker for this class
     * @param m
     * @param cc
     */
    @Override
    public void visit(MethodDeclaration m, AdapterChecker cc) {
        cc.setCurrentMethod(m);

        if (!cc.isRoot()) {
            cc.getMethods().put(m.getNameAsString(), new ArrayList<>());
        }

        super.visit(m, cc);
    }

    /**
     * If the FieldDeclaration declares a variable of the same type as the current class, marks the static field as
     * being found in the Adapter.AdapterChecker
     * @param f
     * @param cc
     */
    @Override
    public void visit(FieldDeclaration f, AdapterChecker cc) {
        if (!cc.isInterface() && !cc.isRoot()) {
            // At this point store all fields. Fields will be checked for being of type Adaptee later
            for (VariableDeclarator v : f.getVariables()) {
                cc.getFields().add(v);
            }
        }

        super.visit(f, cc);
    }

    @Override
    public void visit(MethodCallExpr m, AdapterChecker ac) {
        if (!ac.isInterface() && !ac.isRoot()) {
            String methodString = m.toString();
            // If methodCall is an invocation of a variable's method...
            if (methodString.indexOf('.') > -1) {
                // Save it to the method
                if (ac.getCurrentMethod() != null) {
                    if (ac.getMethods() != null && ac.getMethods().containsKey(ac.getCurrentMethod().getNameAsString())) {
                        ac.getMethods().get(ac.getCurrentMethod().getNameAsString())
                                .add(methodString.substring(0, methodString.indexOf('.')));
                    }
                }
            }
        }

        super.visit(m, ac);
    }
}