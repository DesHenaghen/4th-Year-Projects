package Composite;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeCheckerOld {
    // List of all SingletonCheckers created (one for each class)
    Map<String, CompositeCheckerOld> classes = null;

    List<String> extended = new ArrayList<String>();
    List<String> implemented = new ArrayList<String>();

    List<FieldDeclaration> fields = new ArrayList<>();

    List<MethodDeclaration> applicableMethods = new ArrayList<>();

    MethodDeclaration currentMethod = null;
    ExpressionStmt currentExpression = null;

    // Instance fields
    private String className = null;
    private CompositeCheckerOld root = null;
    private boolean isComposite = false;

    /**
     * Constructor for a Singleton Checker of a specified class
     *
     * @param name - Name of the class
     * @param s - Singleton.SingletonCheckerOld passed into visitor
     */
    public CompositeCheckerOld(String name, CompositeCheckerOld s) {
        className = name;
        root = s.getRoot();
    }

    /**
     * Constructor for the root Singleton.SingletonCheckerOld
     */
    public CompositeCheckerOld(){
        classes = new HashMap<>();
        root = this;
    }

    /**
     * @return - The name of the class
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return - The Singleton.SingletonCheckerOld instance that is the root object
     */
    public CompositeCheckerOld getRoot() {
        return root;
    }

    /**
     * Adds a Singleton.SingletonCheckerOld for the specified class to the list of classes
     * @param name
     * @return
     */
    public CompositeCheckerOld addClass(String name){
        CompositeCheckerOld sc = new CompositeCheckerOld(name, this);
        root.classes.put(name, sc);

        return sc;
    }

    public void flattenTrees(){
        if (classes == null) {
            flattenImplementTree();
        } else {
            for (Map.Entry<String, CompositeCheckerOld> c : classes.entrySet()) {
                c.getValue().flattenImplementTree();
            }
        }
    }

    private void flattenImplementTree() {
        List<String> temp = new ArrayList<>();
        // Check every extended/implemented class for further extensions or implements
        for (String i : extended) {
            // If class exists
            if (root.classes.containsKey(i)) {
                CompositeCheckerOld c = root.classes.get(i);
                // Loop through all classes it extends/implements
                for (String e : c.extended) {
                    // If it's a new one, add it to the list
                    if (!extended.contains(e)) {
                        temp.add(e);
                    }
                }
            }
        }

        extended.addAll(temp);
    }

    public void determineCompositeClasses() {
        List<String> compositeClasses = new ArrayList<>();
        Map<String, List<FieldDeclaration>> compositeFields = new HashMap<>();
        Map<String, List<MethodDeclaration>> compositeMethods = new HashMap<>();

        if (classes != null) {
            for (Map.Entry<String, CompositeCheckerOld> c : classes.entrySet()) {
                c.getValue().getCompositeClassDetails(compositeClasses, compositeMethods, compositeFields);
            }
        }

        printCompositeClasses(compositeClasses, compositeMethods, compositeFields);
    }

    private void printCompositeClasses(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods, Map<String, List<FieldDeclaration>> compositeFields) {
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

    private void getCompositeClassDetails(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods ,Map<String, List<FieldDeclaration>> compositeFields) {
        for (MethodDeclaration m : applicableMethods) {
            boolean compositeParam = false;

            for(com.github.javaparser.ast.body.Parameter p : m.getParameters()) {
                if (extended.contains(p.getType().asString())) {
                    compositeParam = true;
                    break;
                }
            }

            if (compositeParam) {
                m.getBody().ifPresent(b -> {
                    for (Statement s : b.getStatements()) {
                        for (FieldDeclaration f : fields) {
                            for (VariableDeclarator v : f.getVariables()) {
                                if (s.toString().contains(v.getNameAsString()+".add")) {
                                    // Composite Classes
                                    if (!compositeClasses.contains(className))
                                        compositeClasses.add(className);

                                    // Composite Methods
                                    if (compositeMethods.containsKey(className)) {
                                        if (!compositeMethods.get(className).contains(m))
                                            compositeMethods.get(className).add(m);
                                    } else {
                                        List<MethodDeclaration> methods = new ArrayList<>();
                                        methods.add(m);
                                        compositeMethods.put(className, methods);
                                    }

                                    // Composite Fields
                                    if (compositeFields.containsKey(className)) {
                                        if (!compositeFields.get(className).contains(f))
                                            compositeFields.get(className).add(f);
                                    } else {
                                        List<FieldDeclaration> fields = new ArrayList<>();
                                        fields.add(f);
                                        compositeFields.put(className, fields);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public String toString() {
        return className;
    }
}
