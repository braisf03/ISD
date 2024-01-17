package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientMatchService;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientTicketDto;
import es.udc.ws.app.client.service.exceptions.ClientAdquiredAfterException;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyPickedException;
import es.udc.ws.app.client.service.exceptions.ClientSalesLimitReachedException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientMatchService implements ClientMatchService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientMatchService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);


    @Override
    public Long addMatch(ClientMatchDto match) throws InputValidationException {

        ThriftAppService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.addMatch(ClientMatchDtoToThriftMatchDtoConversor.toThriftMatchDto(match)).getIdPartido();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientMatchDto> findByDates(LocalDate endDate) {

        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientMatchDtoToThriftMatchDtoConversor.toClientMatchDtos(client.findByDates(endDate.toString()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<ClientTicketDto> findByUser(String correoUsuario) {

        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientTicketDtoToThriftTicketDtoConversor.toClientTicketDtos(client.findByUser(correoUsuario));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void giveTickets(Long idTickets, String numeroTarjetaBancaria) throws InputValidationException,
            InstanceNotFoundException, ClientAlreadyPickedException, ClientWrongCreditCardException {
        ThriftAppService.Client client = getClient();
        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            client.getTickets(idTickets, numeroTarjetaBancaria);
        }
        catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftAlreadyPickedException e) {
            throw new ClientAlreadyPickedException(e.getIdCompra());
        } catch (ThriftWrongCreditCardException e) {
            throw new ClientWrongCreditCardException(e.getIdCompra(), e.getNumTarjetaBancaria());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientMatchDto findMatch(Long idPartido) throws InstanceNotFoundException{

        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientMatchDtoToThriftMatchDtoConversor.toClientMatchDto(client.findById(idPartido));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long buyTicket(Long matchId, String correoUsuario, String numTarjetaCredito, int HowMany) throws InstanceNotFoundException,
            ClientAdquiredAfterException, ClientSalesLimitReachedException {
        ThriftAppService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.buyTicket(matchId, correoUsuario, numTarjetaCredito, HowMany).getIdTicket();

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftAdquiredAfterException e){
            throw new ClientAdquiredAfterException(e.getIdPartido());
        }catch (ThriftSalesLimitReachedException e) {
            throw new ClientSalesLimitReachedException(e.getIdPartido());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ThriftAppService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftAppService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}
