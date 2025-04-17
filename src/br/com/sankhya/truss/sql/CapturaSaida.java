package br.com.sankhya.truss.sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;

public class CapturaSaida {
	
	
	public void CapturaIte (ContextoAcao ctx, BigDecimal nunico) throws Exception {
		
		  JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		    NativeSql sql = new NativeSql(jdbc);  
		    
		    String saidaIte = "SELECT CAB.DTENTSAI, " +
	                 "       CAB.NUNOTA, " +
	                 "       CAB.CODPARC, " +
	                 "       CAB.NUMNOTA, " +
	                 "       CAB.CODEMP, " +
	                 "       CAB.DTENTSAI, "+
	                 "       CAB.CODTIPOPER, " +
	                 "       TOP.DESCROPER, " +
	                 "       ITE.CODPROD, " +
	                 "       ITE.CONTROLE, " +
	                 "       ITE.QTDNEG, " +
	                 "       ITE.SEQUENCIA, " +
	                 "       ITE.VLRUNIT, " +
	                 "       ITE.VLRTOT " +
	                 "FROM TGFCAB CAB " +
	                 "LEFT JOIN TGFTOP TOP ON TOP.CODTIPOPER = CAB.CODTIPOPER " +
	                 "                     AND TOP.DHALTER = CAB.DHTIPOPER " +
	                 "LEFT JOIN TGFITE ITE ON ITE.NUNOTA = CAB.NUNOTA " +
	                 "WHERE TOP.TIPMOV = 'V' " +
	                 "AND TOP.ATUALFIN = 1 " +
	                 "AND TOP.ATUALEST = 'B' " +
	                 "AND CAB.DTENTSAI >= (SELECT ADD_MONTHS(REFERENCIA, -2) FROM AD_FCICAB WHERE NUNICO = "+nunico+" ) " +
	                 "AND CAB.DTENTSAI <= (SELECT LAST_DAY(ADD_MONTHS(REFERENCIA, -2)) FROM AD_FCICAB WHERE NUNICO = "+nunico+" ) " +
	                 "AND ITE.CODPROD IN (SELECT CODPRODPA FROM AD_FCICOMP WHERE NUNICO = "+nunico+" )";
		    	
		    		ResultSet rsSsaidaIte = sql.executeQuery(saidaIte);
		    		
		    		BigDecimal seq = BigDecimal.ZERO;
		    		
		    		while(rsSsaidaIte.next()) {
		    			
		    			 seq = seq.add(BigDecimal.ONE);
		    	    	 BigDecimal nunota = rsSsaidaIte.getBigDecimal("NUNOTA");
		    	    	 BigDecimal numnota= rsSsaidaIte.getBigDecimal("NUMNOTA");
		    	    	 BigDecimal codparc = rsSsaidaIte.getBigDecimal("CODPARC");
		    	    	 BigDecimal qtdneg = rsSsaidaIte.getBigDecimal("QTDNEG");
		    	    	 BigDecimal codprod = rsSsaidaIte.getBigDecimal("CODPROD");
		    	    	 Date dtentsai = rsSsaidaIte.getDate("DTENTSAI");
		    	    	 BigDecimal vlrunit = rsSsaidaIte.getBigDecimal("VLRUNIT");
		    	    	 BigDecimal vlrtot = rsSsaidaIte.getBigDecimal("VLRTOT");
		    	    	 BigDecimal sequencia = rsSsaidaIte.getBigDecimal("SEQUENCIA");
		    	    	 BigDecimal codtipoper = rsSsaidaIte.getBigDecimal("CODTIPOPER");
		    	    	 

		 	            JapeWrapper insertIte = JapeFactory.dao("AD_FCISAIDAITE");
		 	           ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertIte.create()
		 	    	      .set("NUNICO", nunico))
		 	    	      .set("SEQ",  seq))
		 	    	      .set("NUNOTA", nunota))
		 	    	      .set("NUMNOTA",numnota))
		 	    	      .set("CODPARC", codparc))
		 	    	      .set("QTDNEG", qtdneg))
		 	    	      .set("CODPROD", codprod))
		 	    	      .set("DTENTSAI", dtentsai))
		 	    	      .set("VLRUNIT", vlrunit))
		 				  .set("VLRTOT", vlrtot))
		 				  .set("SEQUENCIA", sequencia))		
		 	              .set("CODTIPOPER", codtipoper))	
		 	              .save();
		 	    		
		    			  
		    		}
		    		
	
	}
	
	
	public void ConsolidaSaida (ContextoAcao ctx, BigDecimal nunico) throws Exception{
		
		  JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		    NativeSql sql = new NativeSql(jdbc);  
		
	    String consolidaSaida = " SELECT CODPROD, "
	    		+ " SUM(VLRTOT) VLRTOT, "
	    		+ " SUM(QTDNEG) QTDNEG, "
	    		+ " ROUND(SUM(VLRTOT)/SUM(QTDNEG),4)VLRMEDIO "
	    		+ " FROM AD_FCISAIDAITE WHERE NUNICO = "+nunico
	    		+ " GROUP BY CODPROD";
	    				
	    ResultSet rsConsolidaSaida = sql.executeQuery(consolidaSaida);
	    
	    BigDecimal seqConsolida = BigDecimal.ZERO;
	    
	    while(rsConsolidaSaida.next()) {
	    	
	    	seqConsolida = seqConsolida.add(BigDecimal.ONE);
	    	
	    	 BigDecimal codprod = rsConsolidaSaida.getBigDecimal("CODPROD");
	    	 BigDecimal vlrtot= rsConsolidaSaida.getBigDecimal("VLRTOT");
	    	 BigDecimal qtdneg = rsConsolidaSaida.getBigDecimal("QTDNEG");
	    	 BigDecimal vlrmedio = rsConsolidaSaida.getBigDecimal("VLRMEDIO");
	    	 
	    	JapeWrapper insertConsolida = JapeFactory.dao("AD_FCISAIDA");
	    	((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertConsolida.create()
	    		 .set("NUNICO", nunico))
 	    	     .set("SEQ",  seqConsolida))
	    		 .set("CODPROD",  codprod))
	    		 .set("VLRTOT",  vlrtot))
	    		 .set("QTDNEG",  qtdneg))
	    		 .set("VLRMEDIO",  vlrmedio))
	    		 .save();
	    }
		
	}
	

}
