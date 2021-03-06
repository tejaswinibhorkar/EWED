USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[deletedNewPlantCodesFromWaterUsageData]    Script Date: 10/23/2019 12:27:04 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 9/25/2019
-- Description:	Deletes new plantcodes from waterUsageData for new calculations based on new dynamic coeff available
-- =============================================
ALTER PROCEDURE [dbo].[deletedNewPlantCodesFromWaterUsageData]
	AS
BEGIN
	SET NOCOUNT ON;
	Declare @plantCode int; 

	DECLARE plantCodeCursor CURSOR FOR SELECT distinct plantCode FROM cooling_summary_raw_14_17 
	OPEN plantCodeCursor;
	FETCH NEXT FROM plantCodeCursor INTO @plantCode;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		IF not exists (select 1 from plantCodeTable where (plantCode=@plantCode))
		BEGIN
			DELETE FROM waterUsageComplete where (plantCode=@plantCode and derived=2);
		END
		FETCH NEXT FROM plantCodeCursor INTO @plantCode;
	END   
	CLOSE plantCodeCursor;  
	DEALLOCATE plantCodeCursor; 
END
