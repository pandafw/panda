SELECT * FROM TEST 
WHERE 
	@validId[ID=:id]
	@idList[AND ID IN (:idList)]
	@name[AND NAME=:name]
ORDER BY 
	@orderCol[::orderCol] @!orderCol[ID] @orderDir[::orderDir]
