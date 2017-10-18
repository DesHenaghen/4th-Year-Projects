package Adapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.List;
import java.util.Map;

public interface AdapterChecker {
    boolean isRoot();
    String getClassName();
    AdapterChecker addClass(String name);
    AdapterChecker addInterface(String name);
    boolean isInterface();
    void findAdapters();
    Map<String, AdapterChecker> getInterfaces();
    Map<String, List<String>> getMethods();
    List<String> getAllClasses();
    List<VariableDeclarator> getFields();
    MethodDeclaration getCurrentMethod();
    void addImplementedClass(ClassOrInterfaceType c);
    void setCurrentMethod(MethodDeclaration m);
}
