package es.udc.ws.app.model.appservice.validation;

import es.udc.ws.util.exceptions.InputValidationException;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class CustomPropertyValidator {

    public static void validateLong(String propertyName, long value, int lowerValidLimit)
    throws InputValidationException{
        if(value < lowerValidLimit){
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit
                    + "): " + value);
        }
    }

    public static void validateLong(String propertyName,
                                    long value, int lowerValidLimit, int upperValidLimit)
            throws InputValidationException {

        if ( (value < lowerValidLimit) || (value > upperValidLimit) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " + value);
        }

    }

    public static void validateDouble(String propertyName,
                                      double doubleValue, double lowerValidLimit, double upperValidLimit)
            throws InputValidationException {

        if ((doubleValue < lowerValidLimit) ||
                (doubleValue > upperValidLimit)) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be gtrater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " +
                    doubleValue);
        }

    }

    public static void validateMandatoryString(String propertyName,
                                               String stringValue) throws InputValidationException {

        if ( (stringValue == null) || (stringValue.trim().length() == 0) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it cannot be null neither empty): " +
                    stringValue);
        }

    }

    public static void validateCreditCard(String propertyValue)
            throws InputValidationException {

        boolean validCreditCard = true;
        if ( (propertyValue != null) && (propertyValue.length() == 16) ) {
            try {
                new BigInteger(propertyValue);
            } catch (NumberFormatException e) {
                validCreditCard = false;
            }
        } else {
            validCreditCard = false;
        }
        if (!validCreditCard) {
            throw new InputValidationException("Invalid credit card number" +
                    " (it should be a sequence of 16 numeric digits): " +
                    propertyValue);
        }

    }

    public static void validateDates(LocalDateTime creationDate, LocalDateTime playDate) throws InputValidationException {
        boolean validDates = true;
        if(creationDate != null && playDate != null){
            if(creationDate.isAfter(playDate)){
                validDates = false;
            }
        }
        else{
            validDates = false;
        }
        if(!validDates){
            throw  new InputValidationException("Invalid match play date (it must be before today: " +
                    creationDate + "): " + playDate);
        }
    }
}
