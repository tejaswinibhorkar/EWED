USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[calculateDynamicWaterUsageNEW]    Script Date: 10/23/2019 12:25:14 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 4/1/2019
-- Description:	Calculates monthly water usage using dynamic water coeff for the years 
--				with no data of the plantcodes with raw data.
--
--				Details: We have water data available from 2014 – 2017. 
--				Some of the plant type water data is missing in the given range, 
--				for that we will use dynamic water coeff to calculate water usage data for the 
--				complete range 2003-2017
-- =============================================
ALTER PROCEDURE [dbo].[calculateDynamicWaterUsageNEW]
	@startYear int, @endYear int
	AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	DECLARE @generation real;
	DECLARE @year int;
	SET @year = @startYear;

	-- declare water usage amount variables
	DECLARE @withdrawalAmt float(5),  @consumptionAmt float(5), @derived int, @month int;
	 
	-- declare dynamic coeff variables
	DECLARE @withdrawalCoeff float(5),
		@consumptionCoeff float(5);

	--declare wateUsageRawPerYear column variables
	DECLARE @plantCode int;

	DECLARE waterCursor CURSOR FOR
	select plantCode from plantCodeTable

	OPEN waterCursor;

	FETCH NEXT FROM waterCursor INTO @plantCode;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		SELECT @withdrawalCoeff =waterWithdrawPerGen, @consumptionCoeff=waterConsumpPerGen
		FROM [EPA].[dbo].[dynamicWaterCoeffNewC] where plantCode=@plantCode;

		WHILE @year <= @endYear
		BEGIN
			SET @month = 1;
				WHILE @month <= 12 BEGIN
					IF not exists (select 1 from waterUsageComplete  
										where (plantCode=@plantCode and usageYear=@year and usageMonth=@month))
					BEGIN
						IF (not EXISTS (SELECT genData FROM generation where (plantCode=@plantCode and genYear=@year and genMonth=@month)))
							begin 
								SET @generation = 0
							end
							Else begin
								SELECT @generation = genData FROM generation where (plantCode=@plantCode and genYear=@year and genMonth=@month)
							end
						SET @withdrawalAmt = @withdrawalCoeff * @generation
						SET @consumptionAmt = @consumptionCoeff * @generation
						SET @derived=1
	
						INSERT INTO waterUsageComplete  VALUES (@plantCode, @year, @month, 
						@withdrawalAmt, @consumptionAmt, @derived);
					END
					SET @month = @month + 1;
				END
			SET @year = @year + 1;
		END

		SET @year = @startYear

	FETCH NEXT FROM waterCursor INTO @plantCode;
	END   
	CLOSE waterCursor;  
	DEALLOCATE waterCursor; 
END
