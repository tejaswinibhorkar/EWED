USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[update_dynamic_coeff]    Script Date: 10/23/2019 12:33:33 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 8/28/2019
-- Description:	Updates dynamic coeff if any new water data is available
-- =============================================
ALTER PROCEDURE [dbo].[update_dynamic_coeff] 
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @plantCode int, @consumptionCoeff real, @withdrawalCoeff real;
	Declare @countInsert int, @countUpdate int;
	SET @countInsert=0;
	SET @countUpdate=0;

	DECLARE dynamicCoeffCursor CURSOR FOR
	Select w.plantCode,
	SUM(w.waterConsumption)/SUM(g.generation) as waterConsumpPerGen, 
	SUM(w.waterWithdrawal)/SUM(g.generation) as waterWithdrawPerGen from cooling_summary_raw_per_year as w, 
	[genPerYear] as g where w.plantCode = g.plantCode and w.usageYear = g.genYear and g.generation !=0
	Group by w.plantCode

	OPEN dynamicCoeffCursor;
	FETCH NEXT FROM dynamicCoeffCursor INTO @plantCode, @consumptionCoeff, @withdrawalCoeff;
	
	WHILE @@FETCH_STATUS = 0
	BEGIN
		IF(NOT EXISTS(SELECT 1 FROM dynamicWaterCoeffNewC where plantCode=@plantCode))
		BEGIN
			print 'Inserting dynamic coeff';
			SET @countInsert= @countInsert+1;
			INSERT INTO dynamicWaterCoeffNewC VALUES (@plantCode, @consumptionCoeff, @withdrawalCoeff);
		 END;
		 ELSE
		 BEGIN
			print 'Updating Dynamic Coeff';
			SET @countUpdate= @countUpdate+1;
			 UPDATE dynamicWaterCoeffNewC
			 SET waterConsumpPerGen = @consumptionCoeff,
			 waterWithdrawPerGen = @withdrawalCoeff
			 where plantCode=@plantCode;
		 END

		FETCH NEXT FROM dynamicCoeffCursor INTO @plantCode, @consumptionCoeff, @withdrawalCoeff;
	END
	CLOSE dynamicCoeffCursor;  
	DEALLOCATE dynamicCoeffCursor; 
	print @countInsert
	print @countUpdate
END