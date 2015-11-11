package ast;

import java.io.File;
import java.util.Stack;

import semanticVisitor.GrapherVisitorExtended;
import semanticVisitor.SemanticPass1;
import semanticVisitor.SemanticPass2;
import syntaxVisitor.GrapherVisitor;
import visitor.*;

public class AstVarDec extends AstNodo implements visitaNodo {

	String ID;//variable que guarda el nombre de la declaracion.
	String tipoID;//variable que guarda el tipo de declaracion.
	int numeroVector=0; //variable usada si es un vector.

	int numeroTipoAsignado=0;//variable usada para diferenciar entre variable de mismo tipo (en el graphViz).
	public AstVarDec(){
		
	}
	
	public AstVarDec(String tipo, String id, int numTipo){
		this.ID=id;
		this.tipoID=tipo;
		this.numeroTipoAsignado=numTipo;
		
		
	}
	public AstVarDec(String tipo, String id, int numTipo,int tamanioVector){
		this.ID=id;
		this.tipoID=tipo;
		this.numeroTipoAsignado=numTipo;
		this.numeroVector=tamanioVector;
	}
	
	public String toString(Stack<String> nombrePadre, int contNodos){
		String padre=nombrePadre.peek();//obtengo la id del padre.
		String lineas="";
		
		//variable global.
		if(this.numeroVector==0){
			//variable normal
			lineas += "\"nodo"+contNodos+"\"[label=\"Var["+this.ID+","+this.tipoID+"]\"]; \n";//creo el nodo dandole identificador.
			lineas +=padre+"->\"nodo"+contNodos+"\"; \n";//enlazo el nuevo nodo con su padre.
			
		}else{
			//variable vector.
			
			lineas += "\"nodo"+contNodos+"\"[label=\""+this.ID+":"+this.tipoID+"["+this.numeroVector+"]\"]; \n";//creo el nodo dandole identificador.
			lineas +=padre+"->\"nodo"+contNodos+"\"; \n";//enlazo el nuevo nodo con su padre.
		}
				
		return  lineas;
		
	}
	public String toStringSemantico(Stack<String> nombrePadre, int contNodos){
		String padre=nombrePadre.peek();//obtengo la id del padre.
		String lineas="";
		//variable global.
		if(this.numeroVector==0){
			//variable normal
			lineas += "\"nodo"+contNodos+"\"[label=\"VarDec["+this.ID+","+this.tipoID+"]\"]; \n";//creo el nodo dandole identificador.
			lineas +=padre+"->\"nodo"+contNodos+"\"; \n";//enlazo el nuevo nodo con su padre.
			
		}else{
			//variable vector.
			
			lineas += "\"nodo"+contNodos+"\"[label=\""+this.ID+":"+this.tipoID+"["+this.numeroVector+"]\"]; \n";//creo el nodo dandole identificador.
			lineas +=padre+"->\"nodo"+contNodos+"\"; \n";//enlazo el nuevo nodo con su padre.
		}
				
		return  lineas;
		
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

	public String getTipoID() {
		return tipoID;
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
