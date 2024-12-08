package com.example.stream

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.math.abs

class BloomFilter(clients: List<String>) {
    companion object {
        const val SIZE = 1_000_000
        const val HASH_COUNT = 7
    }

    private val bitArray = BooleanArray(SIZE)

    init {
        clients.forEach { add(it) }
    }

    fun contains(values: List<ProductSale>): List<ProductSale> {
        return values.filter { containsElement(it.clientId) }
    }

    private fun hash(value: String, seed: Int): Int {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update((value + seed).toByteArray(StandardCharsets.UTF_8))
        val hashBytes = messageDigest.digest()
        return abs(hashBytes.fold(0) { acc, byte -> acc * 31 + byte.toInt() }) % SIZE
    }

    private fun add(value: String) {
        for (i in 0 until HASH_COUNT) {
            val index = hash(value, i)
            bitArray[index] = true
        }
    }

    private fun containsElement(value: String): Boolean {
        for (i in 0 until HASH_COUNT) {
            val index = hash(value, i)
            if (!bitArray[index]) {
                return false
            }
        }
        return true
    }

}
