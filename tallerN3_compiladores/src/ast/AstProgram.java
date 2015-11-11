package ast;

import java.util.*;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;

public class AstProgram extends AstNodo implements visitaNodo {
	AstVarDec astVarDec;//no se usa.
	AstFunction astFunction;//no se usa
	
	ArrayList<AstNodo> listaDeclaraciones=new ArrayList<>();
	
	
	public ArrayList<AstNodo> getDeclaraciones() {
		return listaDeclaraciones;
	}

	public AstProgram(){
		this.astVarDec=new AstVarDec();
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

	public String toString(int contNodos){
		String linea ="\"nodo"+contNodos+"\"[label=\"Programa\"]; \n";
		return linea;
	}
	public AstVarDec getAstVarDec() {
		return astVarDec;
	}

	public void setAstVarDec(AstVarDec astVarDec) {
		this.astVarDec = astVarDec;
	}

	public AstFunction getAstFunction() {
		return astFunction;
	}

	public void setAstFunction(AstFunction astFunction) {
		this.astFunction = astFunction;
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
