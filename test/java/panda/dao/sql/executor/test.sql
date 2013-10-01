SELECT * FROM TEST 
WHERE 
	@validId[ID=:id]
	@intList[AND ID IN (:intList)]
	@fstr[AND FSTR=:fstr]
ORDER BY 
	@orderCol[::orderCol] @!orderCol[ID] @orderDir[::orderDir]
