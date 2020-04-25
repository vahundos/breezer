export function getErrorMessagesForParam(validatedForm, paramName) {
    const errorMessages = [];
    if (validatedForm[paramName].$error) {
        if (isParamValidationFailed(validatedForm[paramName].required)) {
            errorMessages.push('Field is required');
        } else if (isParamValidationFailed(validatedForm[paramName].minLength)) {
            errorMessages.push('Min length is ' + validatedForm[paramName].$params.minLength.min);
        } else if (isParamValidationFailed(validatedForm[paramName].maxLength)) {
            errorMessages.push('Max length is ' + validatedForm[paramName].$params.maxLength.max);
        } else if (isParamValidationFailed(validatedForm[paramName].email)) {
            errorMessages.push('Email is not-well formed');
        } else if (isParamValidationFailed(validatedForm[paramName].sameAsPassword)) {
            errorMessages.push("Passwords are different");
        } else {
            console.warn(paramName + ' is not handled')
        }
    }
    return errorMessages;
}

function isParamValidationFailed(paramValidationSuccess) {
    return paramValidationSuccess != null && !paramValidationSuccess;
}