// Generated from Java8.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
/**
 *This class visits nodes in the parse tree and pretty prints them
 * TODO: Will eventually integrate the random compiler components. 
 */
public class PrettyPrintVisitor<T> extends AbstractParseTreeVisitor<T> implements Java8Visitor<T> {

	/*placed prints here at the top so that they can easily be switched out with 'good' (i.e. to files and such)
	prints*/
	private void print(String in){
		System.out.print(in);
	}

	private void print(char in){
		System.out.print(in);
	}

	private void println(String in){
		System.out.println(in);
	}

	private void println(char in){
		System.out.println(in);
	}

	private void println(){
		System.out.println();
	}

	private <M> List<M> makeSafe(List<M> in){
		if(in == null) {
			return new ArrayList<M>();
		}
		else {
			return in;
		}
	}

	private T safeVisit(ParseTree in){
		if(in != null){
			return visit(in);
		}
		return null;
	}

	private void safePrint(ParseTree in){
		if(in != null){
			print(in.getText());
		}
	}

	private String safeGetText(ParseTree in){
		if(in != null){
			return in.getText();
		}
		return "";
	}

	private <M extends ParseTree> T safeVisitList(List<M> in){
		for(ParseTree child : makeSafe(in)){
			visit(child);
		}
		return null;
	}

	private T delimitedVisitListTerm(List<TerminalNode> in, String delimiter){
		if(in != null){
			for(int i = 0; i < in.size(); i ++){
				print(in.get(i).getText());
				if(i < in.size() - 1){
					print(delimiter);
				}
			} 
		}
		return null;
	}

	private T preDelimitedVisitListTerm(List<TerminalNode> in, String delimiter){
		if(in != null){
			for(int i = 0; i < in.size(); i ++){
				print(delimiter);
				print(in.get(i).getText());
			} 
		}
		return null;
	}

	private T fullDelimitedVisitListTerm(List<TerminalNode> in, String delimiter){
		if(in != null){
			for(int i = 0; i < in.size(); i ++){
				print(in.get(i).getText());
				print(delimiter);
			} 
		}
		return null;
	}

	private <M extends ParseTree> T delimitedVisitList(List<M> in, String delimiter){
		if(in != null){
			for(int i = 0; i < in.size(); i ++){
				visit(in.get(i));
				if(i < in.size() - 1){
					print(delimiter);
				}
			} 
		}
		return null;
	}

	private <M extends ParseTree> T preDelimitedVisitList(List<M> in, String delimiter){
		if(in != null){
			for(int i = 0; i < in.size(); i ++){
				print(delimiter);
				visit(in.get(i));
			} 
		}
		return null;
	}

	private <M extends ParseTree> T fullDelimitedVisitList(List<M> in, String delimiter){
		if(in != null){
			for(int i = 0; i < in.size(); i ++){
				visit(in.get(i));
				print(delimiter);
			} 
		}
		return null;
	}

	int countChars(String in, String in2){
		int count = in.length() - in.replace(in2, "").length();
		return count;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLiteral(Java8Parser.LiteralContext ctx) {
		safePrint(ctx.IntegerLiteral());
		safePrint(ctx.FloatingPointLiteral());
		safePrint(ctx.BooleanLiteral());
		safePrint(ctx.CharacterLiteral());
		safePrint(ctx.StringLiteral());
		safePrint(ctx.NullLiteral());
		print(' ');
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitType(Java8Parser.TypeContext ctx) {
		safeVisit(ctx.primitiveType());
		safeVisit(ctx.referenceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimitiveType(Java8Parser.PrimitiveTypeContext ctx) {
		safeVisitList(ctx.annotation());
		safeVisit(ctx.numericType());
		if(ctx.numericType() == null){
			print("boolean ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitNumericType(Java8Parser.NumericTypeContext ctx) {
		safeVisit(ctx.integralType());
		safeVisit(ctx.floatingPointType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIntegralType(Java8Parser.IntegralTypeContext ctx) {
		print(ctx.getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFloatingPointType(Java8Parser.FloatingPointTypeContext ctx) {
		print(ctx.getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitReferenceType(Java8Parser.ReferenceTypeContext ctx) {
		safeVisit(ctx.classOrInterfaceType());
		safeVisit(ctx.typeVariable());
		safeVisit(ctx.arrayType());	
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassOrInterfaceType(Java8Parser.ClassOrInterfaceTypeContext ctx) { 
		safeVisit(ctx.classType_lfno_classOrInterfaceType());
		safeVisit(ctx.interfaceType_lfno_classOrInterfaceType());
		safeVisitList(ctx.classType_lf_classOrInterfaceType());
		safeVisitList(ctx.interfaceType_lf_classOrInterfaceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassType(Java8Parser.ClassTypeContext ctx) {
		if(ctx.classOrInterfaceType() != null){
			visit(ctx.classOrInterfaceType());
			print(".");
		}
		safeVisitList(ctx.annotation());
		print(ctx.Identifier().getText() + " ");
		safeVisit(ctx.typeArguments());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassType_lf_classOrInterfaceType(Java8Parser.ClassType_lf_classOrInterfaceTypeContext ctx) {
		print(".");
		safeVisitList(ctx.annotation());
		print(ctx.Identifier().getText() + " ");
		safeVisit(ctx.typeArguments());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassType_lfno_classOrInterfaceType(Java8Parser.ClassType_lfno_classOrInterfaceTypeContext ctx) {
		safeVisitList(ctx.annotation());
		print(ctx.Identifier().getText() + " ");
		safeVisit(ctx.typeArguments());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceType(Java8Parser.InterfaceTypeContext ctx) { 
		visit(ctx.classType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceType_lf_classOrInterfaceType(Java8Parser.InterfaceType_lf_classOrInterfaceTypeContext ctx) { 
		visit(ctx.classType_lf_classOrInterfaceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceType_lfno_classOrInterfaceType(Java8Parser.InterfaceType_lfno_classOrInterfaceTypeContext ctx) { 
		visit(ctx.classType_lfno_classOrInterfaceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeVariable(Java8Parser.TypeVariableContext ctx) { 
		safeVisitList(ctx.annotation());
		print(ctx.Identifier().getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArrayType(Java8Parser.ArrayTypeContext ctx) {
		safeVisit(ctx.primitiveType());
		safeVisit(ctx.classOrInterfaceType());
		safeVisit(ctx.typeVariable());
		print(" ");
		safeVisit(ctx.dims());
		print(" ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDims(Java8Parser.DimsContext ctx) { 
		fullDelimitedVisitList(ctx.annotation(), "[]");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeParameter(Java8Parser.TypeParameterContext ctx) { 
		
		safeVisitList(ctx.typeParameterModifier());
		print(ctx.Identifier().getText() + ' ');
		safeVisit(ctx.typeBound());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeParameterModifier(Java8Parser.TypeParameterModifierContext ctx) {
		visit(ctx.annotation());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeBound(Java8Parser.TypeBoundContext ctx) {
		print("extends ");
		safeVisit(ctx.typeVariable());
		safeVisit(ctx.classOrInterfaceType());
		safeVisitList(ctx.additionalBound());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAdditionalBound(Java8Parser.AdditionalBoundContext ctx) {
		print("&");
		visit(ctx.interfaceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeArguments(Java8Parser.TypeArgumentsContext ctx) {
		print("<");
		visit(ctx.typeArgumentList());
		print("> ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeArgumentList(Java8Parser.TypeArgumentListContext ctx) { 
		delimitedVisitList(ctx.typeArgument(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeArgument(Java8Parser.TypeArgumentContext ctx) {
		safeVisit(ctx.referenceType());
		safeVisit(ctx.wildcard());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitWildcard(Java8Parser.WildcardContext ctx) { 
		safeVisitList(ctx.annotation());
		print("? ");
		safeVisit(ctx.wildcardBounds());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitWildcardBounds(Java8Parser.WildcardBoundsContext ctx) {
		safeVisit(ctx.extendWildcardBounds());
		safeVisit(ctx.superWildcardBounds());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */	
	@Override public T visitExtendWildcardBounds(Java8Parser.ExtendWildcardBoundsContext ctx) {
		print("extend ");
		visit(ctx.referenceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSuperWildcardBounds(Java8Parser.SuperWildcardBoundsContext ctx) {
		print("super ");
		visit(ctx.referenceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPackageName(Java8Parser.PackageNameContext ctx) { 
		if(ctx.packageName() != null){
			visit(ctx.packageName());
			print(".");
		}
		print(ctx.Identifier().getText());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeName(Java8Parser.TypeNameContext ctx) { 
		if(ctx.packageOrTypeName() != null){
			visit(ctx.packageOrTypeName());
			print(".");
		}
		print(ctx.Identifier().getText());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPackageOrTypeName(Java8Parser.PackageOrTypeNameContext ctx) {
		if(ctx.packageOrTypeName() != null){
			visit(ctx.packageOrTypeName());
			print(".");
		}
		print(ctx.Identifier().getText());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExpressionName(Java8Parser.ExpressionNameContext ctx) { 
		if(ctx.ambiguousName() != null){
			visit(ctx.ambiguousName());
			print(".");
		}
		print(ctx.Identifier().getText());
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodName(Java8Parser.MethodNameContext ctx) { 
		print(ctx.Identifier().getText());
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAmbiguousName(Java8Parser.AmbiguousNameContext ctx) { 
		if(ctx.ambiguousName() != null){
			visit(ctx.ambiguousName());
			print(".");
		}
		print(ctx.Identifier().getText());
		return null;		
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCompilationUnit(Java8Parser.CompilationUnitContext ctx) { 
		safeVisit(ctx.packageDeclaration());
		safeVisitList(ctx.importDeclaration());
		safeVisitList(ctx.typeDeclaration());	
		return null;
	}

	@Override public T visitPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {	
		safeVisitList(ctx.packageModifier());
		print("package ");		
		delimitedVisitListTerm(ctx.Identifier(), ".");
      println(";");
		println();
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPackageModifier(Java8Parser.PackageModifierContext ctx) {
		visit(ctx.annotation());		
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitImportDeclaration(Java8Parser.ImportDeclarationContext ctx) {	
		print ("import ");
		safeVisit(ctx.singleTypeImportDeclaration());
		safeVisit(ctx.typeImportOnDemandDeclaration());
		safeVisit(ctx.singleStaticImportDeclaration());
		safeVisit(ctx.staticImportOnDemandDeclaration());
		println(';'); 
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSingleTypeImportDeclaration (Java8Parser.SingleTypeImportDeclarationContext ctx) { 
		visit(ctx.typeName());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeImportOnDemandDeclaration(Java8Parser.TypeImportOnDemandDeclarationContext ctx) {
		visit(ctx.packageOrTypeName());
		print(".*");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSingleStaticImportDeclaration(Java8Parser.SingleStaticImportDeclarationContext ctx) {
		print("static ");
		visit(ctx.typeName());
		print('.' + ctx.Identifier().getText());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStaticImportOnDemandDeclaration(Java8Parser.StaticImportOnDemandDeclarationContext ctx) {
		print("static ");
		visit(ctx.typeName());
		print(".*");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeDeclaration(Java8Parser.TypeDeclarationContext ctx) {
		safeVisit(ctx.classDeclaration());
		safeVisit(ctx.interfaceDeclaration());
		if(ctx.classDeclaration() == null && ctx.interfaceDeclaration() == null){
			println(";");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) { 
		safeVisit(ctx.normalClassDeclaration());
		safeVisit(ctx.enumDeclaration());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) { 
		println();	
		safeVisitList(ctx.classModifier());
		print("class ");
		print(ctx.Identifier().getText() + ' ');
		safeVisit(ctx.typeParameters());
		safeVisit(ctx.superclass());		
		safeVisit(ctx.superinterfaces());
		visit(ctx.classBody());

		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassModifier(Java8Parser.ClassModifierContext ctx) {
		print(ctx.getText() + ' ');
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeParameters(Java8Parser.TypeParametersContext ctx) {
		print('<');
		visit(ctx.typeParameterList());
		print("> ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeParameterList(Java8Parser.TypeParameterListContext ctx) {
		delimitedVisitList(ctx.typeParameter(), ", ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSuperclass(Java8Parser.SuperclassContext ctx) {
		print("extends ");
		visit(ctx.classType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSuperinterfaces(Java8Parser.SuperinterfacesContext ctx) {
		print("implements ");
		visit(ctx.interfaceTypeList());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceTypeList(Java8Parser.InterfaceTypeListContext ctx) { 
		delimitedVisitList(ctx.interfaceType(), ", ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassBody(Java8Parser.ClassBodyContext ctx) { 
		println('{');
		safeVisitList(ctx.classBodyDeclaration());
		println();
		println('}');
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassBodyDeclaration(Java8Parser.ClassBodyDeclarationContext ctx) {
		safeVisit(ctx.classMemberDeclaration());
		safeVisit(ctx.instanceInitializer());
		safeVisit(ctx.staticInitializer());
		safeVisit(ctx.constructorDeclaration());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassMemberDeclaration(Java8Parser.ClassMemberDeclarationContext ctx) {
		if(ctx.fieldDeclaration() == null 
			&& ctx.methodDeclaration() == null 
			&& ctx.classDeclaration() == null 
			&& ctx.interfaceDeclaration() == null){
			println(';');
		} else {
			safeVisit(ctx.fieldDeclaration());
			safeVisit(ctx.methodDeclaration());
			safeVisit(ctx.classDeclaration());
			safeVisit(ctx.interfaceDeclaration());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
		safeVisitList(ctx.fieldModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorList());
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFieldModifier(Java8Parser.FieldModifierContext ctx) {
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {
			print(ctx.getText() + " ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableDeclaratorList(Java8Parser.VariableDeclaratorListContext ctx) {
		delimitedVisitList(ctx.variableDeclarator(), ", ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableDeclarator(Java8Parser.VariableDeclaratorContext ctx) {
		visit(ctx.variableDeclaratorId());
		if(ctx.variableInitializer() != null){
			print(" = ");
			visit(ctx.variableInitializer());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableDeclaratorId(Java8Parser.VariableDeclaratorIdContext ctx) {
		print(ctx.Identifier().getText() + " ");
		safeVisit(ctx.dims());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableInitializer(Java8Parser.VariableInitializerContext ctx) {
		safeVisit(ctx.expression());
		safeVisit(ctx.arrayInitializer());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannType(Java8Parser.UnannTypeContext ctx) {
		safeVisit(ctx.unannPrimitiveType());
		safeVisit(ctx.unannReferenceType());
		print(" ");		
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannPrimitiveType(Java8Parser.UnannPrimitiveTypeContext ctx) {
		if(ctx.numericType() == null){
			print("boolean ");
		} else {
			visit(ctx.numericType());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannReferenceType(Java8Parser.UnannReferenceTypeContext ctx) { 
		safeVisit(ctx.unannClassOrInterfaceType());
		safeVisit(ctx.unannTypeVariable());		
		safeVisit(ctx.unannArrayType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannClassOrInterfaceType(Java8Parser.UnannClassOrInterfaceTypeContext ctx) {
		safeVisit(ctx.unannClassType_lfno_unannClassOrInterfaceType());
		safeVisit(ctx.unannInterfaceType_lfno_unannClassOrInterfaceType());
		safeVisitList(ctx.unannClassType_lf_unannClassOrInterfaceType());
		safeVisitList(ctx.unannInterfaceType_lf_unannClassOrInterfaceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannClassType(Java8Parser.UnannClassTypeContext ctx) {
		if(ctx.unannClassOrInterfaceType() != null) {		
			visit(ctx.unannClassOrInterfaceType());
			print(".");
			safeVisitList(ctx.annotation());
		}
		print(ctx.Identifier().getText());
		safeVisit(ctx.typeArguments());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannClassType_lf_unannClassOrInterfaceType(Java8Parser.UnannClassType_lf_unannClassOrInterfaceTypeContext ctx) {
		print(".");
		safeVisitList(ctx.annotation());
		print(ctx.Identifier().getText());
		safeVisit(ctx.typeArguments());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannClassType_lfno_unannClassOrInterfaceType(Java8Parser.UnannClassType_lfno_unannClassOrInterfaceTypeContext ctx) {
		print(ctx.Identifier().getText());
		safeVisit(ctx.typeArguments());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannInterfaceType(Java8Parser.UnannInterfaceTypeContext ctx) { 
		visit(ctx.unannClassType());
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannInterfaceType_lf_unannClassOrInterfaceType(Java8Parser.UnannInterfaceType_lf_unannClassOrInterfaceTypeContext ctx) {
		visit(ctx.unannClassType_lf_unannClassOrInterfaceType());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannInterfaceType_lfno_unannClassOrInterfaceType(Java8Parser.UnannInterfaceType_lfno_unannClassOrInterfaceTypeContext ctx) {
		visit(ctx.unannClassType_lfno_unannClassOrInterfaceType());
		return null;
 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannTypeVariable(Java8Parser.UnannTypeVariableContext ctx) {
		print(ctx.Identifier().getText());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnannArrayType(Java8Parser.UnannArrayTypeContext ctx) {
		safeVisit(ctx.unannPrimitiveType());
		safeVisit(ctx.unannClassOrInterfaceType());
		safeVisit(ctx.unannTypeVariable());	
		visit(ctx.dims());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
		safeVisitList(ctx.methodModifier());
		visit(ctx.methodHeader());
		visit(ctx.methodBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodModifier(Java8Parser.MethodModifierContext ctx) { 
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {
			print(ctx.getText() + " ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodHeader(Java8Parser.MethodHeaderContext ctx) {
		safeVisit(ctx.typeParameters());
		safeVisitList(ctx.annotation());
		visit(ctx.result());
		visit(ctx.methodDeclarator());
		safeVisit(ctx.throws_());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitResult(Java8Parser.ResultContext ctx) { 
		if(ctx.unannType() != null){
			visit(ctx.unannType());
		} else {
			print("void ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {
		print(ctx.Identifier() + " (");
		safeVisit(ctx.formalParameterList());
		print(") ");		
		safeVisit(ctx.dims());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFormalParameterList(Java8Parser.FormalParameterListContext ctx) { 
		if(ctx.formalParameters() != null){
			visit(ctx.formalParameters());
			print(", ");
		}
		visit(ctx.lastFormalParameter());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFormalParameters(Java8Parser.FormalParametersContext ctx) {
		if(ctx.receiverParameter() != null){
			visit(ctx.receiverParameter());
			print(", ");
		}
		delimitedVisitList(ctx.formalParameter(), ", ");
		return null;

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFormalParameter(Java8Parser.FormalParameterContext ctx) {
		safeVisitList(ctx.variableModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorId());
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableModifier(Java8Parser.VariableModifierContext ctx) { 
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {
			print("final ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLastFormalParameter(Java8Parser.LastFormalParameterContext ctx) {
		if(ctx.formalParameter() != null){
			visit(ctx.formalParameter());
		} else {		
			safeVisitList(ctx.variableModifier());
			visit(ctx.unannType());
			safeVisitList(ctx.annotation());
			print(" ... ");
			visit(ctx.variableDeclaratorId());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitReceiverParameter(Java8Parser.ReceiverParameterContext ctx) {
		safeVisitList(ctx.annotation());
		visit(ctx.unannType());
		print(ctx.Identifier().getText() + ".this");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitThrows_(Java8Parser.Throws_Context ctx) {
		print("throws ");
		visit(ctx.exceptionTypeList());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExceptionTypeList(Java8Parser.ExceptionTypeListContext ctx) {
		delimitedVisitList(ctx.exceptionType(), ", ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExceptionType(Java8Parser.ExceptionTypeContext ctx) {
		safeVisit(ctx.classType());
		safeVisit(ctx.typeVariable());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodBody(Java8Parser.MethodBodyContext ctx) { 
		if(ctx.block() == null){
			println(";");
		} else {
			visit(ctx.block());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInstanceInitializer(Java8Parser.InstanceInitializerContext ctx) {
		visit(ctx.block());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStaticInitializer(Java8Parser.StaticInitializerContext ctx) {
		print("static ");		
		visit(ctx.block());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstructorDeclaration(Java8Parser.ConstructorDeclarationContext ctx) {
		safeVisitList(ctx.constructorModifier());
		visit(ctx.constructorDeclarator());
		safeVisit(ctx.throws_());
		visit(ctx.constructorBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstructorModifier(Java8Parser.ConstructorModifierContext ctx) {
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {
			print(ctx.getText() + " ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstructorDeclarator(Java8Parser.ConstructorDeclaratorContext ctx) {
		safeVisit(ctx.typeParameters());
		visit(ctx.simpleTypeName());
		print("(");
		safeVisit(ctx.formalParameterList());
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSimpleTypeName(Java8Parser.SimpleTypeNameContext ctx) {
		print(ctx.getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstructorBody(Java8Parser.ConstructorBodyContext ctx) {
		println("{");
		safeVisit(ctx.explicitConstructorInvocation());
		safeVisit(ctx.blockStatements());
		println("}");
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExplicitConstructorInvocation(Java8Parser.ExplicitConstructorInvocationContext ctx) {
		safeVisit(ctx.expressionName());
		safeVisit(ctx.primary());
		if(ctx.expressionName() != null || ctx.primary() != null){
			print(".");
		}
		safeVisit(ctx.typeArguments());
		if (ctx.getText().toLowerCase().contains("this")) {
			print("this ");
		} else {
			print("super ");
		}
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumDeclaration(Java8Parser.EnumDeclarationContext ctx) {
		safeVisitList(ctx.classModifier());
		print("enum " + ctx.Identifier().getText() + " ");
		safeVisit(ctx.superinterfaces());
		visit(ctx.enumBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumBody(Java8Parser.EnumBodyContext ctx) {
		println("{");
		safeVisit(ctx.enumConstantList());
		if(ctx.getText().equals(safeGetText(ctx.enumConstantList()) + "," + safeGetText(ctx.enumBodyDeclarations()))) {
			print(",");
		}
		safeVisit(ctx.enumBodyDeclarations());
		println("}");
		println();
    	return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumConstantList(Java8Parser.EnumConstantListContext ctx) {
		delimitedVisitList(ctx.enumConstant(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumConstant(Java8Parser.EnumConstantContext ctx) {
		safeVisitList(ctx.enumConstantModifier());
		print(ctx.Identifier() + " (");
		safeVisit(ctx.argumentList());
		safeVisit(ctx.classBody());
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumConstantModifier(Java8Parser.EnumConstantModifierContext ctx) { 
		visit(ctx.annotation());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumBodyDeclarations(Java8Parser.EnumBodyDeclarationsContext ctx) {
		preDelimitedVisitList(ctx.classBodyDeclaration(),";\n"); 
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceDeclaration(Java8Parser.InterfaceDeclarationContext ctx) {
		safeVisit(ctx.normalInterfaceDeclaration());
		safeVisit(ctx.annotationTypeDeclaration());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitNormalInterfaceDeclaration(Java8Parser.NormalInterfaceDeclarationContext ctx) {
		safeVisitList(ctx.interfaceModifier());
		print("interface " + ctx.Identifier() + " ");
		safeVisit(ctx.typeParameters());
		safeVisit(ctx.extendsInterfaces());
		visit(ctx.interfaceBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceModifier(Java8Parser.InterfaceModifierContext ctx) {
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {
			print(ctx.getText() + " ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExtendsInterfaces(Java8Parser.ExtendsInterfacesContext ctx) {
		print("extends ");
		visit(ctx.interfaceTypeList());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceBody(Java8Parser.InterfaceBodyContext ctx) {
			println("{ ");
			safeVisitList(ctx.interfaceMemberDeclaration());
			println();
			println("}");
			println();
			return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceMemberDeclaration(Java8Parser.InterfaceMemberDeclarationContext ctx) {
		safeVisit(ctx.constantDeclaration());
		safeVisit(ctx.interfaceMethodDeclaration());
		safeVisit(ctx.classDeclaration());
		safeVisit(ctx.interfaceDeclaration());
		if(ctx.getText().equals(";")) println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstantDeclaration(Java8Parser.ConstantDeclarationContext ctx) {
		safeVisitList(ctx.constantModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorList());
		println(";");
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstantModifier(Java8Parser.ConstantModifierContext ctx) {
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		}	else {
			print(ctx.getText() + " ");
		}
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceMethodDeclaration(Java8Parser.InterfaceMethodDeclarationContext ctx) {
		safeVisitList(ctx.interfaceMethodModifier());
		visit(ctx.methodHeader());
		visit(ctx.methodBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterfaceMethodModifier(Java8Parser.InterfaceMethodModifierContext ctx) {
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {
			print(ctx.getText() + " ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotationTypeDeclaration(Java8Parser.AnnotationTypeDeclarationContext ctx) {
		safeVisitList(ctx.interfaceModifier());
		print("@interface " + ctx.Identifier() + " ");
		visit(ctx.annotationTypeBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotationTypeBody(Java8Parser.AnnotationTypeBodyContext ctx) {
		print("{");
		safeVisitList(ctx.annotationTypeMemberDeclaration());
		println();
		print("}");
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotationTypeMemberDeclaration(Java8Parser.AnnotationTypeMemberDeclarationContext ctx) {
		safeVisit(ctx.annotationTypeElementDeclaration());
		safeVisit(ctx.constantDeclaration());
		safeVisit(ctx.classDeclaration());
		safeVisit(ctx.interfaceDeclaration());
		if(ctx.getText().equals(";")) println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotationTypeElementDeclaration(Java8Parser.AnnotationTypeElementDeclarationContext ctx) {
		safeVisitList(ctx.annotationTypeElementModifier());
		visit(ctx.unannType());
		print(ctx.Identifier() + "() ");
		safeVisit(ctx.dims());
		safeVisit(ctx.defaultValue());
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotationTypeElementModifier(Java8Parser.AnnotationTypeElementModifierContext ctx) {
		if(ctx.annotation() != null){
			visit(ctx.annotation());
		} else {	
			print(ctx.getText() + null);
		}		
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDefaultValue(Java8Parser.DefaultValueContext ctx) {
		print("default ");
		visit(ctx.elementValue());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotation(Java8Parser.AnnotationContext ctx) {
		safeVisit(ctx.normalAnnotation());
		safeVisit(ctx.markerAnnotation());
		safeVisit(ctx.singleElementAnnotation());	
		println();	
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitNormalAnnotation(Java8Parser.NormalAnnotationContext ctx) {
		print("@ ");
		visit(ctx.typeName());
		print("(");
		safeVisit(ctx.elementValuePairList());
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitElementValuePairList(Java8Parser.ElementValuePairListContext ctx) {
		delimitedVisitList(ctx.elementValuePair(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitElementValuePair(Java8Parser.ElementValuePairContext ctx) {
		print(ctx.getText() + " = ");
		visit(ctx.elementValue());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitElementValue(Java8Parser.ElementValueContext ctx) {
		safeVisit(ctx.conditionalExpression());
		safeVisit(ctx.elementValueArrayInitializer());
		safeVisit(ctx.annotation());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitElementValueArrayInitializer(Java8Parser.ElementValueArrayInitializerContext ctx) {
		println("{");
		safeVisit(ctx.elementValueList());
		if(ctx.getText().equals(ctx.elementValueList().getText() + ",")) print(",");
		println();
		println("}");
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitElementValueList(Java8Parser.ElementValueListContext ctx) {
		delimitedVisitList(ctx.elementValue(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMarkerAnnotation(Java8Parser.MarkerAnnotationContext ctx) {
		print("@");
		visit(ctx.typeName());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSingleElementAnnotation(Java8Parser.SingleElementAnnotationContext ctx) {
		print("@");
		visit(ctx.typeName());
		print("( ");
		visit(ctx.elementValue());
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArrayInitializer(Java8Parser.ArrayInitializerContext ctx) {
		println("{");
		safeVisit(ctx.variableInitializerList());
		if(ctx.getText().equals(ctx.variableInitializerList().getText() + ",")) print(",");
		println();
		println();
		println("}");
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableInitializerList(Java8Parser.VariableInitializerListContext ctx) { 
		delimitedVisitList(ctx.variableInitializer(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBlock(Java8Parser.BlockContext ctx) {
		println("{");
		safeVisit(ctx.blockStatements());
		println();
		println("}");
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBlockStatements(Java8Parser.BlockStatementsContext ctx) {
		safeVisitList(ctx.blockStatement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBlockStatement(Java8Parser.BlockStatementContext ctx) {
		safeVisit(ctx.localVariableDeclarationStatement());
		safeVisit(ctx.classDeclaration());
		safeVisit(ctx.statement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLocalVariableDeclarationStatement(Java8Parser.LocalVariableDeclarationStatementContext ctx) {
		visit(ctx.localVariableDeclaration());
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLocalVariableDeclaration(Java8Parser.LocalVariableDeclarationContext ctx) {
		safeVisitList(ctx.variableModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorList());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStatement(Java8Parser.StatementContext ctx) {
		safeVisit(ctx.statementWithoutTrailingSubstatement());
		safeVisit(ctx.labeledStatement());
		safeVisit(ctx.ifThenStatement());
		safeVisit(ctx.ifThenElseStatement());
		safeVisit(ctx.whileStatement());
		safeVisit(ctx.forStatement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStatementNoShortIf(Java8Parser.StatementNoShortIfContext ctx) {
		safeVisit(ctx.statementWithoutTrailingSubstatement());
		safeVisit(ctx.labeledStatementNoShortIf());
		safeVisit(ctx.ifThenElseStatementNoShortIf());
		safeVisit(ctx.whileStatementNoShortIf());
		safeVisit(ctx.forStatementNoShortIf());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStatementWithoutTrailingSubstatement(Java8Parser.StatementWithoutTrailingSubstatementContext ctx) {
		safeVisit(ctx.block());
		safeVisit(ctx.emptyStatement());
		safeVisit(ctx.expressionStatement());
		safeVisit(ctx.assertStatement());
		safeVisit(ctx.switchStatement());
		safeVisit(ctx.doStatement());
		safeVisit(ctx.breakStatement());
		safeVisit(ctx.continueStatement());
		safeVisit(ctx.returnStatement());
		safeVisit(ctx.synchronizedStatement());
		safeVisit(ctx.throwStatement());
		safeVisit(ctx.tryStatement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEmptyStatement(Java8Parser.EmptyStatementContext ctx) { 
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLabeledStatement(Java8Parser.LabeledStatementContext ctx) {
		print(ctx.Identifier() + ":");
		visit(ctx.statement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLabeledStatementNoShortIf(Java8Parser.LabeledStatementNoShortIfContext ctx) {
		print(ctx.Identifier() + ":");
		visit(ctx.statementNoShortIf());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExpressionStatement(Java8Parser.ExpressionStatementContext ctx) {
		visit(ctx.statementExpression());	
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStatementExpression(Java8Parser.StatementExpressionContext ctx) {
		safeVisit(ctx.assignment());
		safeVisit(ctx.preIncrementExpression());
		safeVisit(ctx.preDecrementExpression());
		safeVisit(ctx.postIncrementExpression());
		safeVisit(ctx.postDecrementExpression());
		safeVisit(ctx.methodInvocation());
		safeVisit(ctx.classInstanceCreationExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIfThenStatement(Java8Parser.IfThenStatementContext ctx) {
		print("if (");
		visit(ctx.expression());
		println(")");
		visit(ctx.statement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIfThenElseStatement(Java8Parser.IfThenElseStatementContext ctx) {
		print("if (");
		visit(ctx.expression());
		println(")");
		visit(ctx.statementNoShortIf());
		print ("else ");
		visit(ctx.statement());
		return null;
}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIfThenElseStatementNoShortIf(Java8Parser.IfThenElseStatementNoShortIfContext ctx) {
		print("if (");
		visit(ctx.expression());
		println(")");
		safeVisitList(ctx.statementNoShortIf());
		print ("else ");
		safeVisitList(ctx.statementNoShortIf());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAssertStatement(Java8Parser.AssertStatementContext ctx) {
		print("assert ");
		delimitedVisitList(ctx.expression(), ": ");
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSwitchStatement(Java8Parser.SwitchStatementContext ctx) {
		print("switch (");
		visit(ctx.expression());
		print(") ");
		visit(ctx.switchBlock());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSwitchBlock(Java8Parser.SwitchBlockContext ctx) {
		println("{");
		safeVisitList(ctx.switchBlockStatementGroup());
		safeVisitList(ctx.switchLabel());
		println();
		println("{");
		println();
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSwitchBlockStatementGroup(Java8Parser.SwitchBlockStatementGroupContext ctx) {
		visit(ctx.switchLabels());
		visit(ctx.blockStatements());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSwitchLabels(Java8Parser.SwitchLabelsContext ctx) {
		safeVisitList(ctx.switchLabel());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSwitchLabel(Java8Parser.SwitchLabelContext ctx) {
		if(ctx.getText().contains("default")) print("default ");
		else {
			print("case ");
			safeVisit(ctx.constantExpression());
			safeVisit(ctx.enumConstantName());
		}
		print(": ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnumConstantName(Java8Parser.EnumConstantNameContext ctx) {
		print(ctx.Identifier().getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitWhileStatement(Java8Parser.WhileStatementContext ctx) {
		print("while ( ");
		visit(ctx.expression());
		print(" )");
		visit(ctx.statement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitWhileStatementNoShortIf(Java8Parser.WhileStatementNoShortIfContext ctx) {
		print("while ( ");
		visit(ctx.expression());
		print(" )");
		visit(ctx.statementNoShortIf());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDoStatement(Java8Parser.DoStatementContext ctx) {
		print("do ");
		visit(ctx.statement());
		print("while ( ");
		visit(ctx.expression());
		print(" );");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitForStatement(Java8Parser.ForStatementContext ctx) {
		safeVisit(ctx.basicForStatement());
		safeVisit(ctx.enhancedForStatement());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitForStatementNoShortIf(Java8Parser.ForStatementNoShortIfContext ctx) {
		safeVisit(ctx.basicForStatementNoShortIf());
		safeVisit(ctx.enhancedForStatementNoShortIf());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBasicForStatement(Java8Parser.BasicForStatementContext ctx) {
		print("for ( ");
		safeVisit(ctx.forInit());
		print("; ");
		safeVisit(ctx.expression());
		print("; ");
		safeVisit(ctx.forUpdate());
		print(") ");
		visit(ctx.statement());
		return null;		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBasicForStatementNoShortIf(Java8Parser.BasicForStatementNoShortIfContext ctx) {
		print("for ( ");
		safeVisit(ctx.forInit());
		print("; ");
		safeVisit(ctx.expression());
		print("; ");
		safeVisit(ctx.forUpdate());
		print(") ");
		visit(ctx.statementNoShortIf());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitForInit(Java8Parser.ForInitContext ctx) {
		safeVisit(ctx.statementExpressionList());
		safeVisit(ctx.localVariableDeclaration());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitForUpdate(Java8Parser.ForUpdateContext ctx) {
		visit(ctx.statementExpressionList());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStatementExpressionList(Java8Parser.StatementExpressionListContext ctx) {
		delimitedVisitList(ctx.statementExpression(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnhancedForStatement(Java8Parser.EnhancedForStatementContext ctx) {
		print("for ( ");
		safeVisitList(ctx.variableModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorId());
		print(": ");
		safeVisit(ctx.expression());
		print(") ");
		visit(ctx.statement());
		return null;
}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEnhancedForStatementNoShortIf(Java8Parser.EnhancedForStatementNoShortIfContext ctx) {
		print("for ( ");
		safeVisitList(ctx.variableModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorId());
		print(": ");
		safeVisit(ctx.expression());
		print(") ");
		visit(ctx.statementNoShortIf());
		return null;
}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBreakStatement(Java8Parser.BreakStatementContext ctx) {
		print("break ");
		safePrint(ctx.Identifier());
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitContinueStatement(Java8Parser.ContinueStatementContext ctx) {
		print("continue ");
		safePrint(ctx.Identifier());
		println(";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitReturnStatement(Java8Parser.ReturnStatementContext ctx) {
		print("return ");
		safeVisit(ctx.expression());
		println(";");
		return null;	
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitThrowStatement(Java8Parser.ThrowStatementContext ctx) {
		print("throw ");
		visit(ctx.expression());
		println(";");
		return null;	
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSynchronizedStatement(Java8Parser.SynchronizedStatementContext ctx) {
		print("synchronized (");
		visit(ctx.expression());
		print(")");
		visit(ctx.block());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTryStatement(Java8Parser.TryStatementContext ctx) {
		if(ctx.tryWithResourcesStatement() != null){
			visit(ctx.tryWithResourcesStatement());
		} else {
			print("try ");
			visit(ctx.block());
			safeVisit(ctx.catches());
			safeVisit(ctx.finally_());			
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCatches(Java8Parser.CatchesContext ctx) {
		safeVisitList(ctx.catchClause());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCatchClause(Java8Parser.CatchClauseContext ctx) {
		print("catch (");
		visit(ctx.catchFormalParameter());
		print(") ");
		visit(ctx.block());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCatchFormalParameter(Java8Parser.CatchFormalParameterContext ctx) {
		safeVisitList(ctx.variableModifier());
		visit(ctx.catchType());
		visit(ctx.variableDeclaratorId());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCatchType(Java8Parser.CatchTypeContext ctx) {
		visit(ctx.unannClassType());
		preDelimitedVisitList(ctx.classType(), "|");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFinally_(Java8Parser.Finally_Context ctx) {
		print("finally ");
		visit(ctx.block());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTryWithResourcesStatement(Java8Parser.TryWithResourcesStatementContext ctx) {
		print("try ");
		visit(ctx.resourceSpecification());
		visit(ctx.block());
		visit(ctx.catches());
		visit(ctx.finally_());		
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitResourceSpecification(Java8Parser.ResourceSpecificationContext ctx) {
		print("(");
		visit(ctx.resourceList());		
		if(ctx.getText().equals("(" + ctx.resourceList().getText() + ";)")){
			print(";");
		}
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitResourceList(Java8Parser.ResourceListContext ctx) {
		delimitedVisitList(ctx.resource(), ";");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitResource(Java8Parser.ResourceContext ctx) {
		safeVisitList(ctx.variableModifier());
		visit(ctx.unannType());
		visit(ctx.variableDeclaratorId());
		print(" = ");
		visit(ctx.expression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimary(Java8Parser.PrimaryContext ctx) {
		safeVisit(ctx.primaryNoNewArray_lfno_primary());
		safeVisit(ctx.arrayCreationExpression());
		safeVisitList(ctx.primaryNoNewArray_lf_primary());
		return null;
	}


	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray(Java8Parser.PrimaryNoNewArrayContext ctx) {
		if(ctx.literal() != null){
			visit(ctx.literal());
		} else if (ctx.expression() != null) {
			print ("(");
			visit(ctx.expression());
			print(")");
		} else if(ctx.classInstanceCreationExpression() != null ||
			ctx.fieldAccess() != null ||
			ctx.arrayAccess() != null ||
			ctx.methodInvocation() != null ||
			ctx.methodReference() != null){
			safeVisit(ctx.classInstanceCreationExpression());
			safeVisit(ctx.fieldAccess());
			safeVisit(ctx.arrayAccess());
			safeVisit(ctx.methodInvocation());
			safeVisit(ctx.methodReference());
		} else {//if (ctx.typeName != null){
			print(ctx.getText());
			//int count = countChars(ctx.getText(), "[");
			//int typeNCount = countChars(ctx.typeName().getText(),"[");
			//for(int i = 0; i < count - typeNCount; i ++){
			//	print("[]");
			//}
			//print(".class");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lf_arrayAccess(Java8Parser.PrimaryNoNewArray_lf_arrayAccessContext ctx) {	
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lfno_arrayAccess(Java8Parser.PrimaryNoNewArray_lfno_arrayAccessContext ctx) {
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lf_primary(Java8Parser.PrimaryNoNewArray_lf_primaryContext ctx) {
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lf_primary_lf_arrayAccess_lf_primary(Java8Parser.PrimaryNoNewArray_lf_primary_lf_arrayAccess_lf_primaryContext ctx) {
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary(Java8Parser.PrimaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primaryContext ctx) {
		safeVisit(ctx.classInstanceCreationExpression_lf_primary());
		safeVisit(ctx.fieldAccess_lf_primary());
		safeVisit(ctx.methodInvocation_lf_primary());
		safeVisit(ctx.methodReference_lf_primary());	
		return null;
   }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lfno_primary(Java8Parser.PrimaryNoNewArray_lfno_primaryContext ctx) {
		if(ctx.literal() != null){
			visit(ctx.literal());
		} else if (ctx.expression() != null) {
			print ("(");
			visit(ctx.expression());
			print(")");
		} else if(ctx.classInstanceCreationExpression_lfno_primary() != null ||
			ctx.fieldAccess_lfno_primary() != null ||
			ctx.arrayAccess_lfno_primary() != null ||
			ctx.methodInvocation_lfno_primary() != null ||
			ctx.methodReference_lfno_primary() != null){
			safeVisit(ctx.classInstanceCreationExpression_lfno_primary());
			safeVisit(ctx.fieldAccess_lfno_primary());
			safeVisit(ctx.arrayAccess_lfno_primary());
			safeVisit(ctx.methodInvocation_lfno_primary());
			safeVisit(ctx.methodReference_lfno_primary());
		} else {
			print(ctx.getText());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lfno_primary_lf_arrayAccess_lfno_primary(Java8Parser.PrimaryNoNewArray_lfno_primary_lf_arrayAccess_lfno_primaryContext ctx) {
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(Java8Parser.PrimaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		if(ctx.literal() != null){
			visit(ctx.literal());
		} else if (ctx.expression() != null) {
			print ("(");
			visit(ctx.expression());
			print(")");
		} else if(ctx.classInstanceCreationExpression_lfno_primary() != null ||
			ctx.fieldAccess_lfno_primary() != null ||
			ctx.methodInvocation_lfno_primary() != null ||
			ctx.methodReference_lfno_primary() != null){
			safeVisit(ctx.classInstanceCreationExpression_lfno_primary());
			safeVisit(ctx.fieldAccess_lfno_primary());
			safeVisit(ctx.methodInvocation_lfno_primary());
			safeVisit(ctx.methodReference_lfno_primary());
		} else {
			print(ctx.getText());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassInstanceCreationExpression(Java8Parser.ClassInstanceCreationExpressionContext ctx) { 
		if(ctx.expressionName() != null){
			visit(ctx.expressionName());
			print(".");
		}
		if(ctx.primary() != null){
			visit(ctx.primary());
			print(".");
		}
		print ("new ");
		safeVisit(ctx.typeArguments());
		safeVisitList(ctx.annotation());
		print(" " + ctx.Identifier() + " ");
		preDelimitedVisitList(ctx.annotationIdentifier(), ".");
		safeVisit(ctx.typeArgumentsOrDiamond());
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		safeVisit(ctx.classBody());
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAnnotationIdentifier(Java8Parser.AnnotationIdentifierContext ctx) {
		safeVisitList(ctx.annotation());
		print(ctx.Identifier().getText());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassInstanceCreationExpression_lf_primary(Java8Parser.ClassInstanceCreationExpression_lf_primaryContext ctx) {
		print (".new ");
		safeVisit(ctx.typeArguments());
		safeVisitList(ctx.annotation());
		print(" " + ctx.Identifier() + " ");
		safeVisit(ctx.typeArgumentsOrDiamond());
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		safeVisit(ctx.classBody());
		return null;

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassInstanceCreationExpression_lfno_primary(Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext ctx) {
		if(ctx.expressionName() != null){
			visit(ctx.expressionName());
			print(".");
		}
		print ("new ");
		safeVisit(ctx.typeArguments());
		safeVisitList(ctx.annotation());
		print(" " + ctx.Identifier() + " ");
		preDelimitedVisitList(ctx.annotationIdentifier(), ".");
		safeVisit(ctx.typeArgumentsOrDiamond());
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		safeVisit(ctx.classBody());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTypeArgumentsOrDiamond(Java8Parser.TypeArgumentsOrDiamondContext ctx) { 
		if(ctx.typeArguments() != null){
			visit(ctx.typeArguments());
		} else {
			print("<>");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFieldAccess(Java8Parser.FieldAccessContext ctx) {
		if(ctx.primary() != null){
			visit(ctx.primary());
			print(".");		
		}	else if(ctx.typeName() != null) {
			visit(ctx.typeName());
			print(".super." + ctx.Identifier().getText() + " ");
		} else {
			print("super." + ctx.Identifier().getText() + " ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFieldAccess_lf_primary(Java8Parser.FieldAccess_lf_primaryContext ctx) {
			print("." + ctx.Identifier().getText() + " ");
			return null;	
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFieldAccess_lfno_primary(Java8Parser.FieldAccess_lfno_primaryContext ctx) {		
		if(ctx.typeName() != null) {
			visit(ctx.typeName());
			print(".super." + ctx.Identifier().getText() + " ");
		} else {
			print("super." + ctx.Identifier().getText() + " ");
		}
		return null;
}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArrayAccess(Java8Parser.ArrayAccessContext ctx) { 
		safeVisit(ctx.expressionName());
		safeVisit(ctx.primaryNoNewArray_lfno_arrayAccess());
		print("[");
		visit(ctx.expression());
		print("]");
		visit(ctx.arrayAccessHelper());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArrayAccess_lf_primary(Java8Parser.ArrayAccess_lf_primaryContext ctx) {
		safeVisit(ctx.primaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary());
		print("[");
		visit(ctx.expression());
		print("]");
		visit(ctx.arrayAccessHelper());
		return null;

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArrayAccess_lfno_primary(Java8Parser.ArrayAccess_lfno_primaryContext ctx) {
		safeVisit(ctx.expressionName());
		safeVisit(ctx.primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary());
		print("[");
		visit(ctx.expression());
		print("]");
		visit(ctx.arrayAccessHelper());
		return null;
	}

	@Override public T visitArrayAccessHelper(Java8Parser.ArrayAccessHelperContext ctx) {
		if(ctx.expression() != null){		
			for(int i = 0; i < ctx.expression().size(); i ++){
				if(ctx.primaryNoNewArray_lf_arrayAccess() != null)
					visit(ctx.primaryNoNewArray_lf_arrayAccess().get(i));
				if(ctx.primaryNoNewArray_lf_primary_lf_arrayAccess_lf_primary() != null)
					visit(ctx.primaryNoNewArray_lf_primary_lf_arrayAccess_lf_primary().get(i));
				if(ctx.primaryNoNewArray_lfno_primary_lf_arrayAccess_lfno_primary() != null)
					visit(ctx.primaryNoNewArray_lfno_primary_lf_arrayAccess_lfno_primary().get(i));
				print("[");
				visit(ctx.expression().get(i));
				print("]");				
			}
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
		if(ctx.methodName() != null){
			visit(ctx.methodName());
		} else {		
			safeVisit(ctx.expressionName());
			safeVisit(ctx.primary());
			if(ctx.getText().contains("super")){
				if(ctx.typeName() != null){
					visit(ctx.typeName());
					print(".");
				}
				print("super.");
			} else {
				safeVisit(ctx.typeName());
			}
			safeVisit(ctx.typeArguments());
			print(ctx.Identifier().getText() + " ");
		}
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodInvocation_lf_primary(Java8Parser.MethodInvocation_lf_primaryContext ctx) {
		print(".");
		safeVisit(ctx.typeArguments());
		print(ctx.Identifier().getText() + " ");
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodInvocation_lfno_primary(Java8Parser.MethodInvocation_lfno_primaryContext ctx) {
		if(ctx.methodName() != null){
			visit(ctx.methodName());
		} else {		
			safeVisit(ctx.expressionName());
			if(ctx.getText().contains("super")){
				if(ctx.typeName() != null){
					visit(ctx.typeName());
					print(".");
				}
				print("super.");
			} else {
				safeVisit(ctx.typeName());
			}
			safeVisit(ctx.typeArguments());
			print(ctx.Identifier().getText() + " ");
		}
		print("(");
		safeVisit(ctx.argumentList());
		print(")");
		return null;	
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArgumentList(Java8Parser.ArgumentListContext ctx) {
		delimitedVisitList(ctx.expression(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodReference(Java8Parser.MethodReferenceContext ctx) {
		safeVisit(ctx.expressionName());
		safeVisit(ctx.referenceType());
		safeVisit(ctx.primary());
		if(ctx.getText().contains("super")){
			if(ctx.typeName() != null){
				visit(ctx.typeName());
				print(".");
			}
			print("super ");
		}
		safeVisit(ctx.classType());
		safeVisit(ctx.arrayType());			
		print(":: ");
		safeVisit(ctx.typeArguments());
		if(ctx.Identifier() != null){
			print(ctx.Identifier().getText() + " ");
		} else {
			print("new ");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodReference_lf_primary(Java8Parser.MethodReference_lf_primaryContext ctx) {		
		print(":: ");
		safeVisit(ctx.typeArguments());
		print(ctx.Identifier().getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethodReference_lfno_primary(Java8Parser.MethodReference_lfno_primaryContext ctx) {
		safeVisit(ctx.expressionName());
		safeVisit(ctx.referenceType());
		if(ctx.getText().contains("super")){
			if(ctx.typeName() != null){
				visit(ctx.typeName());
				print(".");
			}
			print("super ");
		}
		safeVisit(ctx.classType());
		safeVisit(ctx.arrayType());			
		print(":: ");
		safeVisit(ctx.typeArguments());
		if(ctx.Identifier() != null){
			print(ctx.Identifier().getText() + " ");
		} else {
			print("new ");
		}
		return null;

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArrayCreationExpression(Java8Parser.ArrayCreationExpressionContext ctx) {
		print("new ");
		safeVisit(ctx.primitiveType());
		safeVisit(ctx.classOrInterfaceType());
		safeVisit(ctx.primitiveType());
		safeVisit(ctx.classOrInterfaceType());
		safeVisit(ctx.dimExprs());
		safeVisit(ctx.dims());
		safeVisit(ctx.arrayInitializer());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDimExprs(Java8Parser.DimExprsContext ctx) { 
		safeVisitList(ctx.dimExpr());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDimExpr(Java8Parser.DimExprContext ctx) {
		safeVisitList(ctx.annotation());
		print("[");
		visit(ctx.expression());
		print("]");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstantExpression(Java8Parser.ConstantExpressionContext ctx) {
		visit(ctx.expression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExpression(Java8Parser.ExpressionContext ctx) {
		safeVisit(ctx.lambdaExpression());
		safeVisit(ctx.assignmentExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLambdaExpression(Java8Parser.LambdaExpressionContext ctx) {
		visit(ctx.lambdaParameters());
		print("->");
		visit(ctx.lambdaBody());
		print(" ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLambdaParameters(Java8Parser.LambdaParametersContext ctx) {
		if(ctx.Identifier() != null){
			print(ctx.Identifier().getText() + " ");
		} else {
			print("(");
			safeVisit(ctx.formalParameterList());
			safeVisit(ctx.inferredFormalParameterList());
			print(")");
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInferredFormalParameterList(Java8Parser.InferredFormalParameterListContext ctx) {
		delimitedVisitListTerm(ctx.Identifier(), ",");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLambdaBody(Java8Parser.LambdaBodyContext ctx) {
		safeVisit(ctx.expression());
		safeVisit(ctx.block());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAssignmentExpression(Java8Parser.AssignmentExpressionContext ctx) {
		safeVisit(ctx.conditionalExpression());
		safeVisit(ctx.assignment());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAssignment(Java8Parser.AssignmentContext ctx) {
		visit(ctx.leftHandSide());
		visit(ctx.assignmentOperator());
		visit(ctx.expression());		
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLeftHandSide(Java8Parser.LeftHandSideContext ctx) {
		safeVisit(ctx.expressionName());
		safeVisit(ctx.fieldAccess());
		safeVisit(ctx.arrayAccess());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAssignmentOperator(Java8Parser.AssignmentOperatorContext ctx) {
		print(" " + ctx.getText() + " ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConditionalExpression(Java8Parser.ConditionalExpressionContext ctx) {
		visit(ctx.conditionalOrExpression());
		if(ctx.expression() != null){
			print("? ");
			visit(ctx.expression());
			print(":");
			visit(ctx.conditionalExpression());
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConditionalOrExpression(Java8Parser.ConditionalOrExpressionContext ctx) {
		if(ctx.conditionalOrExpression() != null){
			visit(ctx.conditionalOrExpression());
			print(" || ");
		}
		visit(ctx.conditionalAndExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConditionalAndExpression(Java8Parser.ConditionalAndExpressionContext ctx) {
		if(ctx.conditionalAndExpression() != null){
			visit(ctx.conditionalAndExpression());
			print(" && ");
		}
		visit(ctx.inclusiveOrExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInclusiveOrExpression(Java8Parser.InclusiveOrExpressionContext ctx) {
		if(ctx.inclusiveOrExpression() != null){
			visit(ctx.inclusiveOrExpression());
			print(" | ");
		}
		visit(ctx.exclusiveOrExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExclusiveOrExpression(Java8Parser.ExclusiveOrExpressionContext ctx) {
		if(ctx.exclusiveOrExpression() != null){
			visit(ctx.exclusiveOrExpression());
			print(" ^ ");
		}
		visit(ctx.andExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAndExpression(Java8Parser.AndExpressionContext ctx) {
		if(ctx.andExpression() != null){
			visit(ctx.andExpression());
			print(" & ");
		}
		visit(ctx.equalityExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitEqualityExpression(Java8Parser.EqualityExpressionContext ctx) {
		if(ctx.equalityExpression() != null 
			&& ctx.getText().equals(ctx.equalityExpression().getText() 
			+ "==" + ctx.relationalExpression().getText())){
			visit(ctx.equalityExpression());
			print(" == ");
		}
		if(ctx.equalityExpression() != null 
			&& ctx.getText().equals(ctx.equalityExpression() + "!=" + ctx.relationalExpression())){
			visit(ctx.equalityExpression());
			print(" != ");
		}
		visit(ctx.relationalExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitRelationalExpression(Java8Parser.RelationalExpressionContext ctx) {
		if(ctx.relationalExpression() != null 
			&& ctx.getText().equals(ctx.relationalExpression().getText() 
			+ "<" + ctx.shiftExpression().getText())){
			visit(ctx.relationalExpression());
			print(" < ");
		}
		if(ctx.relationalExpression() != null 
			&& ctx.getText().equals(ctx.relationalExpression().getText() 
			+ ">" + ctx.shiftExpression().getText())){
			visit(ctx.relationalExpression());
			print(" > ");
		}
		if(ctx.relationalExpression() != null 
			&& ctx.getText().equals(ctx.relationalExpression().getText() 
			+ "<=" + ctx.shiftExpression().getText())){
			visit(ctx.relationalExpression());
			print(" <= ");
		}
		if(ctx.relationalExpression() != null 
			&& ctx.getText().equals(ctx.relationalExpression() 
			+ ">=" + ctx.shiftExpression().getText())){
			visit(ctx.relationalExpression());
			print(" >= ");
		}
		if(ctx.referenceType() != null){
			visit(ctx.relationalExpression());
			print(" instanceof ");
			visit(ctx.referenceType());
		}
		safeVisit(ctx.shiftExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitShiftExpression(Java8Parser.ShiftExpressionContext ctx) {
		if(ctx.shiftExpression() != null 
			&& ctx.getText().equals(ctx.shiftExpression().getText() 
			+ "<<" + ctx.additiveExpression().getText())){
			visit(ctx.shiftExpression());
			print(" << ");
		}
		if(ctx.shiftExpression() != null 
			&& ctx.getText().equals(ctx.shiftExpression().getText() 
			+ ">>" + ctx.additiveExpression().getText())){
			visit(ctx.shiftExpression());
			print(" >> ");
		}
		if(ctx.shiftExpression() != null 
			&& ctx.getText().equals(ctx.shiftExpression() 
			+ ">>>" + ctx.additiveExpression().getText())){
			visit(ctx.shiftExpression());
			print(" >>> ");
		}
		safeVisit(ctx.additiveExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAdditiveExpression(Java8Parser.AdditiveExpressionContext ctx) {
		if(ctx.additiveExpression() != null 
			&& ctx.getText().equals(ctx.additiveExpression().getText() 
			+ "+" + ctx.multiplicativeExpression().getText())){
			visit(ctx.additiveExpression());
			print(" + ");
		}
		if(ctx.additiveExpression() != null 
			&& ctx.getText().equals(ctx.additiveExpression().getText() 
			+ "-" + ctx.multiplicativeExpression().getText())){
			visit(ctx.additiveExpression());
			print(" - ");
		}
		visit(ctx.multiplicativeExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMultiplicativeExpression(Java8Parser.MultiplicativeExpressionContext ctx) { 
		if(ctx.multiplicativeExpression() != null 
			&& ctx.getText().equals(ctx.multiplicativeExpression().getText() 
			+ "*" + ctx.unaryExpression().getText())){
			visit(ctx.multiplicativeExpression());
			print(" * ");
		}
		if(ctx.multiplicativeExpression() != null 
			&& ctx.getText().equals(ctx.multiplicativeExpression().getText() 
			+ "/" + ctx.unaryExpression().getText())){
			visit(ctx.multiplicativeExpression());
			print(" / ");
		}
		if(ctx.multiplicativeExpression() != null 
			&& ctx.getText().equals(ctx.multiplicativeExpression().getText() 
			+ "%" + ctx.unaryExpression().getText())){
			visit(ctx.multiplicativeExpression());
			print(" % ");
		}
		visit(ctx.unaryExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnaryExpression(Java8Parser.UnaryExpressionContext ctx) {
		safeVisit(ctx.preIncrementExpression());
		safeVisit(ctx.preDecrementExpression());
		if(ctx.unaryExpression() != null){
			if(ctx.getText().equals("+" + ctx.unaryExpression().getText())){
				print("+");
			}			
			if(ctx.getText().equals("-" + ctx.unaryExpression().getText())){
				print("-");
			}	
			visit(ctx.unaryExpression());
		}
		safeVisit(ctx.unaryExpressionNotPlusMinus());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPreIncrementExpression(Java8Parser.PreIncrementExpressionContext ctx) {
		print("++");
		visit(ctx.unaryExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPreDecrementExpression(Java8Parser.PreDecrementExpressionContext ctx) {
		print("--");
		visit(ctx.unaryExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitUnaryExpressionNotPlusMinus(Java8Parser.UnaryExpressionNotPlusMinusContext ctx) {
		safeVisit(ctx.postfixExpression());
		if(ctx.unaryExpression() != null){
			if(ctx.getText().equals("~" + ctx.unaryExpression().getText())) {
				print("~");
			}
			if(ctx.getText().equals("!" + ctx.unaryExpression().getText())) {
				print("!");
			}
			visit(ctx.unaryExpression());
		}
		safeVisit(ctx.castExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPostfixExpression(Java8Parser.PostfixExpressionContext ctx) {
		safeVisit(ctx.primary());
		safeVisit(ctx.expressionName());
		safeVisitList(ctx.postIncrementExpression_lf_postfixExpression());
		safeVisitList(ctx.postDecrementExpression_lf_postfixExpression());
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPostIncrementExpression(Java8Parser.PostIncrementExpressionContext ctx) {
		visit(ctx.postfixExpression());
		print(" ++ ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPostIncrementExpression_lf_postfixExpression(Java8Parser.PostIncrementExpression_lf_postfixExpressionContext ctx) {
		print(" ++ ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPostDecrementExpression(Java8Parser.PostDecrementExpressionContext ctx) {		visit(ctx.postfixExpression());
		print(" -- ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPostDecrementExpression_lf_postfixExpression(Java8Parser.PostDecrementExpression_lf_postfixExpressionContext ctx) { 
		print(" -- ");
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCastExpression(Java8Parser.CastExpressionContext ctx) {
		print("(");
		safeVisit(ctx.primitiveType());
		safeVisit(ctx.referenceType());
		safeVisitList(ctx.additionalBound());
		print(")");
		safeVisit(ctx.unaryExpression());
		safeVisit(ctx.lambdaExpression());
		return null;
 	}
}
