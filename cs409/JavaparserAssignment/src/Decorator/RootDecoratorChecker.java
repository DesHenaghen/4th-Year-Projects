package Decorator;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootDecoratorChecker implements DecoratorChecker {
    // List of all SingletonCheckers created (one for each class)
    Map<String, DecoratorChecker> classes = new HashMap<>();

    /**
     * Constructor for the root Singleton.SingletonCheckerOld
     */
    public RootDecoratorChecker(){}

    /**
     * @return - The Singleton.SingletonCheckerOld instance that is the root object
     */
    @Override
    public DecoratorChecker getRoot() {
        return this;
    }

    /**
     * Adds a DecoratorChecker for the specified class to the list of classes
     * @param name
     * @return
     */
    @Override
    public DecoratorChecker addClass(String name){
        DecoratorChecker dc = new ClassDecoratorChecker(name, this);
        classes.put(name, dc);

        return dc;
    }

    @Override
    public Map<String, DecoratorChecker> getClasses() {
        return classes;
    }

    @Override
    public void findDecorators() {
        for (Map.Entry<String, DecoratorChecker> c : classes.entrySet()) {
            c.getValue().findDecorators();
        }
        determineDecoratorClasses();
    }

    @Override
    public void determineDecoratorClasses() {
        List<String> compositeClasses = new ArrayList<>();
        Map<String, List<VariableDeclarator>> compositeFields = new HashMap<>();
        Map<String, List<MethodDeclaration>> compositeMethods = new HashMap<>();

        for (Map.Entry<String, DecoratorChecker> c : classes.entrySet()) {
            c.getValue().getDecoratorClassDetails(compositeClasses, compositeMethods, compositeFields);
        }

        printDecoratorClasses(compositeClasses, compositeMethods, compositeFields);
    }

    @Override
    public void printDecoratorClasses(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods, Map<String, List<VariableDeclarator>> compositeFields) {
        for (String className : compositeClasses) {
            System.out.println("Decorator Class Name: " + className);
            for (VariableDeclarator v : compositeFields.get(className)) {
                System.out.println("Component Field: " + v.getType() + " " + v.toString());
            }
            for (MethodDeclaration m : compositeMethods.get(className)) {
                System.out.println("Decorator Method: "+ m.getDeclarationAsString());
            }
            System.out.println();
        }
    }
}
