package Decorator;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DecoratorChecker {
    default String getClassName() {
        return "";
    }
    DecoratorChecker getRoot();
    DecoratorChecker addClass(String name);
    default Map<String, DecoratorChecker> getClasses() {
        return new HashMap<>();
    }
    default List<String> getExtended() {
        return new ArrayList<>();
    }
    default List<FieldDeclaration> getFields() {
        return new ArrayList<>();
    }
    default MethodDeclaration getCurrentMethod() {
        return null;
    }
    default ExpressionStmt getCurrentExpression() {
        return null;
    }
    default List<MethodDeclaration> getApplicableMethods() {
        return new ArrayList<>();
    }
    default void flattenTrees() {}
    default void setCurrentMethod(MethodDeclaration m) {}
    default void setCurrentExpression(ExpressionStmt e) {}
    default void trimFields() {}
    default void determineDecoratorClasses() {}
    default void printDecoratorClasses(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods, Map<String, List<VariableDeclarator>> compositeFields) {}
    default void getDecoratorClassDetails(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods, Map<String, List<VariableDeclarator>> compositeFields){}
    default void getPossibleMethods(){}
    void findDecorators();
}
