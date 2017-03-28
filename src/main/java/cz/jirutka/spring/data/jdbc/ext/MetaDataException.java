package cz.jirutka.spring.data.jdbc.ext;

public class MetaDataException extends RuntimeException {

	private static final long serialVersionUID = -1393252314421506942L;

	public MetaDataException() {
		super();
	}

	public MetaDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetaDataException(String message) {
		super(message);
	}

	public MetaDataException(Throwable cause) {
		super(cause);
	}

}
