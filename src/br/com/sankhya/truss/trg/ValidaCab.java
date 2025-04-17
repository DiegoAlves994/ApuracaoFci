package br.com.sankhya.truss.trg;

import java.sql.ResultSet;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ValidaCab implements EventoProgramavelJava {

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
		ValidaApurado(arg0);
		ValidaConfirmado(arg0);
		
	}

	@Override
	public void beforeInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeUpdate(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		ValidaApurado(arg0);
		ValidaConfirmado(arg0);
	}

	private void ValidaApurado(PersistenceEvent arg0) throws Exception {
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
		 DynamicVO cabVO = (DynamicVO) arg0.getVo();
		 
		 String validaApurado = " SELECT COUNT(1) VALIDA"
		 					  + " FROM AD_FCICAB "
		 					  + " WHERE NUNICO = "+	cabVO.asInt("NUNICO")
		 					  + " AND APURADO = 'S'";
		 ResultSet rsValidaApurado = sql.executeQuery(validaApurado);
		 while(rsValidaApurado.next()) {
			 
			 int valida = rsValidaApurado.getInt("VALIDA");
			 if (valida>=1 && cabVO.asString("APURADO")=="S") {
				 
				 throw new Exception("Não é permitida a exclusão ou alteração de um registro APURADO. Caso seja necessário, realize a limpeza e tente novamente. ");
				 	
				 
			 }		
		 }
		 
		
			 
		
		 
	}
	
	private void ValidaConfirmado(PersistenceEvent arg0) throws Exception {
		
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
		 DynamicVO cabVO = (DynamicVO) arg0.getVo();
		 String validaConfirmado = " SELECT COUNT(1) VALIDA"
								  + " FROM AD_FCICAB "
								  + " WHERE NUNICO = "+	cabVO.asInt("NUNICO")
								  + " AND CONFIRMADO = 'S'";
		 ResultSet rsValidaConfirmado = sql.executeQuery(validaConfirmado);
		 while(rsValidaConfirmado.next()) {
			 int valida = rsValidaConfirmado.getInt("VALIDA");
			 if (valida>=1 && cabVO.asString("CONFIRMADO")=="S") {
				 
				 throw new Exception("Não é permitida a exclusão ou alteração de um registro CONFIRMADO. Caso seja necessário, realize a reversão e tente novamente. ");
				 
				 
			 }
			 
		 }			
		 
	}
	
}
