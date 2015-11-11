import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import parser.*;
import scanner.*;
import semanticVisitor.*;
import syntaxVisitor.GrapherVisitor;
import visitor.*;
import ast.*;
import tables.*;


public class main {
	public static void correrConGraph(String path) throws FileNotFoundException {
		File arch = new File(path);
		SymbolTable tablaSimbolos = new SymbolTable();
//		System.out.println(SymbolTable.instancia);
		try {
			Yylex lexer = new Yylex(new FileReader(arch));
			parser parser = new parser(lexer);
			AstProgram astProgram = (AstProgram) parser.parse().value;
			GrapherVisitor grapherVisitor = new GrapherVisitor();
			GrapherVisitorExtended grapherVisitorExtended=new GrapherVisitorExtended();
			grapherVisitor.visitar(astProgram);
			//primer recorrido semantico.
			SemanticPass1 sp1=new SemanticPass1();
			System.out.println("iniciando primera pasada ");
			sp1.visitar(astProgram);
			System.out.println("Primera pasada exitosa");
			System.out.println("imprimiendo tabla");
			//sp1.imprimirTabla();
			//segundo recorrido semantico.
			SemanticPass2 sp2=new SemanticPass2();
			System.out.println("iniciando segunda pasada");
			sp2.visitar(astProgram);
			System.out.println("Segunda pasada exitosa");
			System.out.println("Graficando.");
			grapherVisitorExtended.visitar(astProgram);
			System.out.println("terminado.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		java.util.Scanner in = new Scanner(System.in);

		int valor = 0;
		int valor2=0;
		String archivoPrueba2;
		do {
			System.out.println("Elija una opcion: ");
			System.out.println("1) Generar flex , parser");
			System.out.println("2) generarGraph");
			System.out.println("3) Salir");
			System.out.print("Opcion: ");
			valor = in.nextInt();
			switch (valor) {
			case 1:
				generar(args);
				break;
			case 2:
				do{
					System.out.println("elegir ejemplo");
					System.out.println("1. code1.txt");
					System.out.println("2. code2.txt");
					System.out.println("3. code3.txt");
					System.out.println("4. code4.txt");
					System.out.println("5. code5.txt");
					System.out.println("6. code6.txt");
					System.out.println("7. code7.txt");
					
					System.out.print("Opcion: ");
					valor2=in.nextInt();
					switch(valor2){
					case 1:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code1.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
					case 2:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code2.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
						
					case 3:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code3.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
					case 4:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code4.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
					case 5:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code5.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
					case 6:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code6.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
					case 7:
						archivoPrueba2 = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/code7.txt";
						correrConGraph(archivoPrueba2);
						System.out.println("Ejecutado!");
						break;
					default: {
							System.out.println("Opcion no valida!");
							break;
						}
							
					}
				}while(false);
				break;
			case 3:
				System.out.println("adios!!!!!");
				break;
			
			default: {
				System.out.println("Opcion no valida!");
				break;
			}
			}
		} while (valor != 3);

	}

	/**
	 * Funcion que genera los archivos parser.java, sym.java y Yylex.java
	 * 
	 * @param args
	 */
	public static void generar(String[] args) {

		String archSintax = "";
		String archLex = "";
		if (args.length > 0) {
			System.out.println("procesando archivos");
			archLex = args[0];
			archSintax = args[1];
		} else {
			System.out.println("procesando archivos default");
			archLex = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/src/scanner/Lexer.flex";
			archSintax = "C:/Users/JonathanDavis/Dropbox/Compiladores/proyecto taller 3/tallerN3_compiladores/src/parser/Parser.cup";
		}
		String[] alexico = { archLex };
		String[] asintactico = { "-parser", "parser", archSintax };
		// String [] asintactico={"-expect","1",archSintax}; //le dijo q tengo
		// el problema.

		try {
			java_cup.Main.main(asintactico);
		} catch (Exception ex) {
			Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
		}
		jflex.Main.main(alexico);// generamos lexico.
		// movemos hacia los lugares.
		boolean mvAL = moverArch("Yylex.java");
		boolean mvAS = moverArchSin("parser.java");
		boolean mvSym = moverArchSin("sym.java");
		if (mvAL && mvAS && mvSym)
			System.exit(0);
		System.out.println("generado!!");
	}

	/**
	 * Funcion usada para mover los archivos .java generados hacia el folder
	 * "parser".
	 * 
	 * @param archNombre
	 * @return
	 */
	public static boolean moverArchSin(String archNombre) {
		boolean efectuado = false;
		File arch = new File(archNombre);
		if (arch.exists()) {
			System.out.println("\n*** Moviendo " + arch + " \n***");
			Path currentRelativePath = Paths.get("");
			String nuevoDir = currentRelativePath.toAbsolutePath().toString()
					+ File.separator + "src" + File.separator + "parser"
					+ File.separator + arch.getName();
			File archViejo = new File(nuevoDir);
			archViejo.delete();
			if (arch.renameTo(new File(nuevoDir))) {
				System.out.println("\n*** Generado " + archNombre + "***\n");
				efectuado = true;
			} else {
				System.out.println("\n*** No movido " + archNombre + " ***\n");
			}

		} else {
			System.out.println("\n*** Codigo no existente ***\n");
		}
		return efectuado;
	}

	/**
	 * Funcion usada para mover los archivos .java generados hacia el folder
	 * "scanner".
	 * 
	 * @param archNombre
	 * @return
	 */
	public static boolean moverArch(String archNombre) {
		boolean efectuado = false;
		File arch = new File(archNombre);
		if (arch.exists()) {
			System.out.println("\n*** Moviendo " + arch + " \n***");
			Path currentRelativePath = Paths.get("");
			String nuevoDir = currentRelativePath.toAbsolutePath().toString()
					+ File.separator + "src" + File.separator + "scanner"
					+ File.separator + arch.getName();
			File archViejo = new File(nuevoDir);
			archViejo.delete();
			if (arch.renameTo(new File(nuevoDir))) {
				System.out.println("\n*** Generado " + archNombre + "***\n");
				efectuado = true;
			} else {
				System.out.println("\n*** No movido " + archNombre + " ***\n");
			}

		} else {
			System.out.println("\n*** Codigo no existente ***\n");
		}
		return efectuado;
	}

}
