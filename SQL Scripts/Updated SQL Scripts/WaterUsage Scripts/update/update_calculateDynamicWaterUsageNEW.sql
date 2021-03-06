USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[update_calculateDynamicWaterUsageNEW]    Script Date: 10/23/2019 12:32:57 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 9/15/2019
-- Description: In case of New Generation data available for a month and year, the procedure populates the 
--				water usage table using dynamic water coeff 
-- =============================================
ALTER PROCEDURE [dbo].[update_calculateDynamicWaterUsageNEW]
	@year int, @month int
	AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @generation real;
	-- declare water usage amount variables
	DECLARE @withdrawalAmt float(5),  @consumptionAmt float(5), @derived int;
	-- declare dynamic coeff variables
	DECLARE @withdrawalCoeff float(5), @consumptionCoeff float(5);
	--declare wateUsageRawPerYear column variables
	DECLARE @plantCode int;
	-- Declare Cursor on PlantCodeTable
	DECLARE waterCursor CURSOR FOR select plantCode from plantCodeTable
	OPEN waterCursor;
	FETCH NEXT FROM waterCursor INTO @plantCode;
	WHILE @@FETCH_STATUS = 0
	BEGIN
		SELECT @withdrawalCoeff =waterWithdrawPerGen, @consumptionCoeff=waterConsumpPerGen
		FROM [EPA].[dbo].[dynamicWaterCoeffNewC] where plantCode=@plantCode;

		IF not exists (select 1 from waterUsageComplete  where (plantCode=@plantCode and usageYear=@year and usageMonth=@month))
		BEGIN
			IF exists (select 1 from generation where (plantCode=@plantCode and genYear=@year and genMonth=@month))
			BEGIN
				SELECT @generation = genData FROM generation where (plantCode=@plantCode and genYear=@year and genMonth=@month)
			END
			SET @withdrawalAmt = @withdrawalCoeff * @generation
			SET @consumptionAmt = @consumptionCoeff * @generation
			SET @derived=1
			INSERT INTO waterUsageComplete  VALUES (@plantCode, @year, @month, 
			@withdrawalAmt, @consumptionAmt, @derived);
		END
	FETCH NEXT FROM waterCursor INTO @plantCode;
	END   
	CLOSE waterCursor;  
	DEALLOCATE waterCursor; 
END
