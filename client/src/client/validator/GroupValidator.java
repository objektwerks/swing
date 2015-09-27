package client.validator;

import client.resource.ResourceManager;
import domain.Group;

import java.util.List;

public final class GroupValidator extends Validator {
    private String invalidNameErrorMessage;
    private int minNameLength;

    public GroupValidator() {
    }

    public boolean isValid(List <String> errors,
                           Group group) {
        int isValid = 0;
        String name = group.getName();
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