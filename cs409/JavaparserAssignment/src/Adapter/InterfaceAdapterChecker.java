package Adapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceAdapterChecker implements AdapterChecker {
    // Map of all methods in this class and the method calls within them
    private Map<String, List<String>> methods = new HashMap<>();

    private String className;
    private AdapterChecker root;

    public InterfaceAdapterChecker(String className, RootAdapterChecker root) {
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
        return true;
    }

    @Override
    public void findAdapters() {}

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
        return new ArrayList<>();
    }

    @Override
    public MethodDeclaration getCurrentMethod() {
        return null;
    }

    @Override
    public void addImplementedClass(ClassOrInterfaceType c) {}

    @Override
    public void setCurrentMethod(MethodDeclaration m) {}
}
