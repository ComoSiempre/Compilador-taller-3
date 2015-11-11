package tables;

import java.util.ArrayList;

import ast.*;


/**
 * clase usada para manejas los ambitos dentro de la tabla de simbolos.
 * 
 * simulara la estructura de bosque de arboles de la tabla de simbolos.
 * 
 * @author JonathanDavis
 *
 */
public class Ambito {
	ArrayList<AstNodo> listaDeclaraciones=new ArrayList<>();//tendra declaraciones de variable o funciones.
	Ambito contextoPadre=null;//variabel que guarda el contexto anterior al actual.
	int numeroBloque=0;//variable que guarda el numero de bloque del contexto.
	
	public Ambito(){
		
	}
		
	public Ambito(int num){
		this.numeroBloque=num;
	}

	/**
	 * funcion usada para ingresar la declaracion dentro del ambito.
	 * 
	 * @param nodo, pueden ver AstVarDec, AstFunction, Param
	 * @return true: si fue exitoso el ingreso, false: si no fue exitoso (existe un duplicado);
	 */
	public boolean ingresarNodo(AstNodo nodo){
		AstNodo aux=existeSimbolo(nodo);
		if(aux != null) return false;//ya que existe una declaracion igual, no es exitoso el ingreso nuevo.
		this.listaDeclaraciones.add(nodo);
		return true;
	}
	
	/**
	 * funcion usada para verificar duplicado de simbolos en el ambito (intependiente del tipo).
	 * usado en la primera pasada.
	 * @param nodoeval : es VarDec o Function o param o Var.
	 * @return AstNodo: existe el simbolo en el ambito (duplicado), NULL: no existe el simbolo.
	 */
	public AstNodo existeSimbolo(AstNodo nodoEval){
		for(AstNodo nodo : this.listaDeclaraciones){
			//en caso de que se deba verificar funciones.
			if(nodo instanceof AstFunction && nodoEval instanceof AstFunction){
				//aqui se debe verificar el nombre, el tipo y los parametros.
				if(((AstFunction) nodo).getID().equals(((AstFunction)nodoEval).getID()) && 
						((AstFunction) nodo).getTipoIDFuncion().equals(((AstFunction)nodoEval).getTipoIDFuncion())){
					//veo ahora los parametros.
					ArrayList<AstNodo> aux1=((AstFunction) nodo).getParametros();
					ArrayList<AstNodo> aux2=((AstFunction) nodoEval).getParametros();
					if(this.listasIguales(aux1, aux2)){
						return nodo; //se confirma la duplicacion.
					}
				}
			}
			//en caso de que se debe verificar si existe la declaracion anteriormente.
			if(nodo instanceof AstVarDec && nodoEval instanceof AstVarDec){
				if(((AstVarDec) nodo).getID().equals(((AstVarDec)nodoEval).getID())) return nodo;
			}
			//en caso de que se deba verificar que una variable esta declarada.
			if(nodo instanceof AstVarDec && nodoEval instanceof Var){
				if(((AstVarDec)nodo).getID().equals(((Var)nodoEval).getID())) return nodo;
			}
			//en caso de queren comparar una variable con un parametro de un funcion.
			if(nodo instanceof Param && nodoEval instanceof Var){
				//puede haber dos tipos de parametros, el int y el void.
				if(((Param)nodo).isEsVoid()){
					//corresponde a un parametro VOID, por lo que no deberia compararlo a una variable.
					//ojo que existen variables locales y parametros void dentro de laslistas en ambitos.
				}else{
					//corresponde a un parametro int.
					if(((Param)nodo).getID().equals(((Var)nodoEval).getID())) return nodo;
				}
				
				
			}
		}
		
		return null;
	}
	
	/**
	 * funcion usada para verificar la existencia de la declaracion de variable segun un Var.
	 * 
	 * @param var
	 * @return
	 */
	public AstNodo existeVariable(AstNodo var){
		for(AstNodo nodo : this.listaDeclaraciones){
			if(nodo instanceof AstVarDec){
				//veo los declaraciones de variables.
				if(((AstVarDec) nodo).getID().equals(((Var)var).getID())){
					//existe la declaracion 
					return nodo;
				}
				
			}
			if(nodo instanceof Param){
				//delcaraciones en parametros.
				if(((Param)nodo).getID().equals(((Var)var).getID())){
					//existe la declaracion en el parametro.
					return nodo;
				}
			}
		}
		return null;
	}
	
//	public boolean existeSimboloAnidado(AstNodo nodoEval){
//		//aqui deberia estar el llamado al contexto padre y verificar si existe en ese nivel.
//		Ambito actual=this;
//		
//		boolean result=false;
//		while(actual!=null){
//			result=actual.existeSimbolo(nodoEval);
//			if(result){
//				//existe en el anidado.
//				return true;//se encuentra declaracion en un anidado.
//			}
//			actual=actual.getContextoPadre();
//		}
//		return false;//no se encuentra declaracion.
//	}
	
	/**
	 * funcion usada para verificar la equivalencia de parametros entre funciones.
	 * 
	 * @param aux1 lista de parametros de funcion 1
	 * @param aux2 lista de parametros de funcion 2
	 * @return true si son iguales , false si son distintos.
	 */
	private boolean listasIguales(ArrayList<AstNodo> aux1, ArrayList<AstNodo> aux2){
		int contEquivalencias=0;
		if(aux1.size()!= aux2.size()){
			return false;
		}
		//en caso de que existen misma cantidad de parametros, se debe preguntar por los void.
		
		for(int i=0; i<aux1.size();i++){
			if(((Param)aux1.get(i)).isEsVoid()){
				//corresponderia a un parametro void, por lo que pregunto si el parametro en aux2 seria void tambien.
				if(((Param)aux2.get(i)).isEsVoid()){
					//los dos tienen para metros void. retorno true;
					return true;
				}else{
					return false;
				}
				
			}
			
			//ahora ya verificado los parametros void. desde aki se consideran que son parametros de variables.
			if(((Param)aux1.get(i)).getID().equals(((Param)aux2.get(i)).getID()) &&
					((Param)aux1.get(i)).getTipoID().equals(((Param)aux2.get(i)).getTipoID()) &&
					((Param)aux1.get(i)).isBrackets()== ((Param)aux2.get(i)).isBrackets() &&
					((Param)aux1.get(i)).isEsVoid() == ((Param)aux2.get(i)).isEsVoid()){
				contEquivalencias++;
			}
		}
		if(contEquivalencias == aux1.size()) return true;
		return false;
	}
	

	public void imprimir(){
		for(AstNodo nodo : this.listaDeclaraciones){
			if(nodo instanceof AstVarDec){
				System.out.println(((AstVarDec) nodo).getID()+" :: "+((AstVarDec) nodo).getTipoID());
			}
			if(nodo instanceof AstFunction){
				System.out.println(((AstFunction) nodo).getID()+" * "+((AstFunction) nodo).getTipoIDFuncion());
			}
			if(nodo instanceof Param){
				if(((Param)nodo).isEsVoid()){
					System.out.println("Parametro->"+((Param)nodo).getTipoID());
				}
				if(((Param)nodo).isBrackets()){
					System.out.println("Parametro->"+((Param) nodo).getID()+" || "+((Param) nodo).getTipoID()+"[]");
				}else if(!((Param)nodo).isEsVoid()){
					System.out.println("Parametro->"+((Param) nodo).getID()+" :: "+((Param) nodo).getTipoID());
				}
				
			}
		}
	}
	
	
	
	public ArrayList<AstNodo> getListaDeclaraciones() {
		return listaDeclaraciones;
	}

	public void setListaDeclaraciones(ArrayList<AstNodo> listaDeclaraciones) {
		this.listaDeclaraciones = listaDeclaraciones;
	}

	public int getNumeroBloque() {
		return numeroBloque;
	}

	public void setNumeroBloque(int numeroBloque) {
		this.numeroBloque = numeroBloque;
	}

	public Ambito getContextoPadre() {
		return contextoPadre;
	}

	public void setContextoPadre(Ambito contextoPadre) {
		this.contextoPadre = contextoPadre;
	}
	
	
	
	

}
