export function getErrorMessagesForParam(validatedForm, paramName) {
    const errorMessages = [];
    if (validatedForm[paramName].$error) {
        if (validatedForm[paramName].required != null && !validatedForm[paramName].required) {
            errorMessages.push('Field is required');
        }

        if (validatedForm[paramName].minLength != null && !validatedForm[paramName].minLength) {
            errorMessages.push('Min length is ' + validatedForm[paramName].$params.minLength.min);
        }

        if (validatedForm[paramName].maxLength != null && !validatedForm[paramName].maxLength) {
            errorMessages.push('Max length is ' + validatedForm[paramName].$params.maxLength.max);
        }
    }
    return errorMessages;
}