import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;

public class VariablePrinter {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("ArrayQueue.java");

        // parse it
        CompilationUnit cu = JavaParser.parse(in);

        System.out.println("---------- VARIABLES ----------");
        // visit and print variable names
        cu.accept(new VariableVisitor(), null);

        System.out.println();
        System.out.println("----------- FIELDS -----------");
        // visit and print variable names
        cu.accept(new FieldVisitor(), null);
    }

    /**
     * Simple visitor implementation for visiting VariableDeclarator nodes.
     */
    private static class VariableVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(VariableDeclarator n, Void arg) {
            System.out.println("Type: "+n.getType()+" Name: "+n.getName());
            super.visit(n, arg);
        }
    }

    /**
     * Simple visitor implementation for visiting FieldDeclaration nodes
     */
    private static class FieldVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(FieldDeclaration n, Void arg) {
            System.out.println(n.getVariables());
            super.visit(n, arg);
        }
    }
}