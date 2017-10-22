package com.demo.search.models

data class SwapAliasRequest (val actions: Map<String, Map<String, String>>) {
    companion object {
        fun build(removeIndex: String, removeAlias: String, addIndex: String, addAlias: String) = 
            SwapAliasRequest(hashMapOf(
                "remove" to hashMapOf("index" to removeIndex, "alias" to removeAlias),
                "add" to hashMapOf("index" to addIndex, "alias" to addAlias)
            ))
    }
}
