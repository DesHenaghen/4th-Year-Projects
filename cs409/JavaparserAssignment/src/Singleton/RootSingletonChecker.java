package Singleton;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class RootSingletonChecker implements SingletonChecker {
    // List of all SingletonCheckers created (one for each class)
    private List<SingletonChecker> classes = new ArrayList<>();

    /**
     * Constructor for the root SingletonChecker
     */
    public RootSingletonChecker(){}


    /**
     * @return - The SingletonChecker instance that is the root object
     */
    @Override
    public SingletonChecker getRoot() {
        return this;
    }

    /**
     * Checks whether this class is a Singleton.
     * Does so by checking if both a static instance get method & a static instance field declarator were found.
     * If so, prints the details of the class.
     */
    @Override
    public void isSingleton() {
        for (SingletonChecker c : classes) {
            c.isSingleton();
        }
    }

    /**
     * Adds a SingletonChecker for the specified class to the list of classes
     * @param name
     * @return
     */
    public SingletonChecker addClass(String name){
        SingletonChecker sc = new ClassSingletonChecker(name, this);
        classes.add(sc);

        return sc;
    }

    public List<SingletonChecker> getClasses() {
        return classes;
    }
}
