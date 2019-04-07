SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE getEmissions
	@pgmSysId varchar(30),
	@registryId BIGINT,
	@emMonth int,
	@emYear int,
	@emAmount float(5) output,
	@derived bit output
AS
	--declare @regId as BIGINT;
	declare @emCoeff as float(5);
	declare @genYearMonth as float(5);
	declare @minYear as int;
	declare @maxYear as int;
	
	
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	

	if(@registryId =0) begin
		SET @registryId = (select registryId from facility860Mapping where pgmSysId = @pgmSysId); 
	
	end

	set @genYearMonth = (select genSum from genPerRegPerYearPerMonth where registryId = @registryId
	and genYear = @emyear and genMonth = @emMonth);
		
	SET @emCoeff = (select emPerGen from emissionsCoeff where dataYear = @emYear and registryId = @registryId)
	
	--set @genYearMonth = (select genData from generation where plantCode = @pgmSysId and genYear = @emyear and genMonth = @emMonth)
	set @derived = 0;

	if(@emCoeff is null or @emCoeff = 0)  begin
		set @derived = 1;
		set @minYear = (select top 1 dataYear from emissionsCoeff where registryId = @registryId order by dataYear)
		set @maxYear = (select top 1 dataYear from emissionsCoeff where registryId = @registryId order by dataYear desc)	
		
		if (@emYear <= @minYear) begin
			set @emCoeff = (select emPerGen from emissionsCoeff where registryId = @registryId and dataYear = @minYear); end
		else begin
			set @emCoeff = (select emPerGen from emissionsCoeff where registryId = @registryId and dataYear = @maxYear); end
		
	end 
	
	set @emAmount = (@emCoeff * @genYearMonth);
	
GO