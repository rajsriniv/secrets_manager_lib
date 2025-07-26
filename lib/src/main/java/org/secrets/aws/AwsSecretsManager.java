package org.secrets.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.secrets.base.SecretsManager;
import org.secrets.exception.SecretsManagerException;
import org.secrets.exception.CredentialParsingException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.util.Map;
import java.util.Objects;

public class AwsSecretsManager implements SecretsManager {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    private final AwsCredentials awsCredentials;

    public AwsSecretsManager(final String credentials) {
        Objects.requireNonNull(credentials);
        try {
            this.awsCredentials = OBJECT_MAPPER.readValue(
                    credentials, AwsCredentials.class
            );
        } catch (JsonProcessingException e) {
            throw new CredentialParsingException(e);
        }
    }

    private SecretsManagerClient buildSecretsManagerClient(final AwsCredentials awsCredentials) {
        return SecretsManagerClient.builder()
                        .region(Region.of(awsCredentials.getAwsRegion()))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(awsCredentials.getAccessKey()
                                        , awsCredentials.getSecretAccessKey()))).build();
    }

    /**
     * @param secretsId - Secrets Identifier
     * @return - Secret if identified, null if credential not found
     */
    @Override
    public String fetchSecrets(final String secretsId)
            throws SecretsManagerException {
        try (SecretsManagerClient client = buildSecretsManagerClient(awsCredentials)) {
            GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretsId).build();
            GetSecretValueResponse secretValueResponse = client
                    .getSecretValue(secretValueRequest);
            return secretValueResponse.secretString();

        } catch (Exception e) {
            final String message = "Error while reading secrets";
            throw new SecretsManagerException(message, e);
        }
    }

    /**
     * @param secretsId - ID of the secrets in Secrets Manager
     * @param secrets - Key-Value pair to be added to Secrets Id
     * @return true if successful, false otherwise
     */
    @Override
    public boolean addSecrets(String secretsId, Map<String, String> secrets)
            throws SecretsManagerException {
        try (SecretsManagerClient client = buildSecretsManagerClient(awsCredentials)) {
            CreateSecretRequest request = CreateSecretRequest.builder().name(secretsId)
                    .secretString(OBJECT_MAPPER.writeValueAsString(secrets)).build();
            CreateSecretResponse response = client.createSecret(request);
            return response != null && response.name().equals(secretsId);
        } catch (Exception e) {
            final String message = "Error adding secrets";
            throw new SecretsManagerException(message, e);
        }
    }

    /**
     * @param secretsId
     * @return
     */
    @Override
    public boolean deleteSecrets(String secretsId) throws SecretsManagerException {
        try (SecretsManagerClient client = buildSecretsManagerClient(awsCredentials)) {
            DeleteSecretRequest request = DeleteSecretRequest.builder()
                    .secretId(secretsId).build();
            DeleteSecretResponse response = client.deleteSecret(request);
            return response != null && response.name().equals(secretsId);
        } catch (Exception e) {
            final String message = "Error while deleting secrets";
            throw new SecretsManagerException(message, e);
        }
    }
}
