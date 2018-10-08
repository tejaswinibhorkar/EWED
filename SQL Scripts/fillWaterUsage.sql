USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[fillWaterUsage]    Script Date: 10/6/2018 4:35:34 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[fillWaterUsage] 
	@startYear int, @endYear int
AS 

	SET NOCOUNT ON;

	DECLARE @plantCode VARCHAR(30), @month int, @year int;
	set @year = @startYear; set @month = 1;
	
	DECLARE usageCursor CURSOR FOR
	select distinct plantCode from waterUsage where plantCode in (select distinct plantCode from genPerYear) 

	OPEN usageCursor;

	FETCH NEXT FROM usageCursor INTO @plantCode;
	
	DECLARE	@waterUsage real, @derived bit;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		WHILE @year <= @endYear BEGIN
			SET @month = 1;
			WHILE @month <= 12 BEGIN
				EXEC [dbo].[getWaterUsage]
					@pgmSysId = @plantCode,
					@usageMonth = @month,
					@usageYear = @year,
					@waterUsage = @waterUsage OUTPUT,
					@derived = @derived OUTPUT
			
					--SELECT @plantCode, @year, @month, @waterUsage, @derived;
					
					if(@waterUsage is not null and @waterUsage <> 0 and @derived = 1) begin
						INSERT INTO waterUsage VALUES (@plantCode, @year, @month, @waterUsage, 'Million Gallons', @derived);
					end 

					SET @month = @month + 1;
			END
			SET @year = @year + 1;
		END
		FETCH NEXT FROM usageCursor INTO @plantCode;
		SET @year = @startYear;
	END   
	CLOSE usageCursor;  
	DEALLOCATE usageCursor; 
