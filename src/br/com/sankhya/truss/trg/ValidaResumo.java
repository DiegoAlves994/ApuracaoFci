package br.com.sankhya.truss.trg;
import java.sql.ResultSet;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;


public class ValidaResumo implements EventoProgramavelJava{

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
		validaConfirmacao(arg0);
	}

	@Override
	public void beforeUpdate(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		validaConfirmacao(arg0);
	}

	private void validaConfirmacao (PersistenceEvent arg0) throws Exception {
		
		

        JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
        NativeSql sql = new NativeSql(jdbc);
        DynamicVO cabVO = (DynamicVO) arg0.getVo();
        
        
       
        String confirmado = "SELECT COUNT(1) VALIDA "
        				  + " FROM AD_FCICAB "
        				  + " WHERE NUNICO = "+cabVO.asBigDecimal("NUNICO")	
        				  + " AND CONFIRMADO = 'S'";
        ResultSet rsConfirmado = sql.executeQuery(confirmado);
        
       while(rsConfirmado.next()) {
    	   int valida = rsConfirmado.getInt("VALIDA");
    	   
		if(valida==1) {
			
			throw new Exception ("Não é possível alterar informações do resumo após a confirmação da apuração. "
								+ "	Caso seja necessário, limpe a apuração e tente novamente.");
			
		}
      
	
      }    
	
		
	}
	
}
