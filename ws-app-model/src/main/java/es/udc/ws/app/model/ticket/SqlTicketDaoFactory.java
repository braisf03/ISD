package es.udc.ws.app.model.ticket;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.module.Configuration;

public class SqlTicketDaoFactory {

    private final static  String CLASS_NAME_PARAMETER = "SqlTicketDaoFactory.className";

    private static SqlTicketDao dao = null;

    private SqlTicketDaoFactory(){
    }

    private static SqlTicketDao getInstance(){
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlTicketDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlTicketDao getDao(){
        if(dao==null){
            dao = getInstance();
        }
        return dao;
    }
}
