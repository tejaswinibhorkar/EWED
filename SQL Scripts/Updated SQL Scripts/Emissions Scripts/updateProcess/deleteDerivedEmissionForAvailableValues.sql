USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[deleteDerivedEmissionForAvailableValues]    Script Date: 10/23/2019 12:26:03 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 8/28/19
-- Description:	This procedure checks for derived plantcode and year from emissionsMonthlyNew table 
--				in emissionCoeffNew table. The Coeff for that pair indicates that emission value 
--				is available and we don't need to derive it.
-- =============================================
ALTER PROCEDURE [dbo].[deleteDerivedEmissionForAvailableValues]
	@year int
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @plantCode varchar(30);

	DECLARE emissionsCursor CURSOR FOR
	select distinct plantCode from emissionsMonthlyNew where derived=1 and emYear=@year

	OPEN emissionsCursor;
	FETCH NEXT FROM emissionsCursor INTO @plantCode;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		IF exists (select 1 from emissionsCoeffNew where (pgmSysId=@plantCode and dataYear=@year))
		BEGIN
		-- delete all the rows for it cz it shouldnt be derived
			DELETE FROM emissionsMonthlyNew where plantCode=@plantCode and emYear=@year;
		END
	FETCH NEXT FROM emissionsCursor INTO @plantCode ;
	END   
	CLOSE emissionsCursor;  
	DEALLOCATE emissionsCursor; 
END
