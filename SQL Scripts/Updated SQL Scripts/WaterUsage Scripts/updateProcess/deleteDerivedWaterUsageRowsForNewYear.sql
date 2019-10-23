USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[deleteDerivedWaterUsageRowsForNewYear]    Script Date: 10/23/2019 12:26:40 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 9/20/2019
-- Description:	Delete derived rows for the available year
-- =============================================
ALTER PROCEDURE [dbo].[deleteDerivedWaterUsageRowsForNewYear] 
	@year int
AS
BEGIN
	SET NOCOUNT ON;
	
	DELETE FROM waterUsageComplete
	where (usageYear=@year);
END
