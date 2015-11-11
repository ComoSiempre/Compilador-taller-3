package semanticVisitor;

import java.util.ArrayList;
import java.util.Stack;

import tables.SymbolTable;
import ast.*;
import tables.*;
import visitor.visitor;
/**
 * clase creada para el proceso de declaraciones de funciones y variables.
 * creandose la table de simbolos con las declaraciones de cada bloque.
 * 
 * se analizara el ast como recorrido preorden para detectar las declaraciones.
 * @author JonathanDavis
 *
 */
public class SemanticPass1 implements visitor {
	SymbolTable tablaSimbolos;
	Stack<Ambito> aux=new Stack<>();//auxiliar para guardar los ambitos en el recorrido del ast.
	boolean veriReturn=false;
	AstFunction actual;
	Ambito padre;//variable auxiliar que guardara el padre.
	int numeroBloque=2;
	
	public SemanticPass1(){
		this.tablaSimbolos = SymbolTable.instancia;
//		System.out.println(SymbolTable.instancia);
	}
	/**
	 * funcion usada para buscar dentro de la pila auxiliar los ambitos guardados.
	 * 
	 * @param indice
	 * @return
	 */
	public Ambito busqueda(int indice){
		for(Ambito a  : this.aux){
			if(a.getNumeroBloque()==indice){
				return a;
			}
		}
		return null;
	}
	
	/**
	 * funcion usada para imprimir la tabla de simbolos.
	 * 
	 */
	public void imprimirTabla(){
		this.tablaSimbolos.imprimirTabla();
	}
	public SymbolTable getTablaSimbolos(){
		return this.tablaSimbolos;
		
	}
	@Override
	public void visitar(AstProgram astProgram) {
		// TODO Auto-generated method stub
		System.out.println("visito nodo programa");
		//primer bloque;
		//creo el bloque.
		this.tablaSimbolos.crearBloque(null);//creo un bloque global con un padre null.
		//System.out.println("creacion ambito: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//obtengo la lista de hijos del la raiz.
		ArrayList<AstNodo> listaDec= astProgram.getDeclaraciones();
		//continuo el recorrido.
		for(AstNodo l: listaDec){
			l.aceptar(this);
			
		}
		
		//se busca ordenar la tabla de simbolos, utilizando la pila auxiliar.
		for(int i=2;i<this.aux.size()+2; i++){
			Ambito a=busqueda(i);
			if(a != null)this.tablaSimbolos.getIndiceBloques().push(a);
		}
		System.out.println("termino nodo programa");
	}
	@Override
	public void visitar(AstVarDec astVarDec) {
		// TODO Auto-generated method stub
		//System.out.println("visito nodo varDec: "+astVarDec.getID()+":"+astVarDec.getTipoID());
		//System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//guardo la declaracion en el bloque(ambito).
		boolean result = this.tablaSimbolos.ingresarDeclaracion(astVarDec);
		if(!result){
			System.out.println("Error Semantico!, existe variable duplicada:"+ astVarDec.getID()+" en fila "+astVarDec.getFila()+" columna "+astVarDec.getColumna());
			System.exit(0);
		}
		if(astVarDec.getTipoID().equalsIgnoreCase("void")){
			System.out.println("Error Semantico!, no puede haber variable de tipo VOID:"+ astVarDec.getID()+" en fila "+astVarDec.getFila()+" columna "+astVarDec.getColumna());
			System.exit(0);
		
		}
		//System.out.println("termino nodo varDec");
	}
	@Override
	public void visitar(AstFunction astFunction) {
		// TODO Auto-generated method stub
		//System.out.println("visita nodo funcion: "+astFunction.getID()+":"+astFunction.getTipoIDFuncion());
		//System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		veriReturn=false;
		actual=astFunction;
		//guardo la declaracion de funcion en la tabla de simbolos.
		boolean result = this.tablaSimbolos.ingresarDeclaracion(astFunction);
		if(!result){
			System.out.println("Error Semantico!, existe funcion duplicada:"+ astFunction.getID()+" en fila "+astFunction.getFila()+" columna "+astFunction.getColumna());
			System.exit(0);
		}
		numeroBloque++;//cuento el siguriente bloque.
		
		//creo un nuevo bloque que contendra las variables locales y los parametros.
		this.tablaSimbolos.crearBloque(this.tablaSimbolos.getIndiceBloques().peek());
		//System.out.println("creacion ambito : "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//obtengo los parametros.
		for(AstNodo nodo: astFunction.getParametros()){
			nodo.aceptar(this);
			
		}
		AstNodo com=astFunction.getContenido();
		if(com!=null){
			com.aceptar(this);
			if(astFunction.getTipoIDFuncion().equalsIgnoreCase("int") && this.veriReturn==false){//siendo funcion int, no tiene un return
				System.out.println("Error semantico!, funcion"+astFunction.getID()+" no tiene retorno int. fila"+astFunction.getFila()+", columna "+astFunction.getColumna());
				System.exit(0);
			}
		}
		
		
		
		Ambito subir=this.tablaSimbolos.eliminarBloque();//elimino el bloque de la tabla para guardarlo en el aux. al subir nuevamente.
		aux.push(subir);//se guarda el boque eliminado de la tabla.
		if(this.tablaSimbolos.estaVacio()){
			//quiere decir que se elimino el contexto global.
			//se agrega el global a la pila (pasa por problemas de contexto)
			this.tablaSimbolos.getIndiceBloques().push(subir);
		}
		veriReturn=false;
		actual=null;
		//System.out.println("termino nodo funcion");
	}
	
	@Override
	public void visitar(Param param) {
		// TODO Auto-generated method stub
		//System.out.println("visita nodo param: "+param.getID()+": "+param.getTipoID());
		//System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//guardo la declaracion de parametros en la tabla de simbolos..
		boolean result = this.tablaSimbolos.ingresarDeclaracion(param);
//		if(!result){
//			System.out.println("Error Semantico!, tipo de parametro no declarado:"+ ((Param) actual).getID());
//			System.exit(0);
//		}
		//System.out.println("termino nodo param");
	}
	@Override
	public void visitar(Compound compound) {
		// TODO Auto-generated method stub
//		System.out.println("visita nodo compound");
//		System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//obtengo la lista de declaracionesyStatements.
		for(AstNodo nodo: compound.getLocalDeclarationsAndStatements()){
			nodo.aceptar(this);
		}
		//System.out.println("termino nodo compound");
	}
	@Override
	public void visitar(Var variable) {
		// TODO Auto-generated method stub
//		System.out.println("visita var, no se hace nada");
//		System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
//		System.out.println("termino nodo var");
	}
	@Override
	public void visitar(Expression ex) {
		// TODO Auto-generated method stub
//		System.out.println("visita nodo expresion:"+ex.getOperador());
//		System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//no deberia haber declaraciones aqui.
		ex.getOp1().aceptar(this);
		if(ex.getOp2() instanceof AstNodo){
			((AstNodo) ex.getOp2()).aceptar(this);
			
		}else{
			//op2 es string.
			
		}
//		System.out.println("termino nodo expresion");
	}
	@Override
	public void visitar(Statement operacion) {
		// TODO Auto-generated method stub
//		System.out.println("visita nodo Statement: "+operacion.getTipoFuncion()+" tipo op: "+operacion.getTipoOperacion());
//		System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
		//aqui podria ser cualquiera de las condicionantes o ciclos que se consideran nuevos ambitos.
		
		//sigo el recorrido..
		if(operacion.getTipoOperacion().equalsIgnoreCase("IF") && operacion.getStatement2()!= null){
			//corresponde a un nodo IF con ELSE(gramatica 1).
			
			operacion.getExpression().aceptar(this);
			//guardo el padre real del else.
			Ambito padreElse=this.tablaSimbolos.getIndiceBloques().peek();
			//bloque correspondiente al if.
			numeroBloque++;
			//creo un nuevo bloque correspondiente.
			this.tablaSimbolos.crearBloque(this.tablaSimbolos.getIndiceBloques().peek());
//			System.out.println("creacion ambito: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
			operacion.getStatement1().aceptar(this);
			
			//bloque correspondiente al else.
			numeroBloque++;
			//creo un nuevo bloque correspondiente.
			this.tablaSimbolos.crearBloque(padreElse);
//			System.out.println("creacion ambito: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
			operacion.getStatement2().aceptar(this);
			
			//se elimina el bloque de else y se guarda en el auxiliar.
			Ambito subir=this.tablaSimbolos.eliminarBloque();//elimino el bloque de la tabla para guardarlo en el aux. al subir nuevamente.
			aux.push(subir);//se guarda el boque eliminado de la tabla.
			
			//elimino el bloque correspondiente al bloque del ELSE y lo guardo en auxiliar.
			subir=this.tablaSimbolos.eliminarBloque();//elimino el bloque de la tabla para guardarlo en el aux. al subir nuevamente.
			aux.push(subir);//se guarda el boque eliminado de la tabla.
			
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("IF") && operacion.getStatement2()==null){
			//corresponde a un nodo IF sin ELSE (gramatica 2).
			operacion.getExpression().aceptar(this);//parametros del if.
			//bloque correspondiente al IF.
			numeroBloque++;
			//creo un nuevo bloque correspondiente.
			this.tablaSimbolos.crearBloque(this.tablaSimbolos.getIndiceBloques().peek());
//			System.out.println("creacion ambito: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
			operacion.getStatement1().aceptar(this);
			
			//guardo el bloque en auxiliar, eliminandolo de la tabla de simbolos.
			Ambito subir=this.tablaSimbolos.eliminarBloque();//elimino el bloque de la tabla para guardarlo en el aux. al subir nuevamente.
			aux.push(subir);//se guarda el boque eliminado de la tabla.
			
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("WHILE")){
			//corresponde a un nodo WHILE.
			operacion.getExpression().aceptar(this);

			//bloque dentro del WHILE
			numeroBloque++;
			//creo un nuevo bloque correspondiente.
			this.tablaSimbolos.crearBloque(this.tablaSimbolos.getIndiceBloques().peek());
//			System.out.println("creacion ambito: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
			operacion.getStatement1().aceptar(this);
			
			//guardo el bloque en auxiliar, eliminandolo de la tabla de simbolos.
			Ambito subir=this.tablaSimbolos.eliminarBloque();//elimino el bloque de la tabla para guardarlo en el aux. al subir nuevamente.
			aux.push(subir);//se guarda el boque eliminado de la tabla.
			
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("DO")){
			//corresponde a un nodo DO-WHILE.
			numeroBloque++;
			//creo un nuevo bloque correspondiente.
			this.tablaSimbolos.crearBloque(this.tablaSimbolos.getIndiceBloques().peek());
//			System.out.println("creacion ambito: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
			operacion.getStatement1().aceptar(this);
			operacion.getExpression().aceptar(this);
			
			//guardo el bloque en auxiliar, eliminandolo de la tabla de simbolos.
			Ambito subir=this.tablaSimbolos.eliminarBloque();//elimino el bloque de la tabla para guardarlo en el aux. al subir nuevamente.
			aux.push(subir);//se guarda el boque eliminado de la tabla.
		}else if(operacion.getTipoOperacion().equalsIgnoreCase("RETURN") && operacion.getExpression()!=null){
			//corresponde a un nodo RETURN (gramatica 2).
			//no se considera un nuevo contexto aki.
			//se marca que tiene un return la funcion.
			this.veriReturn=true;
			if(!actual.getTipoIDFuncion().equalsIgnoreCase(operacion.getTipoFuncion())){
				System.out.println("Error semantico!, "+operacion.getTipoOperacion()+" no corresponde al tipo de funcion "+actual.getTipoIDFuncion()+", tipo de return: "+operacion.getTipoFuncion());
				System.exit(0);
			}
			operacion.getExpression().aceptar(this);
		}else{
			//coresponderia al RETURN; (void)
			if(!actual.getTipoIDFuncion().equalsIgnoreCase(operacion.getTipoFuncion())){
				System.out.println("Error semantico!, "+operacion.getTipoOperacion()+" no corresponde al tipo de funcion "+actual.getTipoIDFuncion()+", tipo de return: "+operacion.getTipoFuncion());
				System.exit(0);
			}
		}
		
		
//		System.out.println("termino nodo statement");
	}
	@Override
	public void visitar(Call llamado) {
		// TODO Auto-generated method stub
//		System.out.println("termino nodo Call, no se hace nada");
//		System.out.println("ambito actual: "+this.tablaSimbolos.getIndiceBloques().peek().getNumeroBloque());
//		System.out.println("termino nodo Call");
	}
}
