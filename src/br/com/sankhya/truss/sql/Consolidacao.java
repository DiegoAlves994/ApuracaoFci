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

public class Consolidacao  {

	public void ResumoPA (ContextoAcao ctx, BigDecimal nunico) throws Exception {
	
		JdbcWrapper JDBC = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
	     NativeSql sql = new NativeSql(JDBC);
	     
	     sql.loadSql(getClass(), "ResumoPA.sql");
	     sql.setNamedParameter("P_NUNICO", nunico);
	     ResultSet rsResumoPa = sql.executeQuery();
	     
	     BigDecimal seq = BigDecimal.ZERO;
	     while(rsResumoPa.next()) {
	    	 
	    	 seq = seq.add(BigDecimal.ONE);
	    	 BigDecimal codprodpa = rsResumoPa.getBigDecimal("CODPRODPA");
	    	 BigDecimal vlrcomerc = rsResumoPa.getBigDecimal("VLRCOMERC");
	    	 BigDecimal vlrparcimp = rsResumoPa.getBigDecimal("VLRPARCIMP");
	    	 String origprodatual = rsResumoPa.getString("ORIGPRODATUAL");
	    	 BigDecimal percimportado = rsResumoPa.getBigDecimal("PERCIMPORTADO");
	    	 String origprod = rsResumoPa.getString("ORIGPROD");
	    	 BigDecimal vlrparcimpext = rsResumoPa.getBigDecimal("VLRPARCIMPEXT");
	    	 BigDecimal oldvlrcomerc = rsResumoPa.getBigDecimal("OLDVLRCOMERC");
	    	 
	    	    JapeWrapper insertResumo = JapeFactory.dao("AD_FCIRESUMO");
	    	    ((FluidCreateVO) ((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)((FluidCreateVO)insertResumo.create()
		        	.set("NUNICO", nunico))
			    	.set("SEQ",  seq))
		        	.set("CODPRODPA",  codprodpa))
		        	.set("VLRCOMERC",  vlrcomerc))
		        	.set("VLRPARCIMP",  vlrparcimp))
		        	.set("ORIGPRODATUAL",  origprodatual))
		        	.set("PERCIMPORTADO",  percimportado))
		        	.set("ORIGPROD",  origprod))
		            .set("VLRPARCIMPEXT",  vlrparcimpext))
   					.set("OLDVLRCOMERC",  oldvlrcomerc))
		        	.save();
	     }
		
	}
	

}
