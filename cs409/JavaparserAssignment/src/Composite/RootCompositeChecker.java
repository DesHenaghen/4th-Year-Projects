package Composite;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootCompositeChecker implements  CompositeChecker{
    // List of all CompositeCheckers created (one for each class)
    Map<String, CompositeChecker> classes = new HashMap<>();

    /**
     * Constructor for the root CompositeChecker
     */
    public RootCompositeChecker(){}

    /**
     * @return - The CompositeChecker instance that is the root object
     */
    @Override
    public CompositeChecker getRoot() {
        return this;
    }

    /**
     * Adds a CompositeChecker for the specified class to the list of classes
     * @param name
     * @return
     */
    @Override
    public CompositeChecker addClass(String name){
        CompositeChecker sc = new ClassCompositeChecker(name, this);
        classes.put(name, sc);

        return sc;
    }

    @Override
    public Map<String, CompositeChecker> getClasses() {
        return classes;
    }

    @Override
    public void findComposites() {
        flattenTrees();
        determineCompositeClasses();
    }

    @Override
    public void flattenTrees(){
        for (Map.Entry<String, CompositeChecker> c : classes.entrySet()) {
            c.getValue().flattenTrees();
        }
    }

    @Override
    public void determineCompositeClasses() {
        List<String> compositeClasses = new ArrayList<>();
        Map<String, List<FieldDeclaration>> compositeFields = new HashMap<>();
        Map<String, List<MethodDeclaration>> compositeMethods = new HashMap<>();

        for (Map.Entry<String, CompositeChecker> c : classes.entrySet()) {
            c.getValue().getCompositeClassDetails(compositeClasses, compositeMethods, compositeFields);
        }

        printCompositeClasses(compositeClasses, compositeMethods, compositeFields);
    }

    @Override
    public void printCompositeClasses(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods, Map<String, List<FieldDeclaration>> compositeFields) {
        for (String className : compositeClasses) {
            System.out.println("Class Name: " + className);
            for (FieldDeclaration f : compositeFields.get(className)) {
                System.out.println("Composite Field Declaration: " + f.toString());
            }
            for (MethodDeclaration m : compositeMethods.get(className)) {
                System.out.println("Composite Method Declaration: "+ m.getDeclarationAsString());
            }
        }
    }
}
