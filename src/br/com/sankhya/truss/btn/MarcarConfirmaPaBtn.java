package br.com.sankhya.truss.btn;

import java.math.BigDecimal;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class MarcarConfirmaPaBtn implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao arg0) throws Exception {
		
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
		 NativeSql sql = new NativeSql(JDBC);

		
		Registro[] linhas = arg0.getLinhas();
		for (int i = 0; i < linhas.length; i++) {
				Registro linha = linhas[i];
					
				BigDecimal nunico =((BigDecimal)linha.getCampo("NUNICO"));
			
			String updateResumo = "UPDATE AD_FCIRESUMO SET CONFIRMA = 'S' WHERE NUNICO = "+nunico;
			
			sql.executeUpdate(updateResumo);
			arg0.setMensagemRetorno("Produtos Confirmados Para Atualização!");
			
		}
	}

}
