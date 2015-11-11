package ast;

import java.util.Stack;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;

public class Var extends AstNodo implements visitaNodo {

	String ID; //variable que guarda el nombre de la variable.
	AstNodo expresion;
	int number;//variable a utilizar en caso de que el nodo sea un Factor.
	boolean esVar;// true: es var, false: es factor de numero.
	
	
	
	public Var(String id, AstNodo ex){
		this.ID=id;
		this.expresion=ex;
		this.esVar=true;
		
	}
	public Var(String id, boolean esVar){
		if(esVar){
			this.ID=id;
			this.expresion=null;
			this.esVar=true;
		}else{
			//corresponde a un factor con numero.
			this.number=Integer.parseInt(id);
			this.expresion=null;
			this.esVar=false;
			
		}
	}
	
	public String toString(int contNodos){
		String linea = "";
		if(this.expresion==null){
			return "\"nodo"+contNodos+"\"[label=\"Var["+this.ID+"]\"]; \n";//creo el nodo dandole identificador.
			
		}else{
			return "\"nodo"+contNodos+"\"[label=\"Var["+this.ID+"[]]\"]; \n";
			
		}
	}
	
	public String toStringNumFactor(int contNodos){
		return "\"nodo"+contNodos+"\"[label=\"Num["+this.number+"]\"]; \n";
	}
	
	public String toStringSemantico(int contNodos){
		if(this.expresion==null){
			return "\"nodo"+contNodos+"\"[label=\"Var["+this.ID+", INT]\"]; \n";
		}else{
			return "\"nodo"+contNodos+"\"[label=\"Var["+this.ID+"[] , INT]\"]; \n";
		}
	}
	
	public String toStringNumSemantico(int contNodos){
		return "\"nodo"+contNodos+"\"[label=\"Num["+this.number+", INT]\"]; \n";
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
	
	public String getID() {
		return ID;
	}
	public AstNodo getExpresion() {
		return expresion;
	}
	public boolean isEsVar() {
		return esVar;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
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
