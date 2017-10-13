import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;


import java.io.FileInputStream;

public class run {
    public static void main (String[] args) throws Exception{
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("ArrayQueue.java");

        // parse it
        CompilationUnit cu = JavaParser.parse(in);

        System.out.println("---------- METHODS ----------");
        // visit and print variable names
        cu.accept(new MethodVisitor(), null);
    }
}
