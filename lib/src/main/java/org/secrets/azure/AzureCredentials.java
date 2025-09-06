package org.secrets.azure;

public class AzureCredentials {

    private String vaultUrl;

    private String managedIdentityClientId;

    public String getVaultUrl() {
        return vaultUrl;
    }

    public void setVaultUrl(String vaultUrl) {
        this.vaultUrl = vaultUrl;
    }

    public String getManagedIdentityClientId() {
        return managedIdentityClientId;
    }

    public void setManagedIdentityClientId(String managedIdentityClientId) {
        this.managedIdentityClientId = managedIdentityClientId;
    }
}
