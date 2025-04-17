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
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class CapturaEntrada  {

	public void XmlEntradaIte (ContextoAcao ctx, BigDecimal nunico )throws Exception {
		
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sqlTerc = new NativeSql(JDBC);
	     
	     sqlTerc.loadSql(getClass(), "EntradaIte.sql");
	     sqlTerc.setNamedParameter("P_NUNICO", nunico);
	     ResultSet rsEntradaIte = sqlTerc.executeQuery();
	     
	     BigDecimal seq = BigDecimal.ZERO;
	     while(rsEntradaIte.next()) {
	    	 	
	    	 seq = seq.add(BigDecimal.ONE);
	    	 BigDecimal nunota = rsEntradaIte.getBigDecimal("NUNOTA");
	    	 BigDecimal numnota= rsEntradaIte.getBigDecimal("NUMNOTA");
	    	 String origprod = rsEntradaIte.getString("ORIGPROD");
	    	 BigDecimal codparc = rsEntradaIte.getBigDecimal("CODPARC");
	    	 BigDecimal qtdneg = rsEntradaIte.getBigDecimal("QTDNEG");
	    	 BigDecimal codprod = rsEntradaIte.getBigDecimal("CODPROD");
	    	 Date dtentsai = rsEntradaIte.getDate("DTENTSAI");
	    	 BigDecimal vlrunit = rsEntradaIte.getBigDecimal("VLRUNIT");
	    	 BigDecimal vlrtot = rsEntradaIte.getBigDecimal("VLRTOT");
	    	 BigDecimal sequencia = rsEntradaIte.getBigDecimal("SEQUENCIA");
	    	 BigDecimal codtipoper = rsEntradaIte.getBigDecimal("CODTIPOPER");
	    	 BigDecimal vlricms = rsEntradaIte.getBigDecimal("VLRICMS");
	    	 String controle = rsEntradaIte.getString("CONTROLE");
	    	 String codvol = rsEntradaIte.getString("CODVOL");
	    	  
	            JapeWrapper insertIte = JapeFactory.dao("AD_FCIENTRADAITE");
	            ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertIte.create()
	    	      .set("NUNICO", nunico))
	    	      .set("SEQ",  seq))
	    	      .set("NUNOTA", nunota))
	    	      .set("NUMNOTA",numnota))
	    	      .set("ORIGPROD", origprod))
	    	      .set("CODPARC", codparc))
	    	      .set("QTDNEG", qtdneg))
	    	      .set("CODPROD", codprod))
	    	      .set("DTENTSAI", dtentsai))
	    	      .set("VLRUNIT", vlrunit))
				  .set("VLRTOT", vlrtot))
				  .set("SEQUENCIA", sequencia))		
	              .set("CODTIPOPER", codtipoper))	
	              .set("VLRICMS", vlricms))			
	              .set("CODVOL", codvol))	
	              .set("CONTROLE", controle)
   	    	      .save();
	    		
	    	 
	     }
		
		
	}
	
	
	public void CapturaTgfpapXml (ContextoAcao ctx, BigDecimal nunico)throws Exception{
		
		 JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		    NativeSql sql = new NativeSql(jdbc);  
		    
		    String xmlTgfpap = " SELECT * FROM (SELECT " +
		    		" ITE.*, " +
		    		" to_char((SELECT " +
		    		"    REGEXP_SUBSTR( " +
		    		"        T.XML, " +
		    		"         '<cProd>'||PAP.CODPROPARC||'</cProd>.*?<orig>([^<]+)</orig>',   " +
		    		"        1, " +
		    		"        1, " +
		    		"        NULL, " +
		    		"        1 " +
		    		"    ) AS ORIGEM " +
		    		" FROM TGFIXN T " +
		    		" WHERE REGEXP_LIKE( " +
		    		"    T.XML, " +
		    		"    '<cProd>'||PAP.CODPROPARC||'</cProd>'                            " +
		    		" ) AND T.NUNOTA = ITE.NUNOTA " +
		    		" )) ORIGPRODXML, " +
		    		" PAP.CODPROPARC " +
		    		" FROM AD_FCIENTRADAITE ITE " +
		    		" LEFT JOIN TGFPAP PAP ON PAP.CODPROD = ITE.CODPROD " +
		    		"                     AND PAP.CODPARC = ITE.CODPARC " +
		    		"                     AND PAP.SEQUENCIA = (SELECT MAX(SEQUENCIA) FROM TGFPAP " +
		    		"                                            WHERE TGFPAP.CODPROPARC = PAP.CODPROPARC " +
		    		"                                            AND TGFPAP.CODPROD = PAP.CODPROD " +
		    		"                                            AND TGFPAP.CODPARC = PAP.CODPARC  ) " +
		    		" WHERE NUNICO =  " +nunico+
		    		" AND ORIGPROD IS NULL) WHERE ORIGPRODXML IS NOT NULL " ;
		    ResultSet rsXmlTgfpap = sql.executeQuery(xmlTgfpap);
		    while(rsXmlTgfpap.next()) {
		    	
		    	
		    	BigDecimal seqIte = rsXmlTgfpap.getBigDecimal("SEQ");
		    	String origProdXml = rsXmlTgfpap.getString("ORIGPRODXML");
		    	
		    	String updateIte = "UPDATE AD_FCIENTRADAITE SET ORIGPROD = "+origProdXml+" WHERE NUNICO = "+nunico+ " AND SEQ = "+seqIte; 
		    	sql.executeUpdate(updateIte);
		    }

		
		
	}
	
	
	public void ApuraEntrada (ContextoAcao ctx, BigDecimal nunico)throws Exception{
		
		  JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		    NativeSql sql = new NativeSql(jdbc);  
		
		String total = "SELECT " +
	             "    QTDNEG, " +
	             "    CODPROD, " +
	             "    VLRTOT, "+
	             "    VLRICMS, " +
	             "    ORIGPROD, " +
	             "    ORIGPRODNOTAS, " +
	             "    DENSE_RANK() OVER (PARTITION BY CODPROD ORDER BY QTDNEG DESC, ORIGPROD ASC) AS CLASSIFICACAO, " +
	             "    ((VLRTOTTOTAL-VLRICMSTOTAL)/QTDNEGTOT) VLRMEDIO, "+
	             "    ROUND((QTDNEG * 100) / QTDNEGTOT, 2) AS PERCORIGPROD " +
	             "FROM ( " +
	             "    SELECT " +
	             "        SUM(ITE.QTDNEG) AS QTDNEG, " +
	             "        SUM(ITE.VLRTOT) AS VLRTOT, "+
	             "        SUM(ITE.VLRICMS) AS VLRICMS, "+
	             "        ITE.CODPROD, " +
	             "        ITE.ORIGPROD AS ORIGPRODNOTAS, " +
	             "        PRO.ORIGPROD, " +
	             "        ( " +
	             "            SELECT SUM(QTDNEG) " +
	             "            FROM AD_FCIENTRADAITE " +
	             "            WHERE CODPROD = ITE.CODPROD AND NUNICO = ITE.NUNICO " +
	             "        ) AS QTDNEGTOT, " +
	            " ( " +
	    	             "            SELECT SUM(VLRTOT) " +
	    	             "            FROM AD_FCIENTRADAITE " +
	    	             "            WHERE CODPROD = ITE.CODPROD AND NUNICO = ITE.NUNICO " +
	    	             "        ) AS VLRTOTTOTAL, " +
	    	             " ( " +
	    	             "            SELECT SUM(VLRICMS) " +
	    	             "            FROM AD_FCIENTRADAITE " +
	    	             "            WHERE CODPROD = ITE.CODPROD AND NUNICO = ITE.NUNICO " +
	    	             "        ) AS VLRICMSTOTAL " +
	             "    FROM " +
	             "        AD_FCIENTRADAITE ITE " +
	             "        LEFT JOIN TGFPRO PRO ON PRO.CODPROD = ITE.CODPROD " +
	             "    WHERE " +
	             "        ITE.NUNICO = " +nunico+
	             "    GROUP BY " +
	             "        ITE.CODPROD, ITE.ORIGPROD, PRO.ORIGPROD, ITE.NUNICO " +
	             ") ";
		ResultSet rsTotal = sql.executeQuery(total);
		
		 BigDecimal seq = BigDecimal.ZERO;
		 
		while(rsTotal.next()) {
			
			seq = seq.add(BigDecimal.ONE);
			  
			 BigDecimal qtdneg = rsTotal.getBigDecimal("QTDNEG");
			 BigDecimal codprod = rsTotal.getBigDecimal("CODPROD");
			 String origprod = rsTotal.getString("ORIGPROD");
			 String origprodnotas = rsTotal.getString("ORIGPRODNOTAS");
			 BigDecimal classificacao = rsTotal.getBigDecimal("CLASSIFICACAO");
			 BigDecimal percorigprod = rsTotal.getBigDecimal("PERCORIGPROD");
			 BigDecimal vlrtot= rsTotal.getBigDecimal("VLRTOT");
			 BigDecimal vlricms= rsTotal.getBigDecimal("VLRICMS");
			 BigDecimal vlrmedio= rsTotal.getBigDecimal("VLRMEDIO");
			    JapeWrapper insertTot = JapeFactory.dao("AD_FCIENTRADA");
			    ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertTot.create()
	    	      .set("NUNICO", nunico))
	    	      .set("SEQ",  seq))
	    	      .set("QTDNEG", qtdneg))
	    	      .set("CODPROD",codprod))
	    	      .set("ORIGPROD", origprod))
	    	      .set("ORIGPRODNOTAS", origprodnotas))
	    	      .set("CLASSIFICACAO", classificacao))
	    	      .set("PERCORIGPROD", percorigprod))
			      .set("VLRTOT", vlrtot))
			      .set("VLRICMS", vlricms))
			      .set("VLRMEDIO", vlrmedio))
	    	      .save();
	            
	            if (classificacao.equals(BigDecimal.ONE)) {
	                
	            	String updateComp = "UPDATE AD_FCICOMP SET ORIGEMMPREF = "+origprodnotas+", VLRMEDIO = "+vlrmedio+", VLRMEDIOAPLI = round(QTDMISTURA*"+vlrmedio+",8) WHERE NUNICO = "+nunico+" and codprodmp = "+codprod;
	              	sql.executeUpdate(updateComp);
	            }

			
		}
		
	}
	
	
	public void AjustaProdSemMov (ContextoAcao ctx, BigDecimal nunico)throws Exception{
		
		  JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		    NativeSql sql = new NativeSql(jdbc);  
		    
		    String compSemOrig = "UPDATE AD_FCICOMP SET ORIGEMMPREF = (SELECT ORIGPROD FROM TGFPRO WHERE CODPROD = AD_FCICOMP.CODPRODMP), " 
		    										 +" VLRMEDIO = NVL((SELECT CUSSEMICM FROM TGFCUS"
		    										    			  +" WHERE CODEMP = 6 "
		    										    			  +" AND CODPROD = AD_FCICOMP.CODPRODMP "
		    										    			  +" AND DTATUAL = (SELECT MAX(DTATUAL) FROM TGFCUS " 
		    										    			  			 	 + " WHERE CODPROD = AD_FCICOMP.CODPRODMP "
		    										    			  			 	 +" AND CODEMP = 6 "
		    										    			  			 	 +" AND DTATUAL <= (SELECT ADD_MONTHS(REFERENCIA,-2) "
		    										    			  			 	 +" FROM AD_FCICAB WHERE NUNICO = "+nunico+" ))),0),  "
		                                            
		                                              +" VLRMEDIOAPLI = nvl((QTDMISTURA*(SELECT CUSSEMICM FROM TGFCUS "
		                                                                   				+" WHERE CODEMP = 6 "
		                                                                   				+" AND CODPROD = AD_FCICOMP.CODPRODMP "
		                                                                   				+" AND DTATUAL = (SELECT MAX(DTATUAL) FROM TGFCUS "
		                                                                   								+" WHERE CODPROD = AD_FCICOMP.CODPRODMP "
		                                                                   								+"  AND CODEMP = 6 "
		                                                                   								+"  AND DTATUAL <= (SELECT ADD_MONTHS(REFERENCIA,-2) " 
		                                                                                                +" FROM AD_FCICAB WHERE NUNICO = "+nunico+" )))),0) "
		                                            +" WHERE NUNICO =  " +nunico
					                                +" AND ORIGEMMPREF IS NULL ";
						    
		    

		    sql.executeUpdate(compSemOrig);
		
		    
	}
	
	
}
