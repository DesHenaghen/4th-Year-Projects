import Adapter.AdapterChecker;
import Adapter.AdapterDetector;
import Adapter.RootAdapterChecker;
import Composite.CompositeChecker;
import Composite.CompositeDetector;
import Composite.RootCompositeChecker;
import Decorator.DecoratorChecker;
import Decorator.DecoratorDetector;
import Decorator.RootDecoratorChecker;
import ExtendsConcreteType.ExtendsConcreteTypeDetector;
import Singleton.RootSingletonChecker;
import Singleton.SingletonChecker;
import Singleton.SingletonDetector;
import ExtendsConcreteType.ExtendsConcreteTypeChecker;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.*;

public class RunPatternDetectors {

    private static void listAll(File projectDir) {
        ExtendsConcreteTypeChecker ec = new ExtendsConcreteTypeChecker();
        CompositeChecker cc = new RootCompositeChecker();
        SingletonChecker sc = new RootSingletonChecker();
        AdapterChecker ac = new RootAdapterChecker();
        DecoratorChecker dc = new RootDecoratorChecker();

        // Explore the directory structure parsing any java files found and checking for patterns
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                // Read file in
                FileInputStream in = new FileInputStream(file);

                // parse it
                CompilationUnit cu = JavaParser.parse(in);

                // Visit the compilation unit to detect any extensions of concrete types
                VoidVisitor<ExtendsConcreteTypeChecker> extendsConcreteDetector = new ExtendsConcreteTypeDetector();
                extendsConcreteDetector.visit(cu, ec);

                // Visit the compilation unit to detect any Singletons
                VoidVisitor<SingletonChecker> singletonDetector = new SingletonDetector();
                singletonDetector.visit(cu, sc);

                // Visit the compilation unit to detect any Composites
                VoidVisitor<CompositeChecker> compositeDetector = new CompositeDetector();
                compositeDetector.visit(cu, cc);

                // Visit the compilation unit to detect any Adapters
                VoidVisitor<AdapterChecker> adapterDetector = new AdapterDetector();
                adapterDetector.visit(cu, ac);

                // Visit the compilation unit to detect any Adapters
                VoidVisitor<DecoratorChecker> decoratorDetector = new DecoratorDetector();
                decoratorDetector.visit(cu, dc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);

        printSingletons(sc);
        printComposites(cc);
        printAdapters(ac);
        printDecorators(dc);
        printExtendsConcreteType(ec);
    }

    private static void printSingletons(SingletonChecker sc) {
        System.out.println("\n--------------- Singleton Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for being a Singleton
        for (SingletonChecker s : sc.getClasses()) {
            s.isSingleton();
        }
    }

    private static void printAdapters(AdapterChecker ac) {
        System.out.println("\n--------------- Object Adapter/Command Patterns Detected -----------------");
        System.out.println();

        ac.findAdapters();
    }

    private static void printComposites(CompositeChecker cc) {
        System.out.println("\n--------------- Composite Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for being a Composite
        cc.findComposites();
    }

    private static void printDecorators(DecoratorChecker dc) {
        System.out.println("\n--------------- Decorator Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for being a Decorator
        dc.findDecorators();
    }

    private static void printExtendsConcreteType(ExtendsConcreteTypeChecker ec) {
        System.out.println("\n--------------- Extends Concrete Type Anti-Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for extending a concrete type
        ec.findClassesExtendConcrete();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the file path of the Java project you would like to detect patterns in: ");
        String filePath = reader.readLine();
        listAll(new File(filePath));
    }
}
