USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[getStaticWaterCoeff]    Script Date: 9/27/2019 5:11:38 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 9/9/201
-- Description:	Returns the static coeff from staticCOeffView using the dominantPlantType of the plant
-- =============================================
ALTER PROCEDURE [dbo].[getStaticWaterCoeff]
	-- Add the parameters for the stored procedure here
		@plantCode int,
		@usageYear int,
		@withdrawal float(5) OUTPUT,
		@consumption float(5) OUTPUT,
		@dominantPlantType varchar(30) OUTPUT
AS

	--declare @dominantPlantType as varchar(30)

BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	-- Get dominant plant type of the plant code for that year 
	SET @dominantPlantType = (select dominantType from dominantPlantType where genYear = @usageYear and plantCode = @plantCode)

	SET @withdrawal = (select withdrawalCoeff from staticCoeffView where plantType = @dominantPlantType)
	SET @consumption = (select consumptionCoeff from staticCoeffView where plantType = @dominantPlantType)

END
