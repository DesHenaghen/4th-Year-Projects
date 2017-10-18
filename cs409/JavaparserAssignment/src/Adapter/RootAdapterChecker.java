package Adapter;

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
    public List<String> getAllClasses() {
        return allClasses;
    }
}
