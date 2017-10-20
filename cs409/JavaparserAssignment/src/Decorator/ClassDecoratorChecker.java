package Decorator;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassDecoratorChecker implements DecoratorChecker {
    List<String> extended = new ArrayList<>();

    List<FieldDeclaration> fields = new ArrayList<>();
    List<VariableDeclarator> validFields = new ArrayList<>();

    List<MethodDeclaration> applicableMethods = new ArrayList<>();
    List<String> possibleMethods = new ArrayList<>();

    MethodDeclaration currentMethod = null;
    ExpressionStmt currentExpression = null;

    // Instance fields
    private String className = null;
    private DecoratorChecker root = null;
    private boolean isComposite = false;

    /**
     * Constructor for a Singleton Checker of a specified class
     *
     * @param name - Name of the class
     * @param s - Singleton.SingletonCheckerOld passed into visitor
     */
    public ClassDecoratorChecker(String name, DecoratorChecker s) {
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
     * @return - The Singleton.SingletonCheckerOld instance that is the root object
     */
    @Override
    public DecoratorChecker getRoot() {
        return root;
    }

    /**
     * Adds a Singleton.SingletonCheckerOld for the specified class to the list of classes
     * @param name
     * @return
     */
    @Override
    public DecoratorChecker addClass(String name){
        return root.addClass(name);
    }

    @Override
    public List<String> getExtended() {
        return extended;
    }

    @Override
    public List<FieldDeclaration> getFields() {
        return fields;
    }

    @Override
    public MethodDeclaration getCurrentMethod() {
        return currentMethod;
    }

    @Override
    public ExpressionStmt getCurrentExpression() {
        return currentExpression;
    }

    @Override
    public List<MethodDeclaration> getApplicableMethods() {
        return applicableMethods;
    }

    @Override
    public void setCurrentMethod(MethodDeclaration m) {
        currentMethod = m;
    }

    @Override
    public void setCurrentExpression(ExpressionStmt e) {
        currentExpression = e;
    }

    @Override
    public void flattenTrees(){
        List<String> temp = new ArrayList<>();
        // Check every extended/implemented class for further extensions or implements
        for (String i : extended) {
            // If class exists
            if (root.getClasses().containsKey(i)) {
                DecoratorChecker c = root.getClasses().get(i);
                // Loop through all classes it extends/implements
                for (String e : c.getExtended()) {
                    // If it's a new one, add it to the list
                    if (!extended.contains(e)) {
                        temp.add(e);
                    }
                }
            }
        }

        extended.addAll(temp);
    }

    /**
     * Removes all fields that aren't of the same type as one of the implemented/extended classes
     */
    @Override
    public void trimFields() {
        for (FieldDeclaration f : fields) {
            for (VariableDeclarator v : f.getVariables()) {
                // If variable has type of implement/extend class
                if (extended.contains(v.getType().toString())) {
                    validFields.add(v);
                }
            }
        }
    }

    @Override
    public void getPossibleMethods() {
        for (String i : extended) {
            if (root.getClasses().containsKey(i)) {
                DecoratorChecker c = root.getClasses().get(i);
                for (MethodDeclaration m : c.getApplicableMethods()) {
                    possibleMethods.add(m.getNameAsString());
                }
            }
        }
    }

    @Override
    public void findDecorators() {
        flattenTrees();
        trimFields();
        getPossibleMethods();
    }

    @Override
    public void getDecoratorClassDetails(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods , Map<String, List<VariableDeclarator>> compositeFields) {
        for (MethodDeclaration m : applicableMethods) {
            if (possibleMethods.contains(m.getNameAsString())) {
                m.getBody().ifPresent(b -> {
                    for (Statement s : b.getStatements()) {
                        for (VariableDeclarator v : validFields) {
                            if (s.toString().contains(v.getNameAsString() + "." + m.getNameAsString())) {
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
                                    if (!compositeFields.get(className).contains(v))
                                        compositeFields.get(className).add(v);
                                } else {
                                    List<VariableDeclarator> fields = new ArrayList<>();
                                    fields.add(v);
                                    compositeFields.put(className, fields);
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
