package ast;

import java.util.ArrayList;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;
import parser.*;
public class Call extends AstNodo implements visitaNodo {
	
	String ID;//variable que guarda el nombre de la funcnion el cual esta siendo llamada.
	ArrayList<AstNodo> listaArgs;//lista que guarda los argumentos.
	
	public Call(){
		this.ID="";
		this.listaArgs=new ArrayList<>();
	}

	public String toString(int contNodos){
		return "\"nodo"+contNodos+"\"[label=\"Call["+this.ID+"]\"]; \n";
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
	@Override
	public void aceptar(GrapherVisitorExtended gve) {
		// TODO Auto-generated method stub
		gve.visitar(this);
	}
	public String getID() {
		return ID;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	public ArrayList<AstNodo> getArgs() {
		return listaArgs;
	}

	@Override
	public void aceptar(SemanticPass1 s) {
		// TODO Auto-generated method stub
		s.visitar(this);
	}
	
}
