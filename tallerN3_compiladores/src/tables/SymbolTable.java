package tables;

import java.util.ArrayList;
import java.util.Stack;


import ast.*;

public class SymbolTable {
	
	//lista de bloques que guarda una rama dependiendo del bloque.
	Stack<Ambito> indiceBloques = new Stack<>();
	int contBloques=1;//variable usada para controlar el numero de contextos al ingresar a la tabla de simbolos.
	public static SymbolTable instancia;//instancia de la tabla de simbolos.
	//constructor.
	public SymbolTable(){
		instancia = this;
	}
		
	/**
	 * funcion que inserta un nodo dentro del indice de bloques actual (dentro de la lista del bloque).
	 * 
	 * @param nodo
	 */
	public boolean ingresarDeclaracion(AstNodo nodo){
				
		boolean result = this.indiceBloques.peek().ingresarNodo(nodo); //true: exito, false: existe duplicado.
		
		return result;
	}
	
		
	/**
	 * funcion que crea un nuevo bloque o Ambito(contexto) dentro de la tabla de simbolos.
	 * 
	 */
	public void crearBloque(Ambito padre){
		Ambito nuevoContexto = new Ambito(contBloques);
		nuevoContexto.setContextoPadre(padre);
		this.indiceBloques.push(nuevoContexto);
		contBloques++;
	}
	public Ambito eliminarBloque(){
		Ambito ambito= this.indiceBloques.pop();
		return ambito;
		
	}
	/**
	 * funcion que busca la declaracion segun la variable.
	 * @param var
	 * @return
	 */
	public AstNodo buscarDeclaracion(AstNodo var, Ambito am){
		AstNodo nodoDec= am.existeVariable(var);
		if(nodoDec==null){
			return null;
		}
		return nodoDec;
		
	}
	
	/**
	 * funcion usada teniendo una variable, verificar si esta declarada en algun contexto desde su actual hasta el global.
	 * 
	 * @param var
	 * @param contextoActual
	 * @return AstNodo en caso de que encuentre el AstVarDec, NULL en caso de que no exista declaracion.
	 */
	public AstNodo verificarDeclaracion(AstNodo var, int contextoActual){
		Ambito am= this.buscarContexto(contextoActual);//busco el contexto en la pila.
		AstNodo aux;
		//ciclo usado para viajar entre contextos hasta el global.
		while(am != null){
			aux=am.existeSimbolo(var);//se verifica si existe la declaracion en el ambito.
			if(aux!=null){
				//existe la declaracion, se envia la declaracion.
				return aux;
			}
			//no existe la declaracion en el contexto, se pasa al contexto padre.
			am=am.getContextoPadre();
			
		}
		return null;//en caso de que no se encontro ninguna declaracion en el programa.
	}
	
	/**
	 * funcion usada para buscar una declaracion de funcion segun su id.
	 * @param ID
	 * @return nodo funcion
	 */
	public AstNodo BuscarFuncion(String ID){
		Ambito am= this.buscarContexto(1);
		for(AstNodo nodo : am.listaDeclaraciones){
			if(nodo instanceof AstFunction){
				if(((AstFunction) nodo).getID().equals(ID)){
					//una coincidencia.
					return nodo;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * funcion usada para determinar si el ambito esta dentro de la tabla de simbolos.
	 * 
	 * @param ambito
	 * @return
	 */
	public boolean existeBloque(Ambito ambito){
		for(Ambito am : this.indiceBloques){
			if(am==ambito) return true;
		}
		return false;
	}
	
	/**
	 * funcion usada para verificar si no se ha usado la tabla de simbolos.
	 * @return
	 */
	public boolean estaVacio(){
		if(this.indiceBloques.empty()){
			return true;
		}else{
			return false;
		}
	}
	
	public void imprimirTabla(){
		Stack<Ambito> pilaAux=new Stack<Ambito>();
		while(!this.indiceBloques.isEmpty()){
			Ambito am= this.indiceBloques.pop();
			pilaAux.add(am);
			System.out.println("contexto: "+am.getNumeroBloque());
			if(am.getNumeroBloque() != 1){
				System.out.println("contexo anidado: "+am.getContextoPadre().getNumeroBloque());
			}
			am.imprimir();
		}
		//vuelvo a ingresar los datos a la pila principal.
		while(!pilaAux.isEmpty()){
			this.indiceBloques.push(pilaAux.pop());
		}
	}
	
	public Ambito obtenerPrimerAmbitoPila(){
		return this.indiceBloques.peek();
	}
	
	/**
	 * funcion usada para buscar un contexto en la pila segun el numero de contexto pedido.
	 * 
	 * @param numeroContexto
	 * @return
	 */
	public Ambito buscarContexto(int numeroContexto){
		for(Ambito a : this.indiceBloques){
			if(a.getNumeroBloque()== numeroContexto){
				return a;
			}
		}
		return null;
	}
	
	/**
	 * funcion usada para obtener los bloques anidados segun la declaracion.
	 * 
	 */
//	public obtenerRamaContextos(int numeroContexto){
//		Ambito global = this.indiceBloques.get(this.indiceBloques.size()-1);//obtengo el ambito global.
//		ArrayList<Ambito> auxIndice=new ArrayList<>();
//		ArrayList<Ambito> rama=new ArrayList<>();
//		while(!this.indiceBloques.isEmpty()){
//			auxIndice.add(this.indiceBloques.pop());//se guarda en la lista la tabla.
//		}
//		rama.add(global);
//		
//		
//	}
	
	
	
	
	public Stack<Ambito> getIndiceBloques() {
		return indiceBloques;
	}

	public void setIndiceDeBloques(Stack<Ambito> indiceDeBloques) {
		this.indiceBloques = indiceDeBloques;
	}

	public int getContBloques() {
		return contBloques;
	}

	public void setContBloques(int contBloques) {
		this.contBloques = contBloques;
	}
	
	
	
}
