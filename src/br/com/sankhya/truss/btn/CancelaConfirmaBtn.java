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

public class CancelaConfirmaBtn implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		
		BigDecimal codusu = ctx.getUsuarioLogado();
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
	     
	     
	     
		
	     Registro[] linhas = ctx.getLinhas();
		   for (int i = 0; i < linhas.length; i++) {
			   	Registro linha = linhas[i];
			   
			   BigDecimal nunico =((BigDecimal)linha.getCampo("NUNICO"));
			   String validaConfirma = " SELECT COUNT(1) VALIDA "
					   				+ " FROM AD_FCICAB		"
					   				+"  WHERE CONFIRMADO = 'S' "
					   				+ "	AND NUNICO =  "+ nunico ;
			   
			   ResultSet rsValidaConfirma = sql.executeQuery(validaConfirma);
			   
			   while(rsValidaConfirma.next()) {
				   int valida = rsValidaConfirma.getInt("VALIDA");
				   if(valida==0) {
					   
					   throw new Exception("A apuração de número " + nunico + " não pode ser revertida porque ainda não foi confirmada.");

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
				   String usubanco = rsRegistraLog.getString("USUBANCO");
				   String usurede  = rsRegistraLog.getString("USUREDE");
				   String nommaquina = rsRegistraLog.getString("NOMMAQUINA");
				   String ipmaquina = rsRegistraLog.getString("IPMAQUINA");
				   Date dhacao =  rsRegistraLog.getDate("DHACAO");
				   String programa =  rsRegistraLog.getString("PROGRAMA");
				   String chave =  rsRegistraLog.getString("CHAVE");
				   BigDecimal vlrcomerc = rsRegistraLog.getBigDecimal("VLRCOMERC");
				   BigDecimal oldvlrcomerc = rsRegistraLog.getBigDecimal("OLDVLRCOMERC");
				   BigDecimal vlrparcimp = rsRegistraLog.getBigDecimal("VLRPARCIMP");
				   BigDecimal vlrparcimpext = rsRegistraLog.getBigDecimal("VLRPARCIMPEXT");
				   		   
				   System.out.println("reverte fci 3");
				   String insertLog = "INSERT INTO TSILGT (NOMETAB, DHACAO, ACAO, USUBANCO, USUREDE, NOMMAQUINA, IPMAQUINA, PROGRAMA, USUARIOSIS, CHAVE, CAMPO, NOVO, VELHO)  "
						    + "VALUES ('TGFPRO', TO_DATE('" + dhacao + "', 'YYYY-MM-DD HH24:MI:SS'), 'UPDATE', '" + usubanco + "', '" + usurede + "', '" + nommaquina + "', '" + ipmaquina + "', '" + programa + "', " + codusu + ", '" + chave + "', 'ORIGPROD', '" + origprodatual + "', '" + origprod + "')";
				   String insertLogVlrCom = "INSERT INTO TSILGT (NOMETAB, DHACAO, ACAO, USUBANCO, USUREDE, NOMMAQUINA, IPMAQUINA, PROGRAMA, USUARIOSIS, CHAVE, CAMPO, NOVO, VELHO)  "
						    + "VALUES ('TGFPRO', TO_DATE('" + dhacao + "', 'YYYY-MM-DD HH24:MI:SS'), 'UPDATE', '" + usubanco + "', '" + usurede + "', '" + nommaquina + "', '" + ipmaquina + "', '" + programa + "', " + codusu + ", '" + chave + "', 'VLRCOMERC', '" + oldvlrcomerc + "', '" + vlrcomerc + "')";
				   
				   String insertLogVlrImp = "INSERT INTO TSILGT (NOMETAB, DHACAO, ACAO, USUBANCO, USUREDE, NOMMAQUINA, IPMAQUINA, PROGRAMA, USUARIOSIS, CHAVE, CAMPO, NOVO, VELHO)  "
						    + "VALUES ('TGFPRO', TO_DATE('" + dhacao + "', 'YYYY-MM-DD HH24:MI:SS'), 'UPDATE', '" + usubanco + "', '" + usurede + "', '" + nommaquina + "', '" + ipmaquina + "', '" + programa + "', " + codusu + ", '" + chave + "', 'VLRPARCIMPEXT', '" + vlrparcimpext + "', '" + vlrparcimp + "')";
				 
				   
				   sql.executeUpdate(insertLog);
				  
				   sql.executeUpdate(insertLogVlrCom);
				
				   sql.executeUpdate(insertLogVlrImp);
				   
				   				   
			   }
			   
		
			   String atualizaOrigemMp =" UPDATE TGFPRO SET ORIGPROD = (SELECT DISTINCT ORIGPROD FROM AD_FCIENTRADA WHERE CODPROD = TGFPRO.CODPROD AND CLASSIFICACAO = 1 AND NUNICO = "+nunico+" ) "
						    + " WHERE CODPROD IN (SELECT DISTINCT CODPROD FROM AD_FCIENTRADA WHERE NUNICO = "+nunico+" ) "; 
			 
			   

			 
			   String atualizaProduto = "UPDATE TGFPRO SET ORIGPROD = (SELECT ORIGPRODATUAL FROM AD_FCIRESUMO WHERE CODPRODPA = TGFPRO.CODPROD AND NUNICO = "+nunico+"), "
				   +" VLRCOMERC = ROUND((SELECT OLDVLRCOMERC FROM AD_FCIRESUMO WHERE CODPRODPA = TGFPRO.CODPROD AND NUNICO = "+nunico+"),2), "
	   				   +" VLRPARCIMPEXT = ROUND((SELECT VLRPARCIMPEXT FROM AD_FCIRESUMO WHERE CODPRODPA = TGFPRO.CODPROD AND NUNICO = "+nunico+"),2) "
					   +" WHERE CODPROD IN (SELECT CODPRODPA FROM AD_FCIRESUMO WHERE NUNICO =" +nunico+" AND CONFIRMA = 'S' AND VLRCOMERC > 0 AND VLRPARCIMP > 0) ";
			   
			   
//			   String atualprd = "SELECT VLRCOMERC, VLRPARCIMP, ORIGPROD, CODPRODPA, OLDVLRCOMERC, VLRPARCIMPEXT FROM AD_FCIRESUMO WHERE NUNICO = "+nunico;
//			   ResultSet rsAtualPrd = sql.executeQuery(atualprd);
//			   while(rsAtualPrd.next()) {
//				   
//				   BigDecimal vlrcomerc = rsAtualPrd.getBigDecimal("VLRCOMERC");
//				   BigDecimal vlrparcimp = rsAtualPrd.getBigDecimal("VLRPARCIMP");
//				   String origprod =  rsAtualPrd.getString("ORIGPROD");
//				   BigDecimal codprodpa = rsAtualPrd.getBigDecimal("CODPRODPA");
//				   BigDecimal oldvlrcomerc = rsAtualPrd.getBigDecimal("OLDVLRCOMERC");
//				   BigDecimal vlrparcimpext = rsAtualPrd.getBigDecimal("VLRPARCIMPEXT");
//				   
//				   if (vlrparcimpext.compareTo(BigDecimal.ZERO) > 0 && oldvlrcomerc.compareTo(BigDecimal.ZERO) > 0) {
//					   
//					}
//
//				   
//			   }
//			    
//			   
			
			   String atualizaCab = "UPDATE AD_FCICAB SET CONFIRMADO = 'N', CODUSUCONFIRMA = NULL, DHCONFIRMADO = NULL WHERE NUNICO = "+nunico;
			 
			   sql.executeUpdate(atualizaOrigemMp);
			  
			   sql.executeUpdate(atualizaProduto);
			  
			   sql.executeUpdate(atualizaCab);	
			
			   
		   }ctx.setMensagemRetorno("Dados da FCI estornados, produtos voltaram para STATUS Original!");
		
	}

}
