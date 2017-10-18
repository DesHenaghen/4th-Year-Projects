package ExtendsConcreteType;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ExtendsConcreteTypeDetector extends VoidVisitorAdapter<ExtendsConcreteTypeChecker>{

    /**
     * Adds a Singleton.SingletonCheckerOld to the root Singleton.SingletonCheckerOld object for the current class
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, ExtendsConcreteTypeChecker arg) {
        NodeList<ClassOrInterfaceType> extendedTypes = n.getExtendedTypes();

        // If class extends another class, add it to list of classes that extend
        if (extendedTypes.size() > 0) {
            arg.addExtendingClass(n.getNameAsString(), extendedTypes);
        }

        // If class isn't abstract or an interface, it must be a concrete class
        if (!n.isAbstract() && !n.isInterface()) {
            arg.addConcreteClass(n.getNameAsString());
        }

        super.visit(n, arg);
    }
}
