import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class AntlrRun {
    public static void main(String[] args) throws Exception {
        Java8Lexer lexer = new Java8Lexer(new ANTLRFileStream("./examples/helloworld.java"));
        Java8Parser parser = new Java8Parser(new CommonTokenStream(lexer));
        //System.out.println(parser.classDeclaration());
	     ParseTree tree = parser.compilationUnit();
		  //System.out.println("the package declaration is: " + tree.packageDeclaration().getText());
		  //System.out.println(tree.toStringTree());
		  //ParseTreeWalker walker = new ParseTreeWalker();
        //walker.walk( new PrettyPrintVisitor(), tree );
		  PrettyPrintVisitor theVisitor = new PrettyPrintVisitor(); //.visit(tree);
		  theVisitor.visit(tree);
    }
}
