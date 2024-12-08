package com.example.stream

import kotlinx.serialization.Serializable

@Serializable
data class ProductSale(
    val id: String,
    val name: String,
    val price: Int,
    val clientId: String
) {
    fun toJson(): String {
        return """
            {
                "id": "$id",
                "name": "$name",
                "price": $price,
                "clientId": "$clientId"
            }
        """.trimIndent()
    }
}
