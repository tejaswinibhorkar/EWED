USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[update_fillEmissionsMonthlyNew]    Script Date: 10/23/2019 12:40:53 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 9/10/2019
-- Description:	Update the emissionsMonthlyNew table with new emissions value in case of new available generation value 
--				for a particular month and year. The procedure will convert emissions only if generation data is available 
--				for the plantcode-month-year. It will populate emissionsMonthlyNew table only if the record doesn't already exists
-- =============================================

ALTER PROCEDURE [dbo].[update_fillEmissionsMonthlyNew] 
	@year int, @month int
AS 
	SET NOCOUNT ON;
	DECLARE @plantCode varchar(30);

	DECLARE emissionsCursor CURSOR FOR
	select distinct ORISCode from emPerYearNew where ORISCode in (select plantCode from generation)
	OPEN emissionsCursor;
	FETCH NEXT FROM emissionsCursor INTO @plantCode;
	
	DECLARE	@emAmount real, @derived bit;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		-- check if generation available
		IF exists (select 1 from generation where (plantCode=@plantCode and genYear=@year and genMonth=@month))
		BEGIN
			EXEC [dbo].[getEmissionsNew]
				@pgmSysId = @plantCode,
				@emMonth = @month,
				@emYear = @year,
				@emAmount = @emAmount OUTPUT,
				@derived = @derived OUTPUT
				-- check if emissions is present
				IF not exists (select 1 from emissionsMonthlyNew where (plantCode=@plantCode and emYear=@year and emMonth=@month))
				BEGIN
					INSERT INTO emissionsMonthlyNew VALUES (@plantCode, @year, @month, @emAmount, @derived);
				END
		END
		FETCH NEXT FROM emissionsCursor INTO @plantCode;
	END   
	CLOSE emissionsCursor;  
	DEALLOCATE emissionsCursor;