package org.secrets.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.secrets.base.SecretsManager;
import org.secrets.exception.CredentialParsingException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class AwsSecretsManager implements SecretsManager {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    /**
     * @param secretsId - Secrets Identifier
     * @param credentials - Access credentials to access the secret
     * @return - Secret if identified, null if credential not found
     */
    @Override
    public String fetchSecrets(final String secretsId, final String credentials) {
        try {
            AwsCredentials awsCredentials = OBJECT_MAPPER.readValue(
                    credentials, AwsCredentials.class);
            try (SecretsManagerClient secretsManagerClient =
                    SecretsManagerClient.builder()
                            .region(Region.of(awsCredentials.getAwsRegion()))
                            .credentialsProvider(StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(awsCredentials.getAccessKey()
                                            , awsCredentials.getSecretAccessKey()))).build()) {
                GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
                        .secretId(secretsId).build();
                GetSecretValueResponse secretValueResponse = secretsManagerClient
                        .getSecretValue(secretValueRequest);
                return secretValueResponse.secretString();
            }
        } catch (JsonProcessingException e) {
            throw new CredentialParsingException(e);
        }
    }

    /**
     * @param secretsId
     * @param key
     * @param value
     */
    @Override
    public void addSecrets(String secretsId, String key, String value) {

    }

    /**
     * @param secretsId
     * @return
     */
    @Override
    public boolean deleteSecrets(String secretsId) {
        return false;
    }
}
