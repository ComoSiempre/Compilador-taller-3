package ast;

import java.util.ArrayList;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;
import visitor.*;

public class AstFunction extends AstNodo implements visitaNodo {

	ArrayList<AstNodo> listaParametros= new ArrayList<>();//corresponde a la gramatica params.
	AstNodo contenido=null; //corresponde al conpound (interior de la funcion).
	
	String ID;//variable que guarda el nombre de la funcion
	String tipoIDFuncion;//variable que guarda el tipo de retorno de la funcion.
	boolean parametrosVoid=false;//variable usada si no existen paramentros en la funcion, solo void.
	
	public AstFunction(){
		
	}
	public AstFunction(boolean voi){
		this.ID="";
		this.tipoIDFuncion="";
		this.parametrosVoid=voi;
	}
	public AstFunction(String id, String tipo){
		this.ID=id;
		this.tipoIDFuncion=tipo;
		
	}
	
	public AstFunction(String id, String tipo, boolean b){
		this.ID=id;
		this.tipoIDFuncion=tipo;
		
		
	}

	public String toString(int contNodos){
		String linea = "\"nodo"+contNodos+"\"[label=\"Funcion["+this.ID+","+this.tipoIDFuncion+"]\"]; \n";
		//String linea="\"Funcion["+this.ID+","+this.tipoIDFuncion+"]\"; \n";
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
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getTipoIDFuncion() {
		return tipoIDFuncion;
	}
	public void setTipoIDFuncion(String tipoIDFuncion) {
		this.tipoIDFuncion = tipoIDFuncion;
	}
	public ArrayList<AstNodo> getParametros() {
		return listaParametros;
	}
	public void setParametros(ArrayList<AstNodo> listaParametros) {
		this.listaParametros = listaParametros;
	}
	public AstNodo getContenido() {
		return contenido;
	}
	public void setContenido(AstNodo contenido) {
		this.contenido = contenido;
	}
	
	
}
