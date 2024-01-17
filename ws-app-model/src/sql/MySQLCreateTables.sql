-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Ticket;
DROP TABLE MatchT;


-------------------------------------------------------------------------------
-- Partidos
-- Para dar de alta un partido, se indicará el nombre del equipo visitante, la fecha y hora de
-- celebración, el precio de las entradas (será un precio único para todas las localidades) y el
-- número máximo de entradas disponibles a la venta. Además, se guardará la fecha y hora en
-- las que se ha dado de alta.
-------------------------------------------------------------------------------

    CREATE TABLE MatchT (
                              idPartido BIGINT NOT NULL AUTO_INCREMENT,
                              fechaCreacion DATETIME NOT NULL ,
                              fechaJugar DATETIME  NOT NULL,
                              equipoVisitante VARCHAR(50) NOT NULL ,
                              precioEntrada FLOAT NOT NULL ,
                              numeroAsientos INT NOT NULL,
                              entradasVendidas INT NOT NULL,
                              CONSTRAINT MatchPK PRIMARY KEY(idPartido)
    ) ENGINE = InnoDB;

    CREATE TABLE Ticket (
                             idCompra BIGINT NOT NULL AUTO_INCREMENT,
                             correoUsuario VARCHAR(255) NOT NULL ,
                             numTarjetaBancaria VARCHAR(16) NOT NULL ,
                             fechaHoraCompra DATETIME NOT NULL ,
                             idPartido BIGINT NOT NULL ,
                             numEntradas INT NOT NULL,
                             recogidas INT NOT NULL,
                             CONSTRAINT TicketPK PRIMARY KEY(idCompra),
                             CONSTRAINT partido FOREIGN KEY (idPartido) REFERENCES MatchT(idPartido)
    ) ENGINE = InnoDB;
