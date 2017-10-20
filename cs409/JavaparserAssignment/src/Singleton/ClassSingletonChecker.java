package Singleton;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ClassSingletonChecker implements SingletonChecker{
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
    public ClassSingletonChecker(String name, SingletonChecker s) {
        className = name;
        root = s.getRoot();
    }

    /**
     * @return - The name of the class
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * @return - The SingletonChecker instance that is the root object
     */
    @Override
    public SingletonChecker getRoot() {
        return root;
    }

    /**
     * Marks the static instance field as being found
     * @param f - The static instance field declaration
     */
    @Override
    public void foundStaticInstanceField(FieldDeclaration f){
        staticInstanceField = true;
        fieldDeclaration = f.toString();
    }

    /**
     *
     * @param m
     */
    @Override
    public void foundStaticInstanceMethod(MethodDeclaration m) {
        staticInstanceMethod = true;
        methodDeclaration = m.getDeclarationAsString();
    }

    /**
     * Checks whether this class is a Singleton.
     * Does so by checking if both a static instance get method & a static instance field declarator were found.
     * If so, prints the details of the class.
     */
    @Override
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
    public ClassSingletonChecker addClass(String name){
        ClassSingletonChecker sc = new ClassSingletonChecker(name, this);
        root.getClasses().add(sc);

        return sc;
    }

    @Override
    public List<SingletonChecker> getClasses() {
        return root.getClasses();
    }

    @Override
    public String toString() {
        return className;
    }
}
