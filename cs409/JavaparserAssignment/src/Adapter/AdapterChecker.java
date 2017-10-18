package Adapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AdapterChecker {
    default boolean isRoot() {
        return false;
    }
    default String getClassName() {
        return "";
    }
    AdapterChecker addClass(String name);
    AdapterChecker addInterface(String name);
    default boolean isInterface() { return false; };
    default void findAdapters() {};
    default Map<String, AdapterChecker> getInterfaces() {
        return new HashMap<>();
    }
    default Map<String, List<String>> getMethods() {
        return new HashMap<>();
    }
    default List<String> getAllClasses() {
        return new ArrayList<>();
    }
    default List<VariableDeclarator> getFields() {
        return new ArrayList<>();
    }
    default MethodDeclaration getCurrentMethod() {
        return null;
    }
    default void addImplementedClass(ClassOrInterfaceType c) {}
    default void setCurrentMethod(MethodDeclaration m) {}
}
