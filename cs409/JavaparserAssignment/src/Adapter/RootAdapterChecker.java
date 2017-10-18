package Adapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootAdapterChecker implements AdapterChecker {
    // List of the names of all classes & interfaces
    private List<String> allClasses = new ArrayList<>();

    // List of all possible adapter classes
    private Map<String, AdapterChecker> possibleAdapters = new HashMap<>();

    // List of all interfaces
    private Map<String, AdapterChecker> interfaces = new HashMap<>();

    public RootAdapterChecker() {}

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public AdapterChecker addClass(String name) {
        AdapterChecker sc = new ClassAdapterChecker(name, this);
        possibleAdapters.put(name, sc);

        return sc;
    }

    @Override
    public AdapterChecker addInterface(String name) {
        AdapterChecker sc = new InterfaceAdapterChecker(name, this);
        interfaces.put(name, sc);

        return sc;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public void findAdapters() {
        for (Map.Entry<String, AdapterChecker> c : possibleAdapters.entrySet()) {
            c.getValue().findAdapters();
        }
    }

    @Override
    public Map<String, AdapterChecker> getInterfaces() {
        return interfaces;
    }

    @Override
    public Map<String, List<String>> getMethods() {
        return new HashMap<>();
    }

    @Override
    public List<String> getAllClasses() {
        return allClasses;
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
