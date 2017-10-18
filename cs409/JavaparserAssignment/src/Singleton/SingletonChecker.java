package Singleton;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public interface SingletonChecker {
    default String getClassName() {
        return "";
    }
    SingletonChecker getRoot();
    default void foundStaticInstanceField(FieldDeclaration f) {}
    default void foundStaticInstanceMethod(MethodDeclaration m) {}
    void isSingleton();
    SingletonChecker addClass(String n);
    default List<SingletonChecker> getClasses() {
        return new ArrayList<>();
    }
}
