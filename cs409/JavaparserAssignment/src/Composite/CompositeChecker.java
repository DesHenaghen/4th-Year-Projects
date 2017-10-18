package Composite;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CompositeChecker {
    default String getClassName() {
        return "";
    }
    CompositeChecker getRoot();
    CompositeChecker addClass(String name);
    default Map<String, CompositeChecker> getClasses() {
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
    void flattenTrees();
    default void setCurrentMethod(MethodDeclaration m) {}
    default void setCurrentExpression(ExpressionStmt e) {}
    default void determineCompositeClasses() {}
    default void printCompositeClasses(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods, Map<String, List<FieldDeclaration>> compositeFields) {}
    default void getCompositeClassDetails(List<String> compositeClasses, Map<String, List<MethodDeclaration>> compositeMethods , Map<String, List<FieldDeclaration>> compositeFields){}
}
