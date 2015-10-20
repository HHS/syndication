databaseChangeLog = {

	changeSet(author: "sgates (generated)", id: "1441831936502-1") {
		createTable(tableName: "alternate_image") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "alternate_imaPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "height", type: "integer")

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "width", type: "integer")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-2") {
		createTable(tableName: "cached_content") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "cached_contenPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "content", type: "longtext") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-3") {
		createTable(tableName: "campaign") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "campaignPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "contact_email", type: "varchar(2000)")

			column(name: "description", type: "varchar(2000)")

			column(name: "end_date", type: "datetime")

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "source_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "start_date", type: "datetime") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-4") {
		createTable(tableName: "campaign_media_items") {
			column(name: "campaign_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-5") {
		createTable(tableName: "campaign_metric") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "campaign_metrPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "api_view_count", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "campaign_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "day", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "storefront_view_count", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-6") {
		createTable(tableName: "campaign_subscriber") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "campaign_subsPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "campaign_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "subscriber_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-7") {
		createTable(tableName: "collection_media_item") {
			column(name: "collection_media_items_id", type: "bigint")

			column(name: "media_item_id", type: "bigint")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-8") {
		createTable(tableName: "content_item") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "content_itemPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "externaluid", type: "varchar(255)")

			column(name: "syndication_id", type: "bigint")

			column(name: "url", type: "varchar(2000)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-9") {
		createTable(tableName: "extended_attribute") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "extended_attrPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "value", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-10") {
		createTable(tableName: "extraction_options") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "extraction_opPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "disable_cache", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "namespace", type: "varchar(255)")

			column(name: "strip_anchors", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "strip_comments", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "strip_images", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "strip_in_line_styles", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "strip_scripts", type: "bit") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-11") {
		createTable(tableName: "featured_media") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "featured_mediPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-12") {
		createTable(tableName: "flagged_media") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "flagged_mediaPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "date_flagged", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "failure_type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "ignored", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-13") {
		createTable(tableName: "language") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "languagePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "is_active", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "iso_code", type: "varchar(3)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-14") {
		createTable(tableName: "media_item") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "media_itemPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "active", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "custom_preview_url", type: "varchar(2000)")

			column(name: "custom_thumbnail_url", type: "varchar(2000)")

			column(name: "date_content_authored", type: "datetime")

			column(name: "date_content_published", type: "datetime")

			column(name: "date_content_reviewed", type: "datetime")

			column(name: "date_content_updated", type: "datetime")

			column(name: "date_syndication_captured", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "date_syndication_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "date_syndication_visible", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "longtext")

			column(name: "external_guid", type: "varchar(255)")

			column(name: "hash", type: "varchar(255)")

			column(name: "language_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "manually_managed", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "source_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "source_url", type: "varchar(2000)") {
				constraints(nullable: "false")
			}

			column(name: "target_url", type: "varchar(2000)")

			column(name: "visible_in_storefront", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "class", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "alt_text", type: "varchar(255)")

			column(name: "height", type: "integer")

			column(name: "image_format", type: "varchar(255)")

			column(name: "width", type: "integer")

			column(name: "code", type: "varchar(5000)")

			column(name: "duration", type: "integer")

			column(name: "social_media_type", type: "varchar(255)")

			column(name: "extraction_options_id", type: "bigint")

			column(name: "period", type: "varchar(255)")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-15") {
		createTable(tableName: "media_item_subscriber") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "media_item_suPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "subscriber_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-16") {
		createTable(tableName: "media_metric") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "media_metricPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "api_view_count", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "day", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "media_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "storefront_view_count", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-17") {
		createTable(tableName: "media_preview") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "media_previewPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "image_data", type: "mediumblob") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-18") {
		createTable(tableName: "media_thumbnail") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "media_thumbnaPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "image_data", type: "mediumblob") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-19") {
		createTable(tableName: "role") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "authority", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-20") {
		createTable(tableName: "search_query") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "search_queryPK")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "query", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "search_count", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-21") {
		createTable(tableName: "social_media_account") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "social_media_PK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "account_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "account_type", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-22") {
		createTable(tableName: "source") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sourcePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "acronym", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "contact_email", type: "varchar(2000)")

			column(name: "description", type: "longtext")

			column(name: "large_logo_url", type: "varchar(2000)")

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "small_logo_url", type: "varchar(2000)")

			column(name: "website_url", type: "varchar(2000)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-23") {
		createTable(tableName: "syndication_request") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "syndication_rPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "admin_note", type: "varchar(2000)")

			column(name: "contact_email", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "media_item_id", type: "bigint")

			column(name: "requested_url", type: "varchar(5000)") {
				constraints(nullable: "false")
			}

			column(name: "requester_note", type: "varchar(2000)")

			column(name: "status", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-24") {
		createTable(tableName: "system_event") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "system_eventPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(255)")

			column(name: "message_details", type: "varchar(255)")

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-25") {
		createTable(tableName: "tag") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "tagPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "language_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-26") {
		createTable(tableName: "tag_content_items") {
			column(name: "tag_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "content_item_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-27") {
		createTable(tableName: "tag_type") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "tag_typePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "varchar(255)")

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-28") {
		createTable(tableName: "user") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "userPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "account_expired", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "account_locked", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "code_expiration_date", type: "datetime")

			column(name: "enabled", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)")

			column(name: "password", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "password_expired", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "subscriber_id", type: "bigint")

			column(name: "unlock_code", type: "varchar(255)")

			column(name: "username", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-29") {
		createTable(tableName: "user_media_item") {
			column(name: "user_likes_id", type: "bigint")

			column(name: "media_item_id", type: "bigint")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-30") {
		createTable(tableName: "user_media_list") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "user_media_liPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-31") {
		createTable(tableName: "user_media_list_media_item") {
			column(name: "user_media_list_media_items_id", type: "bigint")

			column(name: "media_item_id", type: "bigint")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-32") {
		createTable(tableName: "user_role") {
			column(name: "role_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-33") {
		addPrimaryKey(columnNames: "campaign_id, media_item_id", tableName: "campaign_media_items")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-34") {
		addPrimaryKey(columnNames: "tag_id, content_item_id", tableName: "tag_content_items")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-35") {
		addPrimaryKey(columnNames: "role_id, user_id", constraintName: "user_rolePK", tableName: "user_role")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-65") {
		createIndex(indexName: "FK_k4pgdk8g12u12h1f47scjqqf9", tableName: "alternate_image") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-66") {
		createIndex(indexName: "FK_lsm1ljg48q9phsd0m09ib73mf", tableName: "cached_content") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-67") {
		createIndex(indexName: "FK_s0e00i8ji89h5y5qnvelcvfs2", tableName: "campaign") {
			column(name: "source_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-68") {
		createIndex(indexName: "name_uniq_1441831936417", tableName: "campaign", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-69") {
		createIndex(indexName: "FK_bi3sixpk4k9m7w8wlp957fjiv", tableName: "campaign_media_items") {
			column(name: "campaign_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-70") {
		createIndex(indexName: "FK_jyy4adufx10miiok8nm6kwwoc", tableName: "campaign_media_items") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-71") {
		createIndex(indexName: "FK_nstx6ffbb6h7i7ndiqu0gb958", tableName: "campaign_metric") {
			column(name: "campaign_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-72") {
		createIndex(indexName: "FK_q3nqdk1j93tukbbbq1aukyni1", tableName: "campaign_subscriber") {
			column(name: "campaign_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-73") {
		createIndex(indexName: "FK_78blbow1dlwamb58wkfqtg0wv", tableName: "collection_media_item") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-74") {
		createIndex(indexName: "FK_hfuuvr0ubft7es368wr8xivt9", tableName: "collection_media_item") {
			column(name: "collection_media_items_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-75") {
		createIndex(indexName: "syndication_id_uniq_1441831936425", tableName: "content_item", unique: "true") {
			column(name: "syndication_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-76") {
		createIndex(indexName: "FK_3ndsffiybwp1v7ac4adt60v58", tableName: "extended_attribute") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-77") {
		createIndex(indexName: "FK_bohgx9t8uctl2i2f1kelpvic3", tableName: "featured_media") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-78") {
		createIndex(indexName: "FK_6jrfbsp8b4btwj3fgyj1e8xqm", tableName: "flagged_media") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-79") {
		createIndex(indexName: "iso_code_uniq_1441831936429", tableName: "language", unique: "true") {
			column(name: "iso_code")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-80") {
		createIndex(indexName: "name_uniq_1441831936429", tableName: "language", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-81") {
		createIndex(indexName: "FK_e500lkt684srq85419729riyf", tableName: "media_item") {
			column(name: "extraction_options_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-82") {
		createIndex(indexName: "FK_fybcuc2l6nm794wkqxq5mal3y", tableName: "media_item") {
			column(name: "language_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-83") {
		createIndex(indexName: "FK_p5gqeyuvi83gw3luo0lf4o3gh", tableName: "media_item") {
			column(name: "source_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-84") {
		createIndex(indexName: "FK_ns0b5ru4gt2rre44u3tr1a8wg", tableName: "media_item_subscriber") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-85") {
		createIndex(indexName: "FK_x6e5xsh5ug6dhqivvp9fr0od", tableName: "media_metric") {
			column(name: "media_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-86") {
		createIndex(indexName: "authority_uniq_1441831936436", tableName: "role", unique: "true") {
			column(name: "authority")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-87") {
		createIndex(indexName: "acronym_uniq_1441831936437", tableName: "source", unique: "true") {
			column(name: "acronym")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-88") {
		createIndex(indexName: "name_uniq_1441831936437", tableName: "source", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-89") {
		createIndex(indexName: "FK_okwa7indwg8twdq9w66udu4vo", tableName: "syndication_request") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-90") {
		createIndex(indexName: "FK_66shlb3u4r1wxfqxmoj1m9g38", tableName: "tag") {
			column(name: "language_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-91") {
		createIndex(indexName: "FK_jsyn12seihbr7e97288w883jg", tableName: "tag") {
			column(name: "type_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-92") {
		createIndex(indexName: "unique_name", tableName: "tag", unique: "true") {
			column(name: "language_id")

			column(name: "name")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-93") {
		createIndex(indexName: "FK_75ttux0af04odo6nnh5noky7v", tableName: "tag_content_items") {
			column(name: "content_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-94") {
		createIndex(indexName: "FK_e0i5dmkg702neoamu9r93rytp", tableName: "tag_content_items") {
			column(name: "tag_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-95") {
		createIndex(indexName: "name_uniq_1441831936442", tableName: "tag_type", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-96") {
		createIndex(indexName: "username_uniq_1441831936442", tableName: "user", unique: "true") {
			column(name: "username")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-97") {
		createIndex(indexName: "FK_hn9ejgcy7s45d831t48wft76p", tableName: "user_media_item") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-98") {
		createIndex(indexName: "FK_qcasduoot76fbly6i0h7gdfmp", tableName: "user_media_item") {
			column(name: "user_likes_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-99") {
		createIndex(indexName: "FK_1noaohkam5wxaicgjc8h0dfa0", tableName: "user_media_list") {
			column(name: "user_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-100") {
		createIndex(indexName: "FK_4uh9rp7rabkblr7eljpt16hsr", tableName: "user_media_list_media_item") {
			column(name: "media_item_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-101") {
		createIndex(indexName: "FK_j0f3neyb4dq2t2clrs4xf8247", tableName: "user_media_list_media_item") {
			column(name: "user_media_list_media_items_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-102") {
		createIndex(indexName: "FK_apcc8lxk2xnug8377fatvbn04", tableName: "user_role") {
			column(name: "user_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-103") {
		createIndex(indexName: "FK_it77eq964jhfqtu54081ebtio", tableName: "user_role") {
			column(name: "role_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441833030172-1") {
		createIndex(indexName: "FK_bi3sixpk4k9m7w8wlp957fjiv", tableName: "campaign_media_items") {
			column(name: "campaign_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441833030172-2") {
		createIndex(indexName: "FK_66shlb3u4r1wxfqxmoj1m9g38", tableName: "tag") {
			column(name: "language_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441833030172-3") {
		createIndex(indexName: "FK_e0i5dmkg702neoamu9r93rytp", tableName: "tag_content_items") {
			column(name: "tag_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441833030172-4") {
		createIndex(indexName: "FK_it77eq964jhfqtu54081ebtio", tableName: "user_role") {
			column(name: "role_id")
		}
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-36") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "alternate_image", constraintName: "FK_k4pgdk8g12u12h1f47scjqqf9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-37") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "cached_content", constraintName: "FK_lsm1ljg48q9phsd0m09ib73mf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-38") {
		addForeignKeyConstraint(baseColumnNames: "source_id", baseTableName: "campaign", constraintName: "FK_s0e00i8ji89h5y5qnvelcvfs2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "source", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-39") {
		addForeignKeyConstraint(baseColumnNames: "campaign_id", baseTableName: "campaign_media_items", constraintName: "FK_bi3sixpk4k9m7w8wlp957fjiv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "campaign", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-40") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "campaign_media_items", constraintName: "FK_jyy4adufx10miiok8nm6kwwoc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-41") {
		addForeignKeyConstraint(baseColumnNames: "campaign_id", baseTableName: "campaign_metric", constraintName: "FK_nstx6ffbb6h7i7ndiqu0gb958", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "campaign", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-42") {
		addForeignKeyConstraint(baseColumnNames: "campaign_id", baseTableName: "campaign_subscriber", constraintName: "FK_q3nqdk1j93tukbbbq1aukyni1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "campaign", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-43") {
		addForeignKeyConstraint(baseColumnNames: "collection_media_items_id", baseTableName: "collection_media_item", constraintName: "FK_hfuuvr0ubft7es368wr8xivt9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-44") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "collection_media_item", constraintName: "FK_78blbow1dlwamb58wkfqtg0wv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-45") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "extended_attribute", constraintName: "FK_3ndsffiybwp1v7ac4adt60v58", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-46") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "featured_media", constraintName: "FK_bohgx9t8uctl2i2f1kelpvic3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-47") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "flagged_media", constraintName: "FK_6jrfbsp8b4btwj3fgyj1e8xqm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-48") {
		addForeignKeyConstraint(baseColumnNames: "extraction_options_id", baseTableName: "media_item", constraintName: "FK_e500lkt684srq85419729riyf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "extraction_options", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-49") {
		addForeignKeyConstraint(baseColumnNames: "language_id", baseTableName: "media_item", constraintName: "FK_fybcuc2l6nm794wkqxq5mal3y", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "language", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-50") {
		addForeignKeyConstraint(baseColumnNames: "source_id", baseTableName: "media_item", constraintName: "FK_p5gqeyuvi83gw3luo0lf4o3gh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "source", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-51") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "media_item_subscriber", constraintName: "FK_ns0b5ru4gt2rre44u3tr1a8wg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-52") {
		addForeignKeyConstraint(baseColumnNames: "media_id", baseTableName: "media_metric", constraintName: "FK_x6e5xsh5ug6dhqivvp9fr0od", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-53") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "syndication_request", constraintName: "FK_okwa7indwg8twdq9w66udu4vo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-54") {
		addForeignKeyConstraint(baseColumnNames: "language_id", baseTableName: "tag", constraintName: "FK_66shlb3u4r1wxfqxmoj1m9g38", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "language", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-55") {
		addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "tag", constraintName: "FK_jsyn12seihbr7e97288w883jg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag_type", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-56") {
		addForeignKeyConstraint(baseColumnNames: "content_item_id", baseTableName: "tag_content_items", constraintName: "FK_75ttux0af04odo6nnh5noky7v", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "content_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-57") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "tag_content_items", constraintName: "FK_e0i5dmkg702neoamu9r93rytp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-58") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "user_media_item", constraintName: "FK_hn9ejgcy7s45d831t48wft76p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-59") {
		addForeignKeyConstraint(baseColumnNames: "user_likes_id", baseTableName: "user_media_item", constraintName: "FK_qcasduoot76fbly6i0h7gdfmp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-60") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_media_list", constraintName: "FK_1noaohkam5wxaicgjc8h0dfa0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-61") {
		addForeignKeyConstraint(baseColumnNames: "media_item_id", baseTableName: "user_media_list_media_item", constraintName: "FK_4uh9rp7rabkblr7eljpt16hsr", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "media_item", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-62") {
		addForeignKeyConstraint(baseColumnNames: "user_media_list_media_items_id", baseTableName: "user_media_list_media_item", constraintName: "FK_j0f3neyb4dq2t2clrs4xf8247", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user_media_list", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-63") {
		addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FK_it77eq964jhfqtu54081ebtio", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
	}

	changeSet(author: "sgates (generated)", id: "1441831936502-64") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FK_apcc8lxk2xnug8377fatvbn04", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	include file: 'prodUpdateSept2015.groovy'
    include file: 'test.groovy'
}
