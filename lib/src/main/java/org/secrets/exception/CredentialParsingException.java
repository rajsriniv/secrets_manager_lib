package org.secrets.exception;

public class CredentialParsingException extends RuntimeException {

    /**
     * Constructor for CredentialParsingException.
     * @param message - error message
     * @param cause - cause of the exception
     */
    public CredentialParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for CredentialParsingException.
     * @param cause - cause of the exception
     */
    public CredentialParsingException(final Throwable cause) {
        super(cause);
    }
}
