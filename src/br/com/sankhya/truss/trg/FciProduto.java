package br.com.sankhya.truss.trg;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Arrays;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class FciProduto implements EventoProgramavelJava {

	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterUpdate(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeCommit(TransactionContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeUpdate(PersistenceEvent arg0) throws Exception {
	
		insereFci(arg0);
		validaFci(arg0);
		
		
	}
	
	
	
	
	private void insereFci(PersistenceEvent arg0) throws Exception{
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
		DynamicVO proVo = (DynamicVO) arg0.getVo();
		
		String newOrigprod = proVo.asString("ORIGPROD");
		String newCodfci = proVo.asString("CODFCI");
		BigDecimal codprod = proVo.asBigDecimal("CODPROD");
        
		
        if(  newCodfci != null && Arrays.asList("3", "5", "8").contains(newOrigprod) && !newCodfci.equals("PENDENTE")) {
        	
        
            String validaFci = "SELECT COUNT(1) AS VALIDA FROM AD_FCIPRO WHERE CODPROD = :CODPROD AND ORIGPROD = :ORIGPROD";

            sql.setNamedParameter("CODPROD", codprod);   
            sql.setNamedParameter("ORIGPROD", newOrigprod);
        	
        	ResultSet rsValidaFci = sql.executeQuery(validaFci);
        	while(rsValidaFci.next()) {
        		
        		int valida = rsValidaFci.getInt("VALIDA");
        		
        		if (valida==0) {
        			
        			JapeWrapper insertFci = JapeFactory.dao("AD_FCIPRO");
        		    ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertFci.create()
        		    .set("CODPROD", codprod))
        		    .set("ORIGPROD", newOrigprod))
        		    .set("CODFCI", newCodfci))
        		    .save();
        				
        		
        			
        		}

        		
        	}
        	
        }
		
		
	}

	
	private void validaFci (PersistenceEvent arg0)throws Exception {
		
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
	     DynamicVO proVo = (DynamicVO) arg0.getVo();
	     
	     String newOrig = proVo.asString("ORIGPROD");
         String oldOrig = (String) arg0.getModifingFields().getOldValue("ORIGPROD");
         
         String newCodfci = proVo.asString("CODFCI");
         String  oldCodfci = (String) arg0.getModifingFields().getOldValue("CODFCI");
	     
         int codprod = proVo.asInt("CODPROD");
	     
		 
	     if (( newCodfci != null && !newOrig.equals(oldOrig) && Arrays.asList("3", "5", "8").contains(newOrig) ) 
	    		 || ( newCodfci != null && !newCodfci.equals(oldCodfci) )) {
	    	 
	    	     	 
	    	 String validaFci = "SELECT CODFCI FROM AD_FCIPRO WHERE CODPROD = "+codprod+" AND ORIGPROD = "+newOrig;
	    	 ResultSet rsValidaFci = sql.executeQuery(validaFci);
	    	 
	    	 while(rsValidaFci.next()) {
	    		 
	    		 String codfci = rsValidaFci.getString("CODFCI");
	    		 
	            proVo.setProperty("CODFCI", codfci);
	    		 
	    	 }
	    	 
	    	 
	    	 
	     }
	     
	
	     
		
	}
	
	
}
