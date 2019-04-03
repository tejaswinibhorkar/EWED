CREATE PROCEDURE fillEmissionsMonthly 
	@startYear int, @endYear int
AS 

	SET NOCOUNT ON;

	DECLARE @regId BIGINT, @month int, @year int;
	set @year = @startYear; set @month = 1;
	DECLARE emissionsCursor CURSOR FOR
	select distinct registryId from emPerYear where registryId in (select registryId from genperRegPerYearPerMonth) --2003 rows

	OPEN emissionsCursor;

	FETCH NEXT FROM emissionsCursor INTO @regId;
	
	DECLARE	@emAmount real, @derived bit, @registryId bigint;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		WHILE @year <= @endYear BEGIN
			SET @month = 1;
			WHILE @month <= 12 BEGIN
				EXEC [dbo].[getEmissions]
					@pgmSysId = N'0',
					@registryId = @regId,
					@emMonth = @month,
					@emYear = @year,
					@emAmount = @emAmount OUTPUT,
					@derived = @derived OUTPUT

					--SELECT	@regId, @year, @month, @emAmount as N'@emAmount', @derived as N'@derived'
					INSERT INTO emissionsMonthly VALUES (@regId, @year, @month, @emAmount, @derived);

					SET @month = @month + 1;
			END
			SET @year = @year + 1;
		END
		FETCH NEXT FROM emissionsCursor INTO @regId;
		SET @year = @startYear;
	END   
	CLOSE emissionsCursor;  
	DEALLOCATE emissionsCursor; 
GO