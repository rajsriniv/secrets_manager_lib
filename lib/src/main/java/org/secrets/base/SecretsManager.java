package org.secrets.base;

import org.secrets.exception.SecretsManagerException;

import java.util.Map;

public interface SecretsManager {

    String fetchSecrets(final String secretsId)
            throws SecretsManagerException;

    boolean addSecrets(final String secretsId, Map<String, String> secrets)
            throws SecretsManagerException;

    boolean deleteSecrets(final String secretsId) throws SecretsManagerException;
}
