databaseChangeLog = {

	changeSet(author: "sgates (generated)", id: "1441833543617-1") {
		addNotNullConstraint(columnDataType: "varchar(2000)", columnName: "url", tableName: "content_item")
	}

	changeSet(author: "sgates (generated)", id: "1441833543617-2") {
		addNotNullConstraint(columnDataType: "varchar(2000)", columnName: "source_url", tableName: "media_item")
	}

	changeSet(author: "sgates (generated)", id: "1441833543617-3") {
		addNotNullConstraint(columnDataType: "varchar(2000)", columnName: "website_url", tableName: "source")
	}

	changeSet(author: "sgates (generated)", id: "1441833543617-4") {
		addNotNullConstraint(columnDataType: "varchar(5000)", columnName: "requested_url", tableName: "syndication_request")
	}
}
