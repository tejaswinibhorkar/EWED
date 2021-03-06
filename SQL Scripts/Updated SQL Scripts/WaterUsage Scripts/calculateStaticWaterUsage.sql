USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[calculateStaticWaterUsage]    Script Date: 10/23/2019 12:25:48 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author, Tejaswini Bhorkar>
-- Create date: <Create Date, 2/19/2019>
-- Description:	Calculates water usage using Static Coefficients
--				For the plantcodes not in the raw data files of 2014-17 range, we will use 
--				static water coeff to calculate water usage data for the complete range 2003-2017
-- =============================================
ALTER PROCEDURE [dbo].[calculateStaticWaterUsage] 
	@startYear int, @endYear int

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	--declare gen columns
	DECLARE @plantCode int, @genYear int, @genMonth int, @genData real;

	Declare @withdrawalCoeff float(5),
		@consumptionCoeff float(5),
		@withdrawalAmt float,
		@consumptionAmt float,
		@derived int,
		@dominantPlantType as varchar(30);

    DECLARE genCursor CURSOR FOR
	select plantCode, genMonth, genYear, genData from generation where genYear between @startYear and @endYear order by plantCode

	OPEN genCursor;
	Declare @var int;

	FETCH NEXT FROM genCursor INTO @plantCode, @genMonth, @genYear, @genData;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		IF not exists (select 1 from waterUsageComplete
							where plantCode=@plantCode and usageYear=@genYear and usageMonth=@genMonth)
		begin
			EXEC [dbo].[getStaticWaterCoeff]
				@plantCode = @plantCode,
				@usageYear = @genYear,
				@withdrawal = @withdrawalCoeff OUTPUT,
				@consumption = @consumptionCoeff OUTPUT,
				@dominantPlantType = @dominantPlantType OUTPUT

				print @dominantPlantType
				if ((@dominantPlantType='WAT-PS' or @dominantPlantType='WAT-HY') and @genData<0)
				Begin
				print 'if'
					set @withdrawalAmt = 0;
					set @consumptionAmt = 0;
				End
				Else Begin
					print 'else'
					set @withdrawalAmt = (@withdrawalCoeff * @genData)/1000000;
					set @consumptionAmt = (@consumptionCoeff * @genData)/1000000;
				end
				set @derived=2;
				
				INSERT INTO waterUsageComplete VALUES (@plantCode, @genYear, @genMonth, 
					@withdrawalAmt, @consumptionAmt, @derived);
		end
	FETCH NEXT FROM genCursor INTO @plantCode, @genMonth, @genYear, @genData;
	END   
	CLOSE genCursor;  
	DEALLOCATE genCursor; 
end