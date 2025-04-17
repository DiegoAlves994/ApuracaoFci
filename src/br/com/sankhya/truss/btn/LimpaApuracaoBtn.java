package br.com.sankhya.truss.btn;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidUpdateVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class LimpaApuracaoBtn implements AcaoRotinaJava{

	@Override
	public void doAction(ContextoAcao arg0) throws Exception {
		
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
		
	     
	     Registro[] linhas = arg0.getLinhas();
	   for (int i = 0; i < linhas.length; i++) {
		   Registro linha = linhas[i];
		   BigDecimal nunico =((BigDecimal)linha.getCampo("NUNICO"));
		   
		   String validaConfirmado = "SELECT COUNT(1) VALIDA"
		   						   + " FROM AD_FCICAB "
		   						   + " WHERE CONFIRMADO = 'S'"
		   						   + " AND NUNICO =  "+nunico;
		   ResultSet rsValidaConfirmado = sql.executeQuery(validaConfirmado);
		   while(rsValidaConfirmado.next()) {
			   
			   int valida = rsValidaConfirmado.getInt("VALIDA");
			   
			   if(valida>=1) {
				   
				   throw new Exception("Não é possível realizar a limpeza de apurações que já foram confirmadas.");
			   }
		   }
		   
		   
		   		String deleteComp = "DELETE FROM AD_FCICOMP WHERE NUNICO ="+nunico;
		   		String deleteEntrada = "DELETE FROM AD_FCIENTRADA WHERE NUNICO = "+nunico;
		   		String deleteEntradaIte = "DELETE FROM AD_FCIENTRADAITE WHERE NUNICO = "+nunico;
		   		String deleteSaidaIte = "DELETE FROM AD_FCISAIDAITE WHERE NUNICO = "+nunico;
		   		String deleteSaida = "DELETE FROM AD_FCISAIDA WHERE NUNICO = "+nunico;
		   		String deleteResumo = "DELETE FROM AD_FCIRESUMO WHERE NUNICO =  "+nunico;
		   		
		   		sql.executeUpdate(deleteComp);
		   		sql.executeUpdate(deleteEntrada);
		   		sql.executeUpdate(deleteEntradaIte);
		   		sql.executeUpdate(deleteSaidaIte);
		   		sql.executeUpdate(deleteSaida);
		   		sql.executeUpdate(deleteResumo);
		   		
		 	   JapeWrapper cabDAO = JapeFactory.dao("AD_FCICAB");
		 	  ((FluidUpdateVO)((FluidUpdateVO)((FluidUpdateVO)cabDAO.prepareToUpdateByPK(new Object[] { nunico }).set("APURADO", "N")).set("CODUSU", null)).set("DHAPURACAO", null)).update();
	   		}
	   arg0.setMensagemRetorno("Limpeza da apuração concluída com sucesso.");
	}
}
