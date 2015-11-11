package ast;

import java.util.Stack;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;

public class Expression extends AstNodo implements visitaNodo {

	AstNodo op1;//variable que guarda el primer operando.
	String operador;//variable que guarda el tipo de operacion.
	Object op2; //puede ser String(numero) o nodo expresion. //ver gramatica 51.
	
	String tipo;//es usado para guardar el tipo de dato entre las 2 operadores.
	public Expression(AstNodo v, String op, AstNodo ex){
		this.op1=v;
		this.operador=op;
		this.op2=ex;
	}
	
	public Expression(AstNodo v, String op, String numero){
		this.op1=v;
		this.operador=op;
		this.op2=numero;
	}
//	/**
//	 * 
//	 * funcion usada para imprimir en codigo graph el nodo.
//	 * @param nombrePadre
//	 * @return
//	 */
//	public String toString(Stack<String> nombrePadre){
//		String linea ="";
//		//String padre=nombrePadre.peek();
//		//linea=padre+"->\""+this.operador+"\"; \n";
//		return linea;
//	}
	public String toString(int contNodos){
		
		String ident="\"nodo"+contNodos+"\"[label=\""+this.operador+"\"]; \n";
		return ident;
		
	}
	public String toStringNumber(Stack<String> nombrePadre){
		String linea ="";
		String padre=nombrePadre.peek();
		linea=padre+"->\""+this.operador+"\"; \n";
		return linea;
	}
	public String toStringSemantico(int contNodos){
		return "\"nodo"+contNodos+"\"[label=\""+this.operador+", result:"+this.getTipo()+"\"]; \n";
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

	public AstNodo getOp1() {
		return op1;
	}

	public String getOperador() {
		return operador;
	}

	public Object getOp2() {
		return op2;
	}

	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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
