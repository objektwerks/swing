package client.validator;

import client.resource.ResourceManager;
import domain.Index;

import java.util.List;

public final class IndexValidator extends Validator {
    private String invalidNameErrorMessage;
    private int minNameLength;

    public IndexValidator() {
    }

    public boolean isValid(List <String> errors,
                           Index index) {
        int isValid = 0;
        String name = index.getName();
        if (isNotValidName(name) || isNotMinLength(name, minNameLength)) {
            isValid++;
            errors.add(invalidNameErrorMessage);
        }
        return (0 == isValid);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.invalidNameErrorMessage = resourceManager.getString("invalid.name");
        this.minNameLength = resourceManager.getInt("validator.min.name.length");
    }
}