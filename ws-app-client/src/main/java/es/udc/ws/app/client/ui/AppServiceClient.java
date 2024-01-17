package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientMatchService;
import es.udc.ws.app.client.service.ClientMatchServiceFactory;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientTicketDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyPickedException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {
        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientMatchService clientMatchService = ClientMatchServiceFactory.getService();
//////////////////////////////////////////////////////////////////////////////////////
        if("-add".equalsIgnoreCase(args[0])) {

            // [add] MovieServiceClient -a <equipoVisitante> <fechaJugar> <precioEntrada> <numeroAsientos>

            validateArgs(args, 5, new int[] {3, 4});

            try {
                String visitante = args[1].trim();
                LocalDateTime fechaJugar = LocalDateTime.parse(args[2]);
                float presio = Float.parseFloat(args[3]);
                int asientos = Integer.parseInt(args[4]);
                Long matchId = clientMatchService.addMatch(new ClientMatchDto(null,
                        visitante, fechaJugar, presio, asientos, 0));

                System.out.println("Match " + matchId + " created sucessfully");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
//////////////////////////////////////////////////////////////////////////////////////
        } else if("-fd".equalsIgnoreCase(args[0])) {

            // [find] MatchServiceClient -fbd <endDate>

            validateArgs(args, 2, new int[] {});

            try {
                LocalDate endDate = LocalDate.parse(args[1]);
                List<ClientMatchDto> matches = clientMatchService.findByDates(endDate);
                System.out.println("Found " + matches.size() +
                        " matches from today to '" + args[1] + "'");
                for (int i = 0; i < matches.size(); i++) {
                    ClientMatchDto matchDto = matches.get(i);
                    System.out.println("Id: " + matchDto.getMatchId() +
                            ", EquipoVisitante: " + matchDto.getEquipoVisitante() +
                            ", fechaJugar: " + (matchDto.getFechaJugar()) +
                            ", precioEntrada: " + matchDto.getPrecioEntrada() +
                            ", numeroAsientos: " + matchDto.getNumeroAsientos() +
                            ", entradasRestantes: " + matchDto.getEntradasRestantes());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
//////////////////////////////////////////////////////////////////////////////////////
        }
        else if("-fm".equalsIgnoreCase(args[0])) {

            // [get] MovieServiceClient -g <saleId> <creditCardNumber>

            validateArgs(args, 2, new int[] {1});

            try {
                ClientMatchDto matchDto =
                        clientMatchService.findMatch(Long.parseLong(args[1]));

                System.out.println("Id: " + matchDto.getMatchId() +
                        ", EquipoVisitante: " + matchDto.getEquipoVisitante() +
                        ", fechaJugar: " + (matchDto.getFechaJugar()) +
                        ", precioEntrada: " + matchDto.getPrecioEntrada() +
                        ", numeroAsientos: " + matchDto.getNumeroAsientos() +
                        ", entradasRestantes: " + matchDto.getEntradasRestantes());

            } catch (NumberFormatException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
//////////////////////////////////////////////////////////////////////////////////////
        }
        else if("-buy".equalsIgnoreCase(args[0])) {

            // [buy] MovieServiceClient -b <movieId> <userId> <creditCardNumber> <HowMany>

            validateArgs(args, 5, new int[] {1, 4});

            Long saleId;
            try {
                saleId = clientMatchService.buyTicket(Long.parseLong(args[1]),
                        args[2], args[3], Integer.parseInt(args[4]));

                System.out.println(args[4] + " tickets for match " + args[1] +
                        " purchased sucessfully with sale identifier " +
                        saleId);

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
//////////////////////////////////////////////////////////////////////////////////////
        }else if("-fu".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [findByUser] TicketServiceClient -fbd <endDate>

            try {
                String correoUsuario = args[1];
                List<ClientTicketDto> tickets = clientMatchService.findByUser(correoUsuario);
                System.out.println("Found " + tickets.size() +
                        " tickets from today to '" + args[1] + "'");
                for (int i = 0; i < tickets.size(); i++) {
                    ClientTicketDto ticketDto = tickets.get(i);
                    System.out.println("Id: " + ticketDto.getIdCompra() +
                            ", Id Partido: " + ticketDto.getIdPartido() +
                            ", Correo del Usuario: " + ticketDto.getCorreoUsuario() +
                            ", Numero Tarjeta Bancaria acaba en: " + (ticketDto.getNumTarjetaBancaria()) +
                            ", FechaHoraCompra: " + ticketDto.getFechaHoraCompra() +
                            ", numeroEntradas: " + ticketDto.getNumEntradas() +
                            ", Están Recogidas?: " + ticketDto.getrecogidas());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
//////////////////////////////////////////////////////////////////////////////////////
        else if("-get".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[]{1});

            // [get] MovieServiceClient -g <saleId> <creditCardNumber>

            try {

                clientMatchService.giveTickets(Long.parseLong(args[1]), args[2]);

                System.out.println("Tickets for the sale " + args[1] + " have been successfully picked");
            } catch (NumberFormatException | InstanceNotFoundException | ClientAlreadyPickedException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
//////////////////////////////////////////////////////////////////////////////////////
    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add] -add <equipoVisitante> <fechaPartidoTHoraPartido> <precioEntrada> <numAsientos>\n" +
                "    [findMatch] -fm <idPartido>\n" +
                "    [findByDates] -fd <fechaHasta>\n" +
                "    [buyTicket] -buy <idPartido> <correoUsuario> <numTarjeta> <CuantosTickets>\n" +
                "    [findByUser] -fu <correoUsuario>\n" +
                "    [getTicket] -get <idCompra> <numTarjeta>\n");
    }
}