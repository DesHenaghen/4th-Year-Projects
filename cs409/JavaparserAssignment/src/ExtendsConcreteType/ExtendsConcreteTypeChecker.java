package ExtendsConcreteType;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendsConcreteTypeChecker {
    // HashMap of all classes that extend a class
    private Map<String, NodeList<ClassOrInterfaceType>> extendingClasses = new HashMap<String, NodeList<ClassOrInterfaceType>>();

    // ArrayList of all concrete classes
    private List<String> concreteClasses = new ArrayList<>();

    public void addExtendingClass(String name, NodeList<ClassOrInterfaceType> types){
        extendingClasses.put(name, types);
    }

    public void addConcreteClass(String name){
        concreteClasses.add(name);
    }

    public void listClassesExtendConcrete(){
        //int i = 0;

        for (Map.Entry<String, NodeList<ClassOrInterfaceType>> entry : extendingClasses.entrySet()) {
            String key = entry.getKey();
            NodeList<ClassOrInterfaceType> types = entry.getValue();

            for(ClassOrInterfaceType c : types){
                String className = c.getNameAsString();
                if (concreteClasses.contains(className)){
                    System.out.println("Class Name: "+key);
                    System.out.println("Extended Concrete Class: "+className);
                    System.out.println();
                    //i++;
                }
            }
        }
        //System.out.println("TOTAL: "+i);
    }
}
