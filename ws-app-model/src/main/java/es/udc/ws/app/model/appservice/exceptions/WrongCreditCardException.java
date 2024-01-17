package es.udc.ws.app.model.appservice.exceptions;

public class WrongCreditCardException extends Exception{

    private Long idCompra;

    private String creditCardNumber;

    public WrongCreditCardException(long idCompra, String creditCardNumber){
        super("Sale with id " + idCompra + " isn't associated with a credit card with the number " + creditCardNumber);
        this.idCompra = idCompra;
        this.creditCardNumber = creditCardNumber;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
