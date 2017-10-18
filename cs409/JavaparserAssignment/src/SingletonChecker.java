import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class SingletonChecker {
    // List of all SingletonCheckers created (one for each class)
    List<SingletonChecker> classes = null;

    // Field fields
    private boolean staticInstanceField = false;
    private String fieldDeclaration = null;

    // Method fields
    private boolean staticInstanceMethod = false;
    private String methodDeclaration = null;

    // Instance fields
    private String className = null;
    private SingletonChecker root = null;

    /**
     * Constructor for a Singleton Checker of a specified class
     *
     * @param name - Name of the class
     * @param s - SingletonChecker passed into visitor
     */
    public SingletonChecker(String name, SingletonChecker s) {
        className = name;
        root = s.getRoot();
    }

    /**
     * Constructor for the root SingletonChecker
     */
    public SingletonChecker(){
        classes = new ArrayList<>();
        root = this;
    }

    /**
     * @return - The name of the class
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return - The SingletonChecker instance that is the root object
     */
    public SingletonChecker getRoot() {
        return root;
    }

    /**
     * Marks the static instance field as being found
     * @param f - The static instance field declaration
     */
    public void foundStaticInstanceField(FieldDeclaration f){
        staticInstanceField = true;
        fieldDeclaration = f.toString();
    }

    /**
     *
     * @param m
     */
    public void foundStaticInstanceMethod(MethodDeclaration m) {
        staticInstanceMethod = true;
        methodDeclaration = m.getDeclarationAsString();
    }

    /**
     * Checks whether this class is a Singleton.
     * Does so by checking if both a static instance get method & a static instance field declarator were found.
     * If so, prints the details of the class.
     */
    public void isSingleton() {
        if (staticInstanceMethod && staticInstanceField) {
            System.out.println("Class Name: "+className);
            System.out.println("Method Declaration: "+methodDeclaration);
            System.out.println("Field Declaration: "+fieldDeclaration);
            System.out.println();
        }
    }

    /**
     * Adds a SingletonChecker for the specified class to the list of classes
     * @param name
     * @return
     */
    public SingletonChecker addClass(String name){
        SingletonChecker sc = new SingletonChecker(name, this);
        root.classes.add(sc);

        return sc;
    }

    @Override
    public String toString() {
        return className;
    }
}
