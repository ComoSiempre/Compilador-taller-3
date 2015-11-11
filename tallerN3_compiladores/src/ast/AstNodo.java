package ast;


public abstract class AstNodo implements visitaNodo {
	
	AstNodo enlaceSemantico=null;//variable utilizada para el enlace en la tabla de simbolos. (proximanmente)
	int fila;//variable que guardara las fila dentro del codigo.
	int columna;//variable que guardara las columna dentro del codigo.
	
	public AstNodo(){
		super();
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}
		
	

}
