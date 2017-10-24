package com.demo.search.models

data class BulkHeaderWrapper(val update: BulkHeader)
data class BulkHeader(val _id: String, val _index: String, val _type: String)
