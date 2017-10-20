package Singleton;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class SingletonDetector extends VoidVisitorAdapter<SingletonChecker> {

    /**
     * Adds a SingletonChecker to the root SingletonChecker object for the current class
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, SingletonChecker arg) {
        SingletonChecker sc = arg.addClass(n.getNameAsString());

        super.visit(n, sc);
    }

    /**
     * If MethodDeclaration has a static modifier and has a type that matches the current class, marks the static method
     * as being found in the SingletonChecker for this class
     * @param n
     * @param arg
     */
    @Override
    public void visit(MethodDeclaration n, SingletonChecker arg) {
        if (n.getModifiers().contains(Modifier.STATIC) && n.getType().toString().equals(arg.getClassName())){
            arg.foundStaticInstanceMethod(n);
        }

        super.visit(n, arg);
    }

    /**
     * If the FieldDeclaration declares a variable of the same type as the current class, marks the static field as
     * being found in the SingletonChecker
     * @param n
     * @param arg
     */
    @Override
    public void visit(FieldDeclaration n, SingletonChecker arg) {
        for(VariableDeclarator v : n.getVariables()) {
            if (v.getType().toString().equals(arg.getClassName())){
                arg.foundStaticInstanceField(n);
            }
        }

        super.visit(n, arg);
    }
}