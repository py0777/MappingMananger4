SELECT  /*+ LEADING(A) USE_NL(A B C) INDEX(A RPA5000_PK) INDEX(B ACB2000_UK03)
            BIZ: BG입출금_자금이체업무관련보고서(월보)_이수진
            IO: BRP1116B1.RPA5000_Q001
        */ 
       NVL(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') = '01' AND C.ACNT_PRDT_SCD <> '30' 
                     AND C.BNK_DACA_ACNT_YN = 'N'       AND A.DACA > 0 
                    THEN A.DACA 
                    ELSE 0 END),0)      AS PSNL_CSGN_BLCE /* 개인위탁잔액 */
     , NVL(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') <> '01' AND C.ACNT_PRDT_SCD <> '30' 
                     AND C.BNK_DACA_ACNT_YN = 'N'        AND A.DACA > 0 
                    THEN A.DACA 
                    ELSE 0 END),0)      AS CRP_CSGN_BLCE /* 법인위탁잔액 */
     , NVL(ROUND(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') = '01' AND C.ACNT_PRDT_SCD = '30' 
                           AND C.BNK_DACA_ACNT_YN = 'N'       AND A.DACA > 0 
                          THEN A.DACA
                          ELSE 0 END) / 1000000 ),0)      AS PSNL_DRV_BLCE /* 개인파생잔액 */
     , NVL(ROUND(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') <> '01' AND C.ACNT_PRDT_SCD = '30' 
                           AND C.BNK_DACA_ACNT_YN = 'N'        AND A.DACA > 0 
                          THEN A.DACA 
                          ELSE 0 END) / 1000000 ),0)      AS CRP_DRV_BLCE /* 법인파생잔액 */
     , 0                 AS PSNL_ETC_BLCE /* 개인기타잔액 */
     , 0                 AS CRP_ETC_BLCE  /* 법인기타잔액 */
     , NVL(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') = '01' AND C.ACNT_STTS_SCD = '0' AND C.ACNT_PRDT_SCD <> '30' 
                    THEN 1
                    ELSE 0 END),0)          AS PSNL_CSGN_ACNT_CNT /* 개인위탁계좌건수 */
     , NVL(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') <> '01' AND C.ACNT_STTS_SCD = '0' AND C.ACNT_PRDT_SCD <> '30' 
                    THEN 1
                    ELSE 0 END),0)          AS CRP_CSGN_ACNT_CNT /* 법인위탁계좌건수 */
     , NVL(SUM(CASE WHEN NVL(B.PSNL_CRP_SCD,'0') = '01' AND C.ACNT_STTS_SCD = '0' AND C.ACNT_PRDT_SCD = '30' 
                    THEN 1
                    ELSE 0 END),0)          AS PSNL_DRV_ACNT_CNT /* 개인파생계좌건수 */
     , (CASE WHEN NVL(B.PSNL_CRP_SCD,'0') <> '01' AND C.ACNT_STTS_SCD = '0' AND C.ACNT_PRDT_SCD = '30' 
                    THEN 1
                    when 1=1 then 0
                    ELSE 0 END  )        AS CRP_DRV_ACNT_CNT /* 법인파생계좌건수 */
     , 0            AS PSNL_ETC_ACNT_CNT /* 개인기타계좌건수 */
     , 0            AS CRP_ETC_ACNT_CNT  /* 법인기타계좌건수 */
  FROM RPA5000 A /* 출납관리_일별예수금기본 */
     , ACA1000 B /* 계좌관리_고객기본 */
     , ACB2000 C /* 계좌관리_개별계좌기본 */
 WHERE A.STDR_DT  = :STDR_DT /* 기준일자 */
   AND A.IACN  = C.IACN
   AND C.CUNO  = B.CUNO