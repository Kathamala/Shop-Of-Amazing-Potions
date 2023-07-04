package exception;

public class InitiatedTransactionException extends Exception {
	private static final long serialVersionUID = -4085541527015437842L;

	public InitiatedTransactionException(String mensagem) {
	    super(mensagem);
	}
}
