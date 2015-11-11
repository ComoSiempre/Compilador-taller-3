package ast;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;

public class Statement extends AstNodo implements visitaNodo{

	String tipoOperacion=""; //IF, IF-ELSE, WHILE, DO-WHILE, RETURN.
	AstNodo expression;
	AstNodo statement1;
	AstNodo statement2;//normalment es usado en la gramatica selection_stmt.
	String tipoFuncion;//variable usada para guardar el tipo de funcion para el return. puede ser VOID o INT. 
	
	//constructor para el IF-ELSE
	public Statement(String tipoOp, AstNodo ex, AstNodo sta1, AstNodo sta2){
		this.tipoOperacion=tipoOp;
		this.expression=ex;
		this.statement1=sta1;
		this.statement2=sta2;
	}
	
	//constructor para el DO-WHILE, WHILE,IF
	public Statement(String tipoOp, AstNodo ex, AstNodo ast){
		this.tipoOperacion=tipoOp;
		this.expression=ex;
		this.statement1=ast;
		this.statement2=null;
	}
	
	//constructor para el RETURN (gramatica 1).
	public Statement(String tipoOp){
		this.tipoOperacion=tipoOp;
		this.expression=null;
		this.statement1=null;
		this.statement2=null;
	}
		
	//constructor para el RETURN (gramatica 2).
	public Statement(String tipoOp, AstNodo ex){
		this.tipoOperacion=tipoOp;
		this.expression=ex;
		this.statement1=null;
		this.statement2=null;
	}	
	
	public String toString(int contNodos){
		return "\"nodo"+contNodos+"\"[label=\""+this.tipoOperacion+"\"]; \n";
	}
	
	@Override
	public void aceptar(GrapherVisitor v) {
		// TODO Auto-generated method stub
		v.visitar(this);
	}
	@Override
	public void aceptar(SemanticPass2 s) {
		// TODO Auto-generated method stub
		s.visitar(this);
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public AstNodo getExpression() {
		return expression;
	}

	public AstNodo getStatement1() {
		return statement1;
	}

	public AstNodo getStatement2() {
		return statement2;
	}
	
	
	public String getTipoFuncion() {
		return tipoFuncion;
	}

	public void setTipoFuncion(String tipoFuncion) {
		this.tipoFuncion = tipoFuncion;
	}

	@Override
	public void aceptar(SemanticPass1 s) {
		// TODO Auto-generated method stub
		s.visitar(this);
	}
	@Override
	public void aceptar(GrapherVisitorExtended gve) {
		// TODO Auto-generated method stub
		gve.visitar(this);
	}
}
