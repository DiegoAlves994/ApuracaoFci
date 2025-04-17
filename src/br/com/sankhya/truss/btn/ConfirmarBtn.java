package br.com.sankhya.truss.btn;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public  class ConfirmarBtn implements AcaoRotinaJava {

	
	public void doAction(ContextoAcao ctx) throws Exception {
			
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
	     
	     BigDecimal codusu = ctx.getUsuarioLogado();
	     
		
	     Registro[] linhas = ctx.getLinhas();
		   for (int i = 0; i < linhas.length; i++) {
			   Registro linha = linhas[i];
			   
			   BigDecimal nunico =((BigDecimal)linha.getCampo("NUNICO"));
			   
			   String validaConfirma = "SELECT COUNT(1) VALIDA "
			   						+ " FROM AD_FCICAB		"
			   						+"  WHERE CONFIRMADO = 'S'"
			   						+ "	AND NUNICO =  "+ nunico ;
			   ResultSet rsValidaConfirma = sql.executeQuery(validaConfirma);
			   while(rsValidaConfirma.next()) {
				   
				   int valida = rsValidaConfirma.getInt("VALIDA");
				   if(valida>=1) {
					   
					   throw new Exception ("Não é possível confirmar a apuração número: "+nunico+". O registro já se encontra confirmado.");
				   }
				   
			   }
			   
			   
			   String validaResumo = "SELECT COUNT(1) VALIDARESUMO "
  						+ " FROM AD_FCIRESUMO		"
  						+"  WHERE CONFIRMA = 'S'"
  						+ "	AND NUNICO =  "+ nunico ;
			   
		  ResultSet rsvalidaResumo = sql.executeQuery(validaResumo);
		  while(rsvalidaResumo.next()) {
			   
			   int validaRes = rsvalidaResumo.getInt("VALIDARESUMO");
			   if(validaRes==0) {
				   
				   throw new Exception ("Não é possível confirmar a apuração número: "+nunico+". Na aba Resumo, não existem itens selecionados para confirmação.");
			   }
			   
		  }
			  
			   
		   String RegistraLog = 
				    "    SELECT " +
				    "    ORIGPROD, " +
				    "    ORIGPRODATUAL," +
				    "    VLRCOMERC,"+
				    "    OLDVLRCOMERC,"+
				    "    VLRPARCIMP,"+
				    "    VLRPARCIMPEXT,"+
				    "    CODPRODPA, " +
				    "    (SELECT USERNAME FROM V$SESSION " +
				    "        WHERE AUDSID = (SELECT USERENV('SESSIONID') FROM DUAL) " +
				    "        AND ROWNUM = 1) USUBANCO, " +
				    "    (SELECT OSUSER FROM V$SESSION " +
				    "        WHERE AUDSID = (SELECT USERENV('SESSIONID') FROM DUAL) " +
				    "        AND ROWNUM = 1) USUREDE, " +
				    "    (SELECT MACHINE FROM V$SESSION " +
				    "        WHERE AUDSID = (SELECT USERENV('SESSIONID') FROM DUAL) " +
				    "        AND ROWNUM = 1) NOMMAQUINA, " +
				    "    SYSDATE DHACAO, " +
				    "    sys_context('USERENV','IP_ADDRESS') IPMAQUINA, " +
				    "    (SELECT PROGRAM FROM V$SESSION " +
				    "        WHERE AUDSID = (SELECT USERENV('SESSIONID') FROM DUAL) " +
				    "        AND ROWNUM = 1) PROGRAMA, " +
				    "    'PK[NUNICO=' || CODPRODPA || ']' CHAVE, " +
				    "    ORIGPROD NOVO, " +
				    "    ORIGPRODATUAL VELHO " +
				    "FROM " +
				    "    AD_FCIRESUMO " +
				    "WHERE " +
				    "    CONFIRMA = 'S' " +
				    "    AND NUNICO =  "+nunico;

		   
		   
		   ResultSet rsRegistraLog = sql.executeQuery(RegistraLog);
		   while(rsRegistraLog.next()) {
			   
			  
			   String origprodatual = rsRegistraLog.getString("ORIGPRODATUAL");
			   String origprod = rsRegistraLog.getString("ORIGPROD");
			   BigDecimal vlrcomerc = rsRegistraLog.getBigDecimal("VLRCOMERC");
			   BigDecimal oldvlrcomerc = rsRegistraLog.getBigDecimal("OLDVLRCOMERC");
			   BigDecimal vlrparcimp = rsRegistraLog.getBigDecimal("VLRPARCIMP");
			   BigDecimal vlrparcimpext = rsRegistraLog.getBigDecimal("VLRPARCIMPEXT");
			   String usubanco = rsRegistraLog.getString("USUBANCO");
			   String usurede  = rsRegistraLog.getString("USUREDE");
			   String nommaquina = rsRegistraLog.getString("NOMMAQUINA");
			   String ipmaquina = rsRegistraLog.getString("IPMAQUINA");
			   Date dhacao =  rsRegistraLog.getDate("DHACAO");
			   String programa =  rsRegistraLog.getString("PROGRAMA");
			   String chave =  rsRegistraLog.getString("CHAVE");
			   		   
			  
			   String insertLogOrig = "INSERT INTO TSILGT (NOMETAB, DHACAO, ACAO, USUBANCO, USUREDE, NOMMAQUINA, IPMAQUINA, PROGRAMA, USUARIOSIS, CHAVE, CAMPO, NOVO, VELHO)  "
					    + "VALUES ('TGFPRO', TO_DATE('" + dhacao + "', 'YYYY-MM-DD HH24:MI:SS'), 'UPDATE', '" + usubanco + "', '" + usurede + "', '" + nommaquina + "', '" + ipmaquina + "', '" + programa + "', " + codusu + ", '" + chave + "', 'ORIGPROD', '" + origprod + "', '" + origprodatual + "')";
			 
			   String insertLogVlrCom = "INSERT INTO TSILGT (NOMETAB, DHACAO, ACAO, USUBANCO, USUREDE, NOMMAQUINA, IPMAQUINA, PROGRAMA, USUARIOSIS, CHAVE, CAMPO, NOVO, VELHO)  "
					    + "VALUES ('TGFPRO', TO_DATE('" + dhacao + "', 'YYYY-MM-DD HH24:MI:SS'), 'UPDATE', '" + usubanco + "', '" + usurede + "', '" + nommaquina + "', '" + ipmaquina + "', '" + programa + "', " + codusu + ", '" + chave + "', 'VLRCOMERC', '" + vlrcomerc + "', '" + oldvlrcomerc + "')";
			   
			   String insertLogVlrImp = "INSERT INTO TSILGT (NOMETAB, DHACAO, ACAO, USUBANCO, USUREDE, NOMMAQUINA, IPMAQUINA, PROGRAMA, USUARIOSIS, CHAVE, CAMPO, NOVO, VELHO)  "
					    + "VALUES ('TGFPRO', TO_DATE('" + dhacao + "', 'YYYY-MM-DD HH24:MI:SS'), 'UPDATE', '" + usubanco + "', '" + usurede + "', '" + nommaquina + "', '" + ipmaquina + "', '" + programa + "', " + codusu + ", '" + chave + "', 'VLRPARCIMPEXT', '" + vlrparcimp + "', '" + vlrparcimpext + "')";
			 
			   
			   
			   
			   sql.executeUpdate(insertLogOrig);
			   sql.executeUpdate(insertLogVlrCom);
			   sql.executeUpdate(insertLogVlrImp);
			   
			   				   
		   }
		   
		   		String atualizaOrigemMp =" UPDATE TGFPRO SET ORIGPROD = (SELECT DISTINCT ORIGPRODNOTAS FROM AD_FCIENTRADA WHERE CODPROD = TGFPRO.CODPROD AND CLASSIFICACAO = 1 AND NUNICO = "+nunico+" ) "
		   							    + " WHERE CODPROD IN (SELECT DISTINCT CODPROD FROM AD_FCIENTRADA WHERE NUNICO = "+nunico+" ) "; 
			   			   

			   String atualizaProduto = "UPDATE TGFPRO SET ORIGPROD = (SELECT ORIGPROD FROM AD_FCIRESUMO WHERE CODPRODPA = TGFPRO.CODPROD AND NUNICO = "+nunico+"), "+
					   					" VLRCOMERC = ROUND((SELECT VLRCOMERC FROM AD_FCIRESUMO WHERE CODPRODPA = TGFPRO.CODPROD AND NUNICO = "+nunico+"),2), "+	
					   					" VLRPARCIMPEXT = ROUND((SELECT VLRPARCIMP FROM AD_FCIRESUMO WHERE CODPRODPA = TGFPRO.CODPROD AND NUNICO = "+nunico+"),2) "+	
								 		" WHERE CODPROD IN (SELECT CODPRODPA FROM AD_FCIRESUMO WHERE NUNICO =" +nunico+" AND CONFIRMA = 'S' AND VLRCOMERC> 0) ";
			   
			   
			   String atualizaCab = "UPDATE AD_FCICAB SET CONFIRMADO = 'S', CODUSUCONFIRMA = "+codusu+", DHCONFIRMADO = SYSDATE WHERE NUNICO = "+nunico;
			   
			   sql.executeUpdate(atualizaOrigemMp);
			   sql.executeUpdate(atualizaProduto);
			   sql.executeUpdate(atualizaCab);	
			   
			   
			   
		   }ctx.setMensagemRetorno("Dados da FCI confirmados com sucesso, produtos atualizados!");
	}   

}
