package client.validator;

import client.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public final class DomainValidator extends Validator {
    private String invalidUrlErrorMessage;

    public DomainValidator() {
    }

    public boolean isValid(String url) {
        return isValid(new ArrayList <String> (), url);
    }

    public boolean isValid(List <String> errors,
                           String url) {
        int isValid = 0;
        if (isNotUrl(url)) {
            isValid++;
            errors.add(invalidUrlErrorMessage);
        }
        return (0 == isValid);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.invalidUrlErrorMessage = resourceManager.getString("invalid.url");
    }
}