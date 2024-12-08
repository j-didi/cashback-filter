package com.example.cross

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.util.*

private val jedis = Jedis("localhost", 6379)
private var clients: List<String>? = null
private var key = "client-subscriptions"

fun getClients(shouldUpdate: Boolean = true): List<String> {
    if (clients != null && !shouldUpdate) {
        return clients!!
    }

    clients = jedis.smembers(key).toList()
    return clients!!
}

fun listenToSubscriptionEvents() {
    val jedisPubSub = object : JedisPubSub() {
        override fun onPMessage(pattern: String, channel: String, message: String) {
            clients = getClients(true)
        }
    }

    Thread { Jedis("localhost", 6379).psubscribe(jedisPubSub, "__keyspace@0__:*") }.start()
}

fun save(client: String) {
    jedis.sadd(key, client)
}