import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class AntlrRun {
    public static void main(String[] args) throws Exception {
        Java8Lexer lexer = new Java8Lexer(new ANTLRFileStream("./examples/helloworld.java"));
        Java8Parser parser = new Java8Parser(new CommonTokenStream(lexer));
        //System.out.println(parser.classDeclaration());
	     ParseTree tree = parser.compilationUnit();
		  //System.out.println(tree.toStringTree());
		  ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk( new Java8Walker(), tree );
    }
}
