package Adapter;

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

}
