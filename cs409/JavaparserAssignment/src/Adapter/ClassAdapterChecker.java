package Adapter;

import Adapter.AdapterChecker;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassAdapterChecker implements AdapterChecker {
    // All class/interfaces extended or implemented by this class
    private List<String> implemented = new ArrayList<>();
    // All fields in this class
    private List<VariableDeclarator> fields = new ArrayList<>();
    // Map of all methods in this class and the method calls within them
    private Map<String, List<String>> methods = new HashMap<>();

    private String className;
    private AdapterChecker root;

    MethodDeclaration currentMethod = null;

    public ClassAdapterChecker(String className, RootAdapterChecker root) {
        this.className = className;
        this.root = root;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public AdapterChecker addClass(String name) {
        return root.addClass(name);
    }

    @Override
    public AdapterChecker addInterface(String name) {
        return root.addInterface(name);
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public void findAdapters() {
        if (fields.size() > 0 && implemented != null) {
            boolean classIsAdapter = false;
            Map<String, AdapterChecker> interfaces = getInterfaces();
            for (String i : implemented) {
                //System.out.println(adapter.implemented);
                //System.out.println(root.interfaces);
                AdapterChecker intrfc = interfaces.get(i);

                //System.out.println(adapter.methods);
                //System.out.println(adapter.fields);
                //if (intrfc != null) System.out.println(intrfc.methods);

                boolean classIsValid = false;
                for (Map.Entry<String, List<String>> m : methods.entrySet()) {
                    // If this is a method from the interface
                    if (intrfc != null && intrfc.getMethods() != null && intrfc.getMethods().containsKey(m.getKey())) {
                        //System.out.println("METHOD: "+m.getKey());
                        //System.out.println("Hello?");
                        boolean methodIsValid = false;
                        // Check every expression in the method for a reference to a field
                        for (String expr : m.getValue()) {
                            for (VariableDeclarator field : fields) {
                                //System.out.println("FIELD: "+field+"; EXPRESSION: "+expr);
                                if (expr.contains(field.getNameAsString()) && getAllClasses().contains(field.getType().toString())) {
                                    //System.out.println("CLASS: "+adapter.getClassName()+"; METHOD: "+m.getKey()+"; CALL: "+expr);
                                    methodIsValid = true;
                                    break;
                                } else {
                                    //System.out.println("woops");
                                }
                            }
                        }
                        if (methodIsValid) {
                            classIsValid = true;
                            break;
                        }

                    }
                }
                if (classIsValid) {
                    classIsAdapter = true;
                    break;
                }
            }
            if (classIsAdapter) {
                System.out.println("Class Name: "+className);
            }
        }
    }

    @Override
    public Map<String, AdapterChecker> getInterfaces() {
        return root.getInterfaces();
    }

    @Override
    public Map<String, List<String>> getMethods() {
        return methods;
    }

    @Override
    public List<String> getAllClasses() {
        return root.getAllClasses();
    }

    @Override
    public List<VariableDeclarator> getFields() {
        return fields;
    }

    @Override
    public MethodDeclaration getCurrentMethod() {
        return currentMethod;
    }

    @Override
    public void addImplementedClass(ClassOrInterfaceType c) {
        implemented.add(c.getNameAsString());
    }

    @Override
    public void setCurrentMethod(MethodDeclaration m) {
        currentMethod = m;
    }
}
