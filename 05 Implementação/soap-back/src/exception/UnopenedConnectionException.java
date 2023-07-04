package exception;

public class UnopenedConnectionException extends Exception{

	private static final long serialVersionUID = -4584043995420976704L;

	public UnopenedConnectionException(String mensagem) {
		super(mensagem);
	}
}
