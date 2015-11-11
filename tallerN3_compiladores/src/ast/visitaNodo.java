package ast;

import syntaxVisitor.GrapherVisitor;
import visitor.*;
import semanticVisitor.*;
public interface visitaNodo {
	public void aceptar(GrapherVisitor v);//cambiar visitor por clase Grapherviz
	public void aceptar(SemanticPass2 s);
	public void aceptar(SemanticPass1 s);
	public void aceptar(GrapherVisitorExtended gve);
}
