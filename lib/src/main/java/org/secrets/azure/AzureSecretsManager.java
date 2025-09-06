package org.secrets.azure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.secrets.base.SecretsManager;
import org.secrets.exception.CredentialParsingException;
import org.secrets.exception.SecretsManagerException;

import java.util.Map;
import java.util.Objects;

public class AzureSecretsManager implements SecretsManager {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    private final AzureCredentials azureCredentials;

    /**
     * Constructor.
     * @param credentials - credentials to build secrets manager client
     */
    public AzureSecretsManager(final String credentials) {
        Objects.requireNonNull(credentials);
        try {
            azureCredentials = OBJECT_MAPPER.readValue(credentials, AzureCredentials.class);
        } catch (JsonProcessingException e) {
            throw new CredentialParsingException(e);
        }
    }

    private void buildSecretClient() {
        DefaultAzureCredential
    }

    /**
     * @param secretsId
     * @return
     * @throws SecretsManagerException
     */
    @Override
    public String fetchSecrets(String secretsId) throws SecretsManagerException {

        try ()
        return null;
    }

    /**
     * @param secretsId
     * @param secrets
     * @return
     * @throws SecretsManagerException
     */
    @Override
    public boolean addSecrets(String secretsId, Map<String, String> secrets) throws SecretsManagerException {
        return false;
    }

    /**
     * @param secretsId
     * @return
     * @throws SecretsManagerException
     */
    @Override
    public boolean deleteSecrets(String secretsId) throws SecretsManagerException {
        return false;
    }
}
