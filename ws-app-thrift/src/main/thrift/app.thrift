namespace java es.udc.ws.app.thrift

struct ThriftMatchDto {
    1: i64 idPartido
    2: string equipoVisitante
    3: string fechaJugar
    4: double precioEntrada
    5: i32 numeroAsientos
    6: i32 entradasVendidas
}

struct ThriftTicketDto {
    1: i64 idTicket
    2: i64 idPartido
    3: string correoUsuario
    4: string fechaHoraCompra
    5: string numTarjetaBancaria
    6: i32 numEntradas
    7: i32 recogidas
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftAdquiredAfterException {
    1: i64 idPartido
}

exception ThriftAlreadyPickedException {
    1: i64 idCompra
}

exception ThriftSalesLimitReachedException {
    1: i64 idPartido
}

exception ThriftWrongCreditCardException {
    1: i64 idCompra
    2: string numTarjetaBancaria
}

service ThriftAppService {

   ThriftMatchDto addMatch(1: ThriftMatchDto matchDto) throws (1: ThriftInputValidationException e)

   list<ThriftMatchDto> findByDates(1: string endDate)

   ThriftMatchDto findById(1: i64 matchId) throws (1: ThriftInstanceNotFoundException e)

   ThriftTicketDto buyTicket(1: i64 idPartido, 2: string correoUsuario, 3: string numTarjetaBancaria, 4: i32 numEntradas) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ie, 3: ThriftAdquiredAfterException ae, 4: ThriftSalesLimitReachedException se)

   list<ThriftTicketDto> findByUser(1: string correoUsuario)

   void getTickets(1: i64 idCompra, 2: string numTarjetaBancaria) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ie, 3: ThriftAlreadyPickedException ae, 4: ThriftWrongCreditCardException we)
}