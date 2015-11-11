package ast;

import java.util.Stack;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;

public class Param extends AstNodo implements visitaNodo{
	String ID;//variable que guarda el nombre de la variable parametro.
	String tipoID;//variable que guarda el tipo de parametro
	boolean brackets;//variable usada para la verificacion de un parametro vector.
	boolean esVoid=false;//variable que detecta si el parametro es void.
	public Param(String id, String tipo, boolean tieneBrackets){
		this.ID=id;
		this.tipoID=tipo;
		this.brackets=tieneBrackets;
		this.esVoid=false;
	}
	//constructor usado para los parametros void.
	public Param(String tipo,boolean esVoid){
		this.ID=null;
		this.tipoID=tipo;
		this.brackets=false;
		this.esVoid=esVoid;
	}
	public String toString(Stack<String> nombrePadre, int contNodos){
		String linea="";
		String padre=nombrePadre.peek();
		if(this.esVoid){
			//parametro void.
			linea += "\"nodo"+contNodos+"\"[label=\"Param["+this.tipoID+"]\"]; \n";//creo nodo con identificador y nombre de nodo.
			linea += padre+"->\"nodo"+contNodos+"\"; \n"; //hago el enlace con su padre.
			
		}
		if(this.brackets){
			//parametro vector.
			linea += "\"nodo"+contNodos+"\"[label=\"Param["+this.ID+","+this.tipoID+"[]]\"]; \n";//creo nodo con identificador y nombre de nodo.
			linea += padre+"->\"nodo"+contNodos+"\"; \n"; //hago el enlace con su padre.
			
		}else if(!this.esVoid){
			linea += "\"nodo"+contNodos+"\"[label=\"Param["+this.ID+","+this.tipoID+"]\"]; \n";//creo nodo con identificador y nombre de nodo.
			linea += padre+"->\"nodo"+contNodos+"\"; \n";
			
		}
		return linea;
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
	public void setID(String iD) {
		ID = iD;
	}
	public String getTipoID() {
		return tipoID;
	}
	public void setTipoID(String tipoID) {
		this.tipoID = tipoID;
	}
	public boolean isBrackets() {
		return brackets;
	}
	public void setBrackets(boolean brackets) {
		this.brackets = brackets;
	}
	public boolean isEsVoid() {
		return esVoid;
	}
	public void setEsVoid(boolean esVoid) {
		this.esVoid = esVoid;
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
