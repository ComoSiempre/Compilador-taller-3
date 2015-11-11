package visitor;
import ast.*;
//
public interface visitor {
	public void visitar(AstProgram astProgram);
	public void visitar(AstFunction astFunction);
	public void visitar(AstVarDec astVarDec);
	public void visitar(Param param);
	public void visitar(Compound compound);
	public void visitar(Var variable);
	public void visitar(Expression ex);
	public void visitar(Statement operacion);
	public void visitar(Call llamado);
}
