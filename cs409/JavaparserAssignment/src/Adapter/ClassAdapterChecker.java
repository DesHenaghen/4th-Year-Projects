package Adapter;

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
    public void findAdapters() {
        if (fields.size() > 0 && implemented != null) {
            Map<String, AdapterChecker> interfaces = getInterfaces();
            // Maps target classes to fields to methods
            Map<AdapterChecker, Map<VariableDeclarator, List<String>>> adapterDetails = new HashMap<>();
            boolean classIsAdapter = false;

            // For every interface/class that is implemented or extended by this class
            for (String i : implemented) {
                AdapterChecker intrfc = interfaces.get(i);

                // Loop through the methods in this class
                for (Map.Entry<String, List<String>> m : methods.entrySet()) {
                    // If this is a method from the interface/class
                    if (intrfc != null && intrfc.getMethods() != null && intrfc.getMethods().containsKey(m.getKey())) {
                        // Check every expression in the method for a method call on a field of type that is creaeted in this project
                        for (String expr : m.getValue()) {
                            for (VariableDeclarator field : fields) {
                                if (expr.contains(field.getNameAsString()) && getAllClasses().contains(field.getType().toString())) {
                                    addAdapterDetails(adapterDetails, intrfc, m.getKey(), field);
                                    classIsAdapter = true;
                                }
                            }
                        }
                    }
                }
            }

            if (classIsAdapter)
                printAdapterDetails(adapterDetails);
        }
    }

    /**
     *
     * @param adapterDetails
     */
    private void printAdapterDetails(Map<AdapterChecker, Map<VariableDeclarator, List<String>>> adapterDetails) {
        System.out.println("Adapter/ConcreteCommand Class: "+className);
        for (Map.Entry<AdapterChecker, Map<VariableDeclarator, List<String>>> intrfc : adapterDetails.entrySet()) {
            AdapterChecker target = intrfc.getKey();
            System.out.println("Target/Command Class: "+target.getClassName());
            for (Map.Entry<VariableDeclarator, List<String>> fieldMapping : intrfc.getValue().entrySet()) {
                VariableDeclarator field = fieldMapping.getKey();
                System.out.println("Adaptee/Receiver Field: "+field.getType()+" "+field.getNameAsString());
                System.out.print("Request/Execute Method(s):");
                for (String method : fieldMapping.getValue()) {
                    System.out.print(" "+method);
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     *
     * @param adapterDetails
     * @param intrfc
     * @param method
     * @param field
     */
    private void addAdapterDetails(Map<AdapterChecker, Map<VariableDeclarator, List<String>>> adapterDetails, AdapterChecker intrfc, String method, VariableDeclarator field) {
        // If a mapping already exists for this target
        if (adapterDetails.containsKey(intrfc)) {
            Map<VariableDeclarator, List<String>> targetDetails = adapterDetails.get(intrfc);

            // If a mapping already exists for this field
            if (targetDetails.containsKey(field)){
                // Add this method to the mapping if it doesn't already exist
                if (!targetDetails.get(field).contains(method))
                    targetDetails.get(field).add(method);
            } else {
                // Create mapping and add method to it
                List<String> methods = new ArrayList<>();
                methods.add(method);
                targetDetails.put(field, methods);
            }

        } else {
            // Create mapping and add field mapping with method to it
            Map<VariableDeclarator, List<String>> fieldMapping = new HashMap<>();
            List<String> methods = new ArrayList<>();
            methods.add(method);
            fieldMapping.put(field, methods);

            adapterDetails.put(intrfc, fieldMapping);
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
