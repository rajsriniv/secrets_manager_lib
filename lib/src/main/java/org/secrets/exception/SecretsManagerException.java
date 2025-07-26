package org.secrets.exception;

public class SecretsManagerException extends Exception {

    public SecretsManagerException(final String message) {
        super(message);
    }

    public SecretsManagerException(final String message, final Throwable clause) {
        super(message, clause);
    }
}
