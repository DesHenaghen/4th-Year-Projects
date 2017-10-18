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

public class ClassCompositeChecker implements CompositeChecker{
    List<String> extended = new ArrayList<>();
    List<String> implemented = new ArrayList<>();

    List<FieldDeclaration> fields = new ArrayList<>();

    List<MethodDeclaration> applicableMethods = new ArrayList<>();

    MethodDeclaration currentMethod = null;
    ExpressionStmt currentExpression = null;

    // Instance fields
    private String className = null;
    private CompositeChecker root = null;
    private boolean isComposite = false;

    /**
     * Constructor for a Singleton Checker of a specified class
     *
     * @param name - Name of the class
     * @param s - Singleton.SingletonCheckerOld passed into visitor
     */
    public ClassCompositeChecker(String name, CompositeChecker s) {
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
    public CompositeChecker getRoot() {
        return root;
    }

    /**
     * Adds a Singleton.SingletonCheckerOld for the specified class to the list of classes
     * @param name
     * @return
     */
    @Override
    public CompositeChecker addClass(String name){
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
                CompositeChecker c = root.getClasses().get(i);
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

    @Override
    public void getCompositeClassDetails(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods ,Map<String, List<FieldDeclaration>> compositeFields) {
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
