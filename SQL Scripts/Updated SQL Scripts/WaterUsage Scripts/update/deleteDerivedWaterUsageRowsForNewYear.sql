USE [EPA]
GO
/****** Object:  StoredProcedure [dbo].[deleteDerivedWaterUsageRowsForNewYear]    Script Date: 10/29/2019 6:34:25 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Tejaswini Bhorkar
-- Create date: 9/20/2019
-- Description:	Delete derived rows for the available year
-- =============================================
CREATE OR ALTER PROCEDURE [dbo].[deleteDerivedWaterUsageRowsForNewYear] 
	@year int
AS
BEGIN
	SET NOCOUNT ON;
	
	DELETE FROM waterUsageComplete
	where (usageYear=@year);
END
