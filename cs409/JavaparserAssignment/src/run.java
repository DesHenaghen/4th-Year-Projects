import Adapter.AdapterChecker;
import Adapter.AdapterDetector;
import Adapter.RootAdapterChecker;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class run {

    /**
     * Iterates through all java files in the path specified and checks in each file for the existence of Singletons
     * @param projectDir - Path to the directory or file to process
     */
    public static void listSingletons(File projectDir) {
        // Root SingletonChecker
        SingletonChecker sc = new SingletonChecker();

        // Explore the directory structure parsing any java files found and checking for Singletons
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            try {
                // Read file in
                FileInputStream in = new FileInputStream(file);

                // parse it
                CompilationUnit cu = JavaParser.parse(in);

                // Visit the compilation unit to detect any Singletons
                VoidVisitor<SingletonChecker> singletonDetector = new SingletonDetector();
                singletonDetector.visit(cu, sc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);

        printSingletons(sc);
    }

    /**
     * Iterates through all java files in the path specified and checks in each file for the existence of Singletons
     * @param projectDir - Path to the directory or file to process
     */
    public static void listComposites(File projectDir) {
        // Root CompositeChecker
        CompositeChecker cc = new CompositeChecker();

        // Explore the directory structure parsing any java files found and checking for Singletons
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            try {
                // Read file in
                FileInputStream in = new FileInputStream(file);

                // parse it
                CompilationUnit cu = JavaParser.parse(in);

                // Visit the compilation unit to detect any Composites
                VoidVisitor<CompositeChecker> compositeDetector = new CompositeDetector();
                compositeDetector.visit(cu, cc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);

        printComposites(cc);
    }

    /**
     *
     * @param projectDir
     */
    public static void listExtendsConcreteType(File projectDir) {
        ExtendsConcreteTypeChecker ec = new ExtendsConcreteTypeChecker();

        // Explore the directory structure parsing any java files found and checking for Singletons
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            try {
                // Read file in
                FileInputStream in = new FileInputStream(file);

                // parse it
                CompilationUnit cu = JavaParser.parse(in);

                // Visit the compilation unit to detect any Singletons
                VoidVisitor<ExtendsConcreteTypeChecker> extendsConcreteDetector = new ExtendsConcreteTypeDetector();
                extendsConcreteDetector.visit(cu, ec);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);

        printExtendsConcreteType(ec);
    }

    /**
     *
     * @param projectDir
     */
    public static void listAdapters(File projectDir) {
        AdapterChecker ac = new RootAdapterChecker();

        // Explore the directory structure parsing any java files found and checking for Singletons
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            //System.out.println(path);
            try {
                // Read file in
                FileInputStream in = new FileInputStream(file);

                // parse it
                CompilationUnit cu = JavaParser.parse(in);

                // Visit the compilation unit to detect any Singletons
                VoidVisitor<AdapterChecker> adapterDetector = new AdapterDetector();
                adapterDetector.visit(cu, ac);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);

        printAdapters(ac);
    }

    public static void listAll(File projectDir) {
        ExtendsConcreteTypeChecker ec = new ExtendsConcreteTypeChecker();
        CompositeChecker cc = new CompositeChecker();
        SingletonChecker sc = new SingletonChecker();
        AdapterChecker ac = new RootAdapterChecker();

        // Explore the directory structure parsing any java files found and checking for Singletons
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            //System.out.println(path);
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);

        printSingletons(sc);
        printComposites(cc);
        printAdapters(ac);
        printExtendsConcreteType(ec);
    }

    private static void printSingletons(SingletonChecker sc) {
        System.out.println("\n--------------- Singleton Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for being a Singleton
        for (SingletonChecker s : sc.classes) {
            s.isSingleton();
        }
    }

    private static void printAdapters(AdapterChecker ac) {
        System.out.println("\n--------------- Adapter Patterns Detected -----------------");
        System.out.println();

        ac.findAdapters();
    }

    public static void printComposites(CompositeChecker cc) {
        System.out.println("\n--------------- Composite Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for being a Composite
        cc.flattenTrees();
        cc.determineCompositeClasses();
    }

    private static void printExtendsConcreteType(ExtendsConcreteTypeChecker ec) {
        System.out.println("\n--------------- Extends Concrete Type Anti-Patterns Detected -----------------");
        System.out.println();

        // Check every class processed for extending a concrete type
        ec.listClassesExtendConcrete();
    }

    public static void main(String[] args) {
        File projectDir = new File("D:\\Libraries\\Documents\\JHotDraw5.1\\sources\\CH");
        //listSingletons(projectDir);
        //listExtendsConcreteType(projectDir);
        //listComposites(projectDir);
        //listAdapters(projectDir);
        listAll(projectDir);
    }
}
