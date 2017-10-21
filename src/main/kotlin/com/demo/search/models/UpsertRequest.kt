package com.demo.search.models

data class UpsertRequest(val doc: Movie, val doc_as_upsert: Boolean = true)
