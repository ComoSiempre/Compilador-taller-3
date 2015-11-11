package syntaxVisitor;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

import visitor.visitor;
import ast.*;


public class GrapherVisitor implements visitor {

	public int cantNodosVisitados;//cantidad de nodos que se han visitado.
	String codigoGraph="";
	Stack<String> auxPadres=new Stack<>();//variable usada para guardar el nombre del nodo de los padres.
	public GrapherVisitor(){
		cantNodosVisitados = 0;
	}
	
	
	public void generarImagenGraph() throws Exception{
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter ("codSin.txt"));
			//FileWriter writer = new FileWriter(new File("cod.txt"));
			System.out.println(this.codigoGraph);
			writer.write("digraph G { \n");
			writer.write(this.codigoGraph);//se escribe el codigo generado al txt.
			writer.flush();
			writer.write("}");
			writer.close();
			String dotPath = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";//path del dot.exe que genera la imagen
		    String fileInputPath = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/codSin.txt";//txt del codigo .dot del arbol 
		    String fileOutputPath = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/imgCodSintactico.png";//salida de imagen del codigo
		    String tParam = "-Tpng";
		    String tOParam = "-o";
		    String[] cmd = new String[5];//variable para guardar el codigo en consola
		    cmd[0] = dotPath;
		    cmd[1] = tParam;
		    cmd[2] = fileInputPath;
		    cmd[3] = tOParam;
		    cmd[4] = fileOutputPath;
		                  
		    Runtime rt = Runtime.getRuntime();
		      
		    rt.exec( cmd );
		    System.out.println("imagen creada.");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	 
	@Override
	public void visitar(AstProgram astProgram){
		//System.out.println(astProgram.toString());
		String ident=astProgram.toString(this.cantNodosVisitados);//obtengo el identificador del primer nodo(raiz).
		this.codigoGraph +=ident; //agrego el identificador al codigo.
		String[] delimitador=ident.split("\\[");//separo el nombre del identificador del nombre asignado dentro del nodo, posicion 0 .
		//this.auxPadres=astProgram.toString().substring(0, astProgram.toString().length()-3);
		this.auxPadres.push(delimitador[0]);//obtengo la id del nodo 'sin el ;'.
		this.cantNodosVisitados++; //cuento como visitado.
		ArrayList<AstNodo> lista=astProgram.getDeclaraciones();
		for(int i=0; i<lista.size();i++){
			lista.get(i).aceptar(this);
		}
		this.auxPadres.pop();//elimino el padre actual.
		//System.out.println(this.codigoGraph);// para ver el string del codigo.
		try {
			generarImagenGraph();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//astProgram.getAstVarDec().aceptar(this);
		//astProgram.getAstFunction().aceptar(this);
	}
	
	
	@Override
	public void visitar(AstFunction astFunction){
		String ident = astFunction.toString(this.cantNodosVisitados);//ontengo el ident del nodo funcion.
		this.codigoGraph += ident; //agrego el dient, del nodo al codigo.
		String[] delimitador=ident.split("\\[");
		String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";//creo el enlace del padre con el nuevo nodo funcion.
		this.codigoGraph +=enlace;//guardo el enlace en el codigo.
		//this.auxPadres=astFunction.toString().substring(0, astFunction.toString().length()-3);
		this.auxPadres.push(delimitador[0]);//guardo el nuevo nodo en la pila 'sin el 0'.
		this.cantNodosVisitados++; //ya que use el numero para la creacion de el nodo funcion.
		ArrayList<AstNodo> lista=astFunction.getParametros();//obtengo la lista de parametros de la funcion
		for(int i=0; i<lista.size();i++){
			lista.get(i).aceptar(this);
		}
		
		AstNodo contenido = astFunction.getContenido();
		if(contenido!=null)	contenido.aceptar(this);
		this.auxPadres.pop();//elimino el padre actual.
		
	}
	
	@Override
	public void visitar(AstVarDec astvarDec){
		//System.out.println(astvarDec.toString(this.cantNodosVisitados));
		this.codigoGraph += astvarDec.toString(this.auxPadres, this.cantNodosVisitados);
		this.cantNodosVisitados++;
		
	}

	@Override
	public void visitar(Param param) {
		// TODO Auto-generated method stub
		this.codigoGraph +=param.toString(this.auxPadres, this.cantNodosVisitados);
		this.cantNodosVisitados++;
	}


	@Override
	public void visitar(Compound compound) {
		// TODO Auto-generated method stub
		String ident=compound.toString(this.cantNodosVisitados);
		this.codigoGraph += ident;
		String[] delimitador=ident.split("\\[");
		String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";//creo el enlace del padre con el nuevo nodo 
		this.codigoGraph+=enlace;
		this.auxPadres.push(delimitador[0]);//guardo en la pila al padre.
		this.cantNodosVisitados++;
		ArrayList<AstNodo> listavarLocal=compound.getLocalDeclarationsAndStatements();
		for(int i=0; i<listavarLocal.size();i++){
			listavarLocal.get(i).aceptar(this);
		}
		this.auxPadres.pop();
		
	}


	@Override
	public void visitar(Var variable) {
		// TODO Auto-generated method stub
		String ident="";
		if(variable.getExpresion()==null){
			//ya se que es un nodo terminal. ahora pregunto si el nodo contiene una letra a un numero.
			if(variable.isEsVar()){
				//el nodo contiene una variable.
				ident=variable.toString(this.cantNodosVisitados);
			}else{
				//el nodo contiene un numero.
				ident=variable.toStringNumFactor(this.cantNodosVisitados);
			}
			
			this.codigoGraph +=ident;
			String[] delimitador=ident.split("\\[");
			String enlace= this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
			this.codigoGraph+=enlace;
			this.cantNodosVisitados++;
		}else{
			//en caso de que el nodo no fuera un terminal.
			//pregunto si el nodo contiene una letra a un numero.
			if(variable.isEsVar()){
				//el nodo contiene una variable.
				ident=variable.toString(this.cantNodosVisitados);
			}else{
				//el nodo contiene un numero.
				ident=variable.toStringNumFactor(this.cantNodosVisitados);
			}
			this.codigoGraph+=ident;
			String[] delimitador=ident.split("\\[");
			String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
			this.codigoGraph+=enlace;
			this.auxPadres.push(delimitador[0]);//guardo en la pila al padre.
			this.cantNodosVisitados++;
			variable.getExpresion().aceptar(this);
		}
		
	}


	@Override
	public void visitar(Expression ex) {
		// TODO Auto-generated method stub
		//this.codigoGraph += ex.toString(this.auxPadres);
		String ident=ex.toString(this.cantNodosVisitados);
		this.codigoGraph+=ident;
		String[] delimitador=ident.split("\\[");
		String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
		this.codigoGraph+=enlace;
		this.auxPadres.push(delimitador[0]);
		this.cantNodosVisitados++;
		
		
		ex.getOp1().aceptar(this);
		if(ex.getOp2() instanceof AstNodo){
			((AstNodo) ex.getOp2()).aceptar(this);
		}else{
			this.codigoGraph += this.auxPadres.peek()+"->"+((String)ex.getOp2())+"; \n";
		}
		this.auxPadres.pop();
		
	}


	@Override
	public void visitar(Statement operacion) {
		// TODO Auto-generated method stub
		String ident=operacion.toString(this.cantNodosVisitados);
		this.codigoGraph+=ident;
		String[] delimitador=ident.split("\\[");
		String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
		this.codigoGraph+=enlace;
		this.auxPadres.push(delimitador[0]);
		this.cantNodosVisitados++;
		if(operacion.getTipoOperacion().equalsIgnoreCase("IF") && operacion.getStatement2()!= null){
			//corresponde a un nodo IF con ELSE(gramatica 1).
			operacion.getExpression().aceptar(this);
			operacion.getStatement1().aceptar(this);
			operacion.getStatement2().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("IF") && operacion.getStatement2()==null){
			//corresponde a un nodo IF sin ELSE (gramatica 2).
			operacion.getExpression().aceptar(this);
			operacion.getStatement1().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("WHILE")){
			//corresponde a un nodo WHILE.
			operacion.getExpression().aceptar(this);
			operacion.getStatement1().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("DO")){
			//corresponde a un nodo DO-WHILE.
			operacion.getStatement1().aceptar(this);
			operacion.getExpression().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("RETURN") && operacion.getExpression()!=null){
			//corresponde a un nodo RETURN (gramatica 2).
			operacion.getExpression().aceptar(this);
		}
		//no incluyo el return solo ya que es un terminal.
		
		this.auxPadres.pop();
	}


	@Override
	public void visitar(Call llamado) {
		// TODO Auto-generated method stub
		String ident=llamado.toString(this.cantNodosVisitados);
		this.codigoGraph+=ident;
		String[] delimitador=ident.split("\\[");
		String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
		this.codigoGraph +=enlace;//guardo el enlace en el codigo.
		this.auxPadres.push(delimitador[0]);//guardo el nuevo nodo en la pila 'sin el 0'.
		this.cantNodosVisitados++; //ya que use el numero para la creacion de el nodo funcion.
		ArrayList<AstNodo> lista=llamado.getArgs();//obtengo la lista de parametros de la funcion(call o expresiones).
		for(int i=0; i<lista.size();i++){
			lista.get(i).aceptar(this);
		}
		this.auxPadres.pop();//elimino el padre actual.
	}
	
		

	
	
}
