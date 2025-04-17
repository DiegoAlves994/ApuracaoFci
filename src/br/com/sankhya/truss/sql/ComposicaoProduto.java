package br.com.sankhya.truss.sql;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ComposicaoProduto {
	
	
public void composicaoTerc(ContextoAcao ctx, BigDecimal nunico)throws Exception {
		
		
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sqlTerc = new NativeSql(JDBC);
	     
	     sqlTerc.loadSql(getClass(), "ComposicaoTerceiros.sql");
	     sqlTerc.setNamedParameter("P_NUNICO", nunico);
	     ResultSet rsComposicaoTerceiros = sqlTerc.executeQuery();
			
	     BigDecimal seqTerc = BigDecimal.ZERO;
	        
				
			try {
			while(rsComposicaoTerceiros.next()) {
				
				seqTerc = seqTerc.add(BigDecimal.ONE);

		         
	            BigDecimal seqMp = rsComposicaoTerceiros.getBigDecimal("SEQCOMP");
	            BigDecimal codProdPa = rsComposicaoTerceiros.getBigDecimal("CODPRODPA");
	            BigDecimal codProdMp = rsComposicaoTerceiros.getBigDecimal("CODPRODMP");
	            BigDecimal codPrc = rsComposicaoTerceiros.getBigDecimal("CODPRC");
	            String descrAbrev = rsComposicaoTerceiros.getString("DESCRABREV");
	            String codvol = rsComposicaoTerceiros.getString("CODVOL");
	            BigDecimal idefx = rsComposicaoTerceiros.getBigDecimal("IDEFX");
	            BigDecimal versao = rsComposicaoTerceiros.getBigDecimal("VERSAO");
	            BigDecimal qtdmistura = rsComposicaoTerceiros.getBigDecimal("QTDMISTURA");

	            
	            
	            JapeWrapper insertTerc = JapeFactory.dao("AD_FCICOMP");
	           ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertTerc.create()
	    	      .set("NUNICO", nunico))
	    	      .set("SEQ",  seqMp.add(seqTerc)))
	    	      .set("CODPRODPA", codProdPa))
	    	      .set("CODPRODMP",codProdMp))
	    	      .set("CODPRC", codPrc))
	    	      .set("DESCRABREV", descrAbrev))
	    	      .set("IDEFX", idefx))
	    	      .set("VERSAO", versao))
	    	      .set("QTDMISTURA", qtdmistura))
	              .set("CODVOL", codvol))
	    	      .save();
	    		
	   		
				}
			}	
			
			finally {
				rsComposicaoTerceiros.close();
				NativeSql.releaseResources(sqlTerc);
				JDBC.closeSession();
			}
			
} 
			

				    

public void composicaoProp(ContextoAcao ctx, BigDecimal nunico) throws Exception {
    JdbcWrapper JDBCP = null;
    NativeSql sqlProp = null;
    ResultSet rsComposicaoPropria = null;
   

    try {
        JDBCP = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        sqlProp = new NativeSql(JDBCP);

      
        sqlProp.loadSql(getClass(), "ComposicaoPropria.sql");
        sqlProp.setNamedParameter("P_NUNICO", nunico);
        rsComposicaoPropria = sqlProp.executeQuery();

        BigDecimal seqProp = BigDecimal.ZERO;
        

        
        while (rsComposicaoPropria.next()) {
        	
        	seqProp = seqProp.add(BigDecimal.ONE);

         
            BigDecimal seqMp = rsComposicaoPropria.getBigDecimal("SEQCOMP");
            BigDecimal codProdPa = rsComposicaoPropria.getBigDecimal("CODPRODPA");
            BigDecimal codProdMp = rsComposicaoPropria.getBigDecimal("CODPRODMP");
            BigDecimal codPrc = rsComposicaoPropria.getBigDecimal("CODPRC");
            String descrAbrev = rsComposicaoPropria.getString("DESCRABREV");
            String codvol = rsComposicaoPropria.getString("CODVOL");
            BigDecimal idefx = rsComposicaoPropria.getBigDecimal("IDEFX");
            BigDecimal versao = rsComposicaoPropria.getBigDecimal("VERSAO");
            BigDecimal qtdmistura = rsComposicaoPropria.getBigDecimal("QTDMISTURA");
            
            
            JapeWrapper insertProp = JapeFactory.dao("AD_FCICOMP");
            ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertProp.create()
    	      .set("NUNICO", nunico))
    	      .set("SEQ",  seqMp.add(seqProp)))
    	      .set("CODPRODPA", codProdPa))
    	      .set("CODPRODMP",codProdMp))
    	      .set("CODPRC", codPrc))
    	      .set("DESCRABREV", descrAbrev))
    	      .set("IDEFX", idefx))
    	      .set("VERSAO", versao))
    	      .set("QTDMISTURA", qtdmistura))
              .set("CODVOL", codvol))	
    	     .save();
    		
   
      
        }
    } finally {
       
    	rsComposicaoPropria.close();
      	NativeSql.releaseResources(sqlProp);
        JDBCP.closeSession();
        }
    }
}

		    	 


	     



