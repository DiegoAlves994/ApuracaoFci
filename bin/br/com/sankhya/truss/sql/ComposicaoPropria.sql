SELECT
*
 FROM 
(SELECT CASE WHEN TAB.IDPROC IS NOT NULL THEN TAB.IDPROC ELSE PRC.IDPROC END IDPROC, 
        CASE WHEN TAB.VERSAO IS NOT NULL THEN TAB.VERSAO ELSE PRC.VERSAO END VERSAO,
        CASE WHEN TAB.CODPRC IS NOT NULL THEN TAB.CODPRC ELSE PRC.CODPRC END CODPRC,
        CASE WHEN TAB.DESCRABREV IS NOT NULL THEN TAB.DESCRABREV ELSE PRC.DESCRABREV END DESCRABREV , 
        CASE WHEN TAB.DHALTER IS NOT NULL THEN TAB.DHALTER ELSE PRC.DHALTER END DHALTER, 
        CASE WHEN TAB.IDEFX IS NOT NULL  THEN TAB.IDEFX ELSE ATV.IDEFX END IDEFX, 
        LMP.CODPRODPA,
        CASE WHEN TAB.CODPRODMP IS NOT NULL THEN TAB.CODPRODMP ELSE LMP.CODPRODMP END CODPRODMP,   
        CASE WHEN TAB.CODPRODMP IS NOT NULL THEN TAB.CODVOL ELSE LMP.CODVOL END CODVOL,
        CASE WHEN TAB.CODPRODMP IS NOT NULL THEN TAB.USOPROD ELSE PROMP.USOPROD END USOPROD,
        CASE WHEN TAB.CODPRODMP IS NOT NULL THEN TAB.ATIVO ELSE PROPA.ATIVO END ATIVO,
         (SELECT SUM(LMP1.QTDMISTURA) QTDMISTURA
        FROM TPRLMP LMP1 
        LEFT JOIN TPRATV ATV1 ON ATV1.IDEFX = LMP1.IDEFX 
        LEFT JOIN TGFPRO PRO1 ON PRO1.CODPROD = LMP1.CODPRODMP    
        WHERE LMP1.CODPRODPA = LMP.CODPRODPA AND LMP1.IDEFX = LMP.IDEFX
        AND PRO1.USOPROD != 'P' ) TOTAL_APONTADO_PA,
        CASE WHEN TAB.CODPRODMP IS NOT NULL THEN TAB.QTDMISTURA*(SELECT SUM(QTDMISTURA) 
                                                                FROM TPRLMP M 
                                                                LEFT JOIN TGFPRO P ON P.CODPROD = M.CODPRODMP 
                                                                WHERE IDEFX = ATV.IDEFX 
                                                                AND CODPRODPA = LMP.CODPRODPA
                                                                AND P.USOPROD = 'P' )
        ELSE LMP.QTDMISTURA END QTDMISTURA,
        
 (SELECT SUM(LMP1.QTDMISTURA) QTDMISTURA
        FROM TPRLMP LMP1 
        LEFT JOIN TPRATV ATV1 ON ATV1.IDEFX = LMP1.IDEFX 
        LEFT JOIN TGFPRO PRO1 ON PRO1.CODPROD = LMP1.CODPRODMP    
        WHERE LMP1.CODPRODPA = LMP.CODPRODPA AND LMP1.IDEFX = LMP.IDEFX
        AND PRO1.USOPROD != 'P' )+(SELECT SUM(QTDMISTURA) 
                                                                FROM TPRLMP M 
                                                                LEFT JOIN TGFPRO P ON P.CODPROD = M.CODPRODMP 
                                                                WHERE IDEFX = ATV.IDEFX 
                                                                AND CODPRODPA = LMP.CODPRODPA
                                                                AND P.USOPROD = 'P' )QTDMISTURA_TOTAL,
   (SELECT nvl(MAX(SEQ),0) FROM AD_FCICOMP WHERE NUNICO = :P_NUNICO) SEQCOMP,
            CASE WHEN TAB.CODPRODMP IS NOT NULL THEN ROUND(((TAB.QTDMISTURA*(SELECT SUM(QTDMISTURA) 
                                                                FROM TPRLMP M 
                                                                LEFT JOIN TGFPRO P ON P.CODPROD = M.CODPRODMP 
                                                                WHERE IDEFX = ATV.IDEFX 
                                                                AND CODPRODPA = LMP.CODPRODPA
                                                                AND P.USOPROD = 'P' ))*100)/((SELECT SUM(LMP1.QTDMISTURA) QTDMISTURA
                                                                                            FROM TPRLMP LMP1 
                                                                                            LEFT JOIN TPRATV ATV1 ON ATV1.IDEFX = LMP1.IDEFX 
                                                                                            LEFT JOIN TGFPRO PRO1 ON PRO1.CODPROD = LMP1.CODPRODMP    
                                                                                            WHERE LMP1.CODPRODPA = LMP.CODPRODPA AND LMP1.IDEFX = LMP.IDEFX
                                                                                            AND PRO1.USOPROD != 'P' )+(SELECT SUM(QTDMISTURA) 
                                                                                                                        FROM TPRLMP M 
                                                                                                                        LEFT JOIN TGFPRO P ON P.CODPROD = M.CODPRODMP 
                                                                                                                        WHERE IDEFX = ATV.IDEFX 
                                                                                                                        AND CODPRODPA = LMP.CODPRODPA
                                                                                                                        AND P.USOPROD = 'P' )),4) 


        ELSE ROUND((LMP.QTDMISTURA*100)/ (SELECT SUM(LMP1.QTDMISTURA) QTDMISTURA
        FROM TPRLMP LMP1 
        LEFT JOIN TPRATV ATV1 ON ATV1.IDEFX = LMP1.IDEFX 
        LEFT JOIN TGFPRO PRO1 ON PRO1.CODPROD = LMP1.CODPRODMP    
        WHERE LMP1.CODPRODPA = LMP.CODPRODPA AND LMP1.IDEFX = LMP.IDEFX
        AND PRO1.USOPROD != 'P' )+(SELECT SUM(QTDMISTURA) 
                                                                FROM TPRLMP M 
                                                                LEFT JOIN TGFPRO P ON P.CODPROD = M.CODPRODMP 
                                                                WHERE IDEFX = ATV.IDEFX 
                                                                AND CODPRODPA = LMP.CODPRODPA
                                                                AND P.USOPROD = 'P' ),4) END AS PERCMP
       

         


        
        


                                               
 
                                
 FROM TPRPRC PRC
 LEFT JOIN TPRATV ATV ON ATV.IDPROC = PRC.IDPROC 
 LEFT JOIN TPRLMP LMP ON LMP.IDEFX = ATV.IDEFX 
 LEFT JOIN TGFPRO PROMP ON PROMP.CODPROD = LMP.CODPRODMP
 LEFT JOIN TGFPRO PROPA ON PROPA.CODPROD = LMP.CODPRODPA
 LEFT  JOIN (SELECT PRC.IDPROC, PRC.CODPRC, PRC.DESCRABREV, PRC.DHALTER, ATV.IDEFX, LMP.CODPRODPA,PROPA.DESCRPROD DESCRPRODPA, LMP.CODPRODMP, PROMP.DESCRPROD DESCRPRODMP, LMP.QTDMISTURA, LMP.CODVOL, PROMP.USOPROD,PRC.VERSAO,
(SELECT SUM(QTDMISTURA) FROM TPRLMP WHERE IDEFX = ATV.IDEFX AND CODPRODPA = LMP.CODPRODPA) TOTAL_MISTURA,
ROUND((LMP.QTDMISTURA*100)/(SELECT SUM(QTDMISTURA) FROM TPRLMP WHERE IDEFX = ATV.IDEFX AND CODPRODPA = LMP.CODPRODPA),2) PERCMP, PROPA.ATIVO
                                
 FROM TPRPRC PRC
 LEFT JOIN TPRATV ATV ON ATV.IDPROC = PRC.IDPROC 
 LEFT JOIN TPRLMP LMP ON LMP.IDEFX = ATV.IDEFX 
 LEFT JOIN TGFPRO PROMP ON PROMP.CODPROD = LMP.CODPRODMP
 LEFT JOIN TGFPRO PROPA ON PROPA.CODPROD = LMP.CODPRODPA
 WHERE PRC.IDPROC = (SELECT MAX(IDPROC) FROM TPRPRC
                 WHERE CODPRC = 100 /*PROCESSO PSA*/
                 AND DHALTER <= (SELECT ADD_MONTHS(REFERENCIA,-2)  FROM AD_FCICAB WHERE NUNICO = :P_NUNICO))
)TAB ON TAB.CODPRODPA = LMP.CODPRODMP
 WHERE PRC.IDPROC = (SELECT MAX(IDPROC) FROM TPRPRC
                 WHERE CODPRC = 1 /*PROCESSO PROCESSOPROP.*/
                 AND DHALTER <= (SELECT ADD_MONTHS(REFERENCIA,-2)  FROM AD_FCICAB WHERE NUNICO = :P_NUNICO))
                 AND PROPA.ATIVO = 'S'
 ORDER BY CODPRODPA ASC) TAB WHERE CODPRODPA NOT IN (SELECT DISTINCT CODPRODPA FROM AD_FCICOMP WHERE NUNICO = :P_NUNICO) /*AND ATIVO = 'S'*/