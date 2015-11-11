package ast;

import java.util.ArrayList;





import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;

public class Compound extends AstNodo implements visitaNodo {

	int numeroCompound;//no se usa.
	ArrayList<AstNodo> localDeclarationsAndStatements=new ArrayList<>();//lista que guarda las declaraciones locales y Statements.
	
	public Compound(ArrayList<AstNodo> astvardec,ArrayList<AstNodo> statements){
		for(AstNodo c : astvardec){
			this.localDeclarationsAndStatements.add(c);
		}
		for(AstNodo c : statements){
			this.localDeclarationsAndStatements.add(c);
		}
	}
	
	public Compound(){
		
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
		
		return "\"nodo"+contNodos+"\"[label=\"Compound\"]; \n";
	}
	public ArrayList<AstNodo> getLocalDeclarationsAndStatements() {
		return localDeclarationsAndStatements;
	}
	public int getNumeroCompound(){
		return numeroCompound;
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
