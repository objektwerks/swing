package client.validator;

import client.resource.ResourceManager;
import domain.Repository;

import java.util.List;

public final class RepositoryValidator extends Validator {
    private String invalidNameErrorMessage;
    private String invalidContextErrorMessage;
    private int minNameLength;
    private int minContextLength;

    public RepositoryValidator() {
    }

    public boolean isValid(List <String> errors,
                           Repository repository) {
        int isValid = 0;
        String name = repository.getName();
        if (isNotValidName(name) || isNotMinLength(name, minNameLength)) {
            isValid++;
            errors.add(invalidNameErrorMessage);
        }
        String context = repository.getContext();
        if (isNotValidName(context) || isNotMinLength(context, minContextLength)) {
            isValid++;
            errors.add(invalidContextErrorMessage);
        }
        return (0 == isValid);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.invalidNameErrorMessage = resourceManager.getString("invalid.name");
        this.invalidContextErrorMessage = resourceManager.getString("invalid.context");
        this.minNameLength = resourceManager.getInt("validator.min.name.length");
        this.minContextLength = resourceManager.getInt("validator.min.context.length");
    }
}