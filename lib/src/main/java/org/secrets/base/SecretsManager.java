package org.secrets.base;

public interface SecretsManager {

    String fetchSecrets(final String secretsId, final String credentials);

    void addSecrets(final String secretsId, final String key, final String value);

    boolean deleteSecrets(final String secretsId);
}
