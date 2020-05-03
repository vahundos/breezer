import {getErrorMessagesForParam} from 'utils/errorMessageUtils'

describe('getErrorMessageForParam', () => {

    const paramName = 'param'
    let validatedForm

    beforeEach(() => {
        validatedForm = {
            param: {
                $error: true,
                $params: {
                    minLength: {},
                    maxLength: {}
                }
            }
        }
    });


    test('required validation fails', () => {
        validatedForm[paramName].required = false

        const errorMessages = getErrorMessagesForParam(validatedForm, paramName)
        expect(errorMessages).toHaveLength(1)
        expect(errorMessages[0]).toBe('Field is required')
    });

    test('min length validation fails', () => {
        validatedForm[paramName].minLength = false
        validatedForm[paramName].$params.minLength.min = 10

        const errorMessages = getErrorMessagesForParam(validatedForm, paramName)
        expect(errorMessages).toHaveLength(1)
        expect(errorMessages[0]).toBe('Min length is 10')
    });

    test('max length validation fails', () => {
        validatedForm[paramName].maxLength = false
        validatedForm[paramName].$params.maxLength.max = 10

        const errorMessages = getErrorMessagesForParam(validatedForm, paramName)
        expect(errorMessages).toHaveLength(1)
        expect(errorMessages[0]).toBe('Max length is 10')
    });

    test('email format validation fails', () => {
        validatedForm[paramName].email = false

        const errorMessages = getErrorMessagesForParam(validatedForm, paramName)
        expect(errorMessages).toHaveLength(1)
        expect(errorMessages[0]).toBe('Email is not-well formed')
    });

    test('passwords equality validation fails', () => {
        validatedForm[paramName].sameAsPassword = false

        const errorMessages = getErrorMessagesForParam(validatedForm, paramName)
        expect(errorMessages).toHaveLength(1)
        expect(errorMessages[0]).toBe('Passwords are different')
    });

    test('unknow parameter', () => {
        const otherParam = 'otherParam';
        validatedForm[otherParam] = {}

        const errorMessages = getErrorMessagesForParam(validatedForm, otherParam)
        expect(errorMessages).toHaveLength(0)
    });
});