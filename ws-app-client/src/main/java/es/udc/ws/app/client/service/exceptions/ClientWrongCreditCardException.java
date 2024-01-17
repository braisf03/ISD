package es.udc.ws.app.client.service.exceptions;

public class ClientWrongCreditCardException extends Exception{
    private Long idCompra;

    private String creditCardNumber;

    public ClientWrongCreditCardException(long idCompra, String creditCardNumber){
        super("Sale with id \"" + idCompra + "\" isn't associated with a credit card with the number " + creditCardNumber);
        this.idCompra = idCompra;
        this.creditCardNumber = creditCardNumber;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setIdCompra(long idCompra) {
        this.idCompra = idCompra;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
