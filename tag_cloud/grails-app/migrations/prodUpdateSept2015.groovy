databaseChangeLog = {

	changeSet(author: "sgates (generated)", id: "1441833238561-1") {
		modifyDataType(columnName: "contact_email", newDataType: "varchar(2000)", tableName: "campaign")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-2") {
		modifyDataType(columnName: "description", newDataType: "varchar(2000)", tableName: "campaign")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-3") {
		addNotNullConstraint(columnDataType: "bigint", columnName: "source_id", tableName: "campaign")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-4") {
		modifyDataType(columnName: "url", newDataType: "varchar(2000)", tableName: "content_item")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-5") {
		modifyDataType(columnName: "code", newDataType: "varchar(5000)", tableName: "media_item")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-6") {
		modifyDataType(columnName: "source_url", newDataType: "varchar(2000)", tableName: "media_item")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-7") {
		modifyDataType(columnName: "target_url", newDataType: "varchar(2000)", tableName: "media_item")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-8") {
		modifyDataType(columnName: "contact_email", newDataType: "varchar(2000)", tableName: "source")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-9") {
		modifyDataType(columnName: "large_logo_url", newDataType: "varchar(2000)", tableName: "source")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-10") {
		modifyDataType(columnName: "small_logo_url", newDataType: "varchar(2000)", tableName: "source")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-11") {
		modifyDataType(columnName: "website_url", newDataType: "varchar(2000)", tableName: "source")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-12") {
		modifyDataType(columnName: "admin_note", newDataType: "varchar(2000)", tableName: "syndication_request")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-13") {
		modifyDataType(columnName: "requested_url", newDataType: "varchar(5000)", tableName: "syndication_request")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-14") {
		modifyDataType(columnName: "requester_note", newDataType: "varchar(2000)", tableName: "syndication_request")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-15") {
		dropIndex(indexName: "name", tableName: "tag")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-16") {
		dropTable(tableName: "registered_application")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-17") {
		dropTable(tableName: "report")
	}

	changeSet(author: "sgates (generated)", id: "1441833238561-18") {
		dropTable(tableName: "type")
	}
}
