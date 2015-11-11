package semanticVisitor;

import java.util.ArrayList;
import java.util.Stack;

import ast.*;
import tables.*;
import visitor.*;

/**
 * clase usada para la verificacion de tipos, utilizado la tabla de contenido ya creado en la primera pasada semantica.
 * 
 * @author JonathanDavis
 *
 */
public class SemanticPass2 implements visitor{
	Stack<Ambito> pilaAux=new Stack<>();//pila que guardara el numero de contexto actual.
	SymbolTable tablaSimbolos;
	String tipoIDVar;//guardo el tipo de variable.
	int context=1;//variable que controla el contexto actual por visita.
	
	
	
	
	public SemanticPass2(){
		this.tablaSimbolos = SymbolTable.instancia;
		System.out.println(SymbolTable.instancia);
		
	}
	
	@Override
	public void visitar(AstProgram astProgram) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo programa");
		Ambito contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
		pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
		context++;//para crear el siguiente contexto.
		
		ArrayList<AstNodo> lista=astProgram.getDeclaraciones();
		for(int i=0; i<lista.size();i++){
			lista.get(i).aceptar(this);
		}
	}

	@Override
	public void visitar(AstFunction astFunction) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo varDec: "+astFunction.getID()+":"+astFunction.getTipoIDFuncion());
			
		//hay contenido, por lo tanto un bloque nuevo.
		Ambito contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
		pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
		context++;
		
		ArrayList<AstNodo> lista=astFunction.getParametros();//obtengo la lista de parametros de la funcion
		for(int i=0; i<lista.size();i++){
			lista.get(i).aceptar(this);
		}
		
		AstNodo contenido = astFunction.getContenido();
		if(contenido!=null){
			contenido.aceptar(this);
		}
		
		
		if(this.pilaAux.size()!=1){
			this.pilaAux.pop();
		}	
//		System.out.println("termino Funcion");
	}

	@Override
	public void visitar(AstVarDec astVarDec) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo varDec: "+astVarDec.getID()+":"+astVarDec.getTipoID());
//		System.out.println("contexto actual: "+(context-1));
//		System.out.println("termino vardec");
	}

	@Override
	public void visitar(Param param) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo param: "+param.getID()+":"+param.getTipoID());
//		System.out.println("contexto actual: "+(context-1));		
//		System.out.println("termino param");
	
	}

	@Override
	public void visitar(Compound compound) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo compound ");
//		System.out.println("contexto actual: "+(context-1));
		ArrayList<AstNodo> listavarLocal=compound.getLocalDeclarationsAndStatements();
		for(int i=0; i<listavarLocal.size();i++){
			listavarLocal.get(i).aceptar(this);
		}
//		System.out.println("termino compound");
	}

	@Override
	public void visitar(Var variable) {//viene de un call o una expresion.
		// TODO Auto-generated method stub
//		System.out.println("visito nodo var: "+variable.getID()+" numero:"+variable.getNumber());
//		System.out.println("contexto actual: "+(context-1));
		
	
		//primero verifico si esta declarado en el contexto.
//		System.out.println("contexto actual: "+(context-1)+" contexto en la pila aux: "+this.pilaAux.peek().getNumeroBloque());
		Ambito contextoActual=this.pilaAux.peek();
		//se pide la verificacion.
		AstNodo declaracionEncontrada;
		if(variable.isEsVar()){
			//corresponde a una variable.
			declaracionEncontrada=this.tablaSimbolos.verificarDeclaracion(variable, contextoActual.getNumeroBloque());
			if(declaracionEncontrada ==null){
				System.out.println("Error semantico!, variable no definida: "+variable.getID()+" en fila "+variable.getFila()+", columna "+variable.getColumna());
				System.exit(0);
			}
			if(declaracionEncontrada instanceof AstVarDec){
				this.tipoIDVar=((AstVarDec)declaracionEncontrada).getTipoID();
			}else if(declaracionEncontrada instanceof Param){
				this.tipoIDVar=((Param)declaracionEncontrada).getTipoID();
			}
		}else{
			//el nodo corresponde aun numero.
			//se que es INT.
			this.tipoIDVar="int";
		}
//		System.out.println("termino var");
	}

	@Override
	public void visitar(Expression ex) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo expresion: "+ex.getOperador());
//		System.out.println("contexto actual: "+(context-1));
		
		//aqui tengo q verificar q las expresiones cumplen con los tipos correctos.
		String decla1, decla2;//variable que guardan los tipos.
			
			ex.getOp1().aceptar(this);//var.
			decla1=this.tipoIDVar;//guardo el tipo de declaracion de la visita de var.
			if(ex.getOp2() instanceof AstNodo){
				((AstNodo) ex.getOp2()).aceptar(this);
				decla2=this.tipoIDVar;//guardo la declaracion de la visita de expresion.
			
				//verifico en caso de que haya una declaracion entre variable y un numero.
				if(!(decla2.equalsIgnoreCase("int")) || !(decla1.equalsIgnoreCase("int"))){
					System.out.println("Error Semantico!, operaciones aritmeticas requieren operando INT. fila: "+ex.getFila()+" columna: "+ex.getColumna());
					System.exit(0);
				}
			}else{
				//operacion es una nuemro.
				decla2=this.tipoIDVar;//guardo la declaracion de la visita de numero.
				
				//verifico en caso de que haya una declaracion entre variable y un numero.
				if(!decla1.equalsIgnoreCase("int") || !(decla1.equalsIgnoreCase("int"))){
					System.out.println("Error Semantico!, tipo incompatible en asignacion (int). fila: "+ex.getFila()+" columna: "+ex.getColumna());
					System.exit(0);
				}
			}
			
			ex.setTipo(decla1);
			
//			System.out.println("termino expresion ");
			
		
	
		
	}

	@Override
	public void visitar(Statement operacion) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo Statement: "+operacion.getTipoFuncion()+":"+operacion.getTipoOperacion());
//		System.out.println("contexto actual: "+(context-1));
		
		
		if(operacion.getTipoOperacion().equalsIgnoreCase("IF") && operacion.getStatement2()!= null){
			//corresponde a un nodo IF con ELSE(gramatica 1).
			operacion.getExpression().aceptar(this);
			
			Ambito contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
			pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
			context++;
			
			operacion.getStatement1().aceptar(this);
			
			contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
			pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
			context++;
			
			operacion.getStatement2().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("IF") && operacion.getStatement2()==null){
			//corresponde a un nodo IF sin ELSE (gramatica 2).
			operacion.getExpression().aceptar(this);
			
			Ambito contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
			pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
			context++;
			
			operacion.getStatement1().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("WHILE")){
			//corresponde a un nodo WHILE.
			operacion.getExpression().aceptar(this);
			
			Ambito contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
			pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
			context++;
			
			operacion.getStatement1().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("DO")){
			//corresponde a un nodo DO-WHILE.
			
			Ambito contexto=this.tablaSimbolos.buscarContexto(context);//busco el contexto en la tabla de simbolos
			pilaAux.push(contexto);//lo guardo en el auxiliar.(estoy en este contexto);
			context++;
			
			operacion.getStatement1().aceptar(this);
			operacion.getExpression().aceptar(this);
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("RETURN") && operacion.getExpression()!=null){
			//corresponde a un nodo RETURN (gramatica 2).
			operacion.getExpression().aceptar(this);
		}else{
			//seria el return de gramatica 1.
			
			
		}
		
		this.pilaAux.pop();
//		System.out.println("termino statement");
	}

	@Override
	public void visitar(Call llamado) {
		// TODO Auto-generated method stub
//		System.out.println("visito nodo call: "+llamado.getID());
//		System.out.println("contexto actual: "+(context-1));
		
		//verifico si existe la declaracion de la funcion.
		AstNodo funcion= this.tablaSimbolos.BuscarFuncion(llamado.getID());
		if(funcion==null){
			System.out.println("Error semantico!, Invocacion de funcion no definida:funcion de nombre: "+llamado.getID()+",fila "+llamado.getFila()+" columna "+llamado.getColumna());
			System.exit(0);
		}
		ArrayList<AstNodo> lista=llamado.getArgs();//obtengo la lista de parametros de la funcion(call o expresiones).
		if(lista.size() != ((AstFunction)funcion).getParametros().size()){
			System.out.println("Error semantico!, Invocacion de funcion no definida (inconsistencia de parametros): fila "+llamado.getFila()+" columna "+llamado.getColumna());
			System.exit(0);
		}
		for(int i=0; i<lista.size();i++){
			lista.get(i).aceptar(this);
		}
//		System.out.println("termino call");
	}

	/**
	 * funcion usada para imprimir la table de simbolos.
	 * 
	 */
	public void imprimirTabla(){
		this.tablaSimbolos.imprimirTabla();
	}
////	public SymbolTable getTablaSimbolos() {
////		return tablaSimbolos;
////	}
//
//	public void setTablaSimbolos(SymbolTable tablaSimbolos) {
//		this.tablaSimbolos = tablaSimbolos;
//	}
	
}
