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
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import br.com.sankhya.truss.sql.CapturaEntrada;
import br.com.sankhya.truss.sql.CapturaSaida;
import br.com.sankhya.truss.sql.ComposicaoProduto;
import br.com.sankhya.truss.sql.Consolidacao;



public class ApuracaoBtn implements AcaoRotinaJava {

	JdbcWrapper JDBC = null;
    NativeSql sql = null;
		
	@Override
	public void doAction(ContextoAcao arg0) throws Exception {
		// TODO Auto-generated method stub
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
	     
	     BigDecimal codusu = arg0.getUsuarioLogado();
	     
	     Registro[] linhas = arg0.getLinhas();
	   for (int i = 0; i < linhas.length; i++) {
		   Registro linha = linhas[i];
		   BigDecimal nunico =((BigDecimal)linha.getCampo("NUNICO"));
		   
		   String geraApuracao = "SELECT COUNT(1) VALIDA FROM AD_FCICOMP WHERE NUNICO = "+nunico;
		   	 ResultSet rsGeraApuracao = sql.executeQuery(geraApuracao);
		   	 
		   	 while(rsGeraApuracao.next()) {
		   		 	
		   		 	int valida = rsGeraApuracao.getInt("VALIDA");
		   		 	
		   		 	if(valida>=1) {
		   		 		
		   		 		 throw new Exception("Não foi possível gerar a apuração.  Por favor, execute a limpeza dos dados e tente novamente.");
		   		 		 	
		   		 		}
		   		 
		   		 
		   	 		}
		   					   			   
		   	 ComposicaoProduto composicaoProduto = new ComposicaoProduto();
		     composicaoProduto.composicaoTerc(arg0, nunico);
		     composicaoProduto.composicaoProp(arg0, nunico);

		     CapturaEntrada capturaEntrada= new CapturaEntrada();
		     capturaEntrada.XmlEntradaIte(arg0, nunico);
		     capturaEntrada.CapturaTgfpapXml(arg0, nunico);
		     capturaEntrada.ApuraEntrada(arg0, nunico);
		     capturaEntrada.AjustaProdSemMov(arg0, nunico);
		     
		     CapturaSaida capturaSaida= new CapturaSaida();
		     capturaSaida.CapturaIte(arg0, nunico);
		     capturaSaida.ConsolidaSaida(arg0, nunico);
		     
		     Consolidacao consolidacao= new Consolidacao();
		     consolidacao.ResumoPA(arg0, nunico);	 
		     
				   
				   JapeWrapper cabDAO = JapeFactory.dao("AD_FCICAB");
				   cabDAO.prepareToUpdateByPK(new Object[] { nunico })
				    .set("APURADO", "S")
				    .set("DHAPURACAO", new java.sql.Timestamp(System.currentTimeMillis())) 
				    .set("CODUSU",codusu)
				    .update();
				   
				   arg0.setMensagemRetorno("Apuração gerada com sucesso!");
				   
	   			}
		
	   		
	  
			}

	   }
		
	
	
	

