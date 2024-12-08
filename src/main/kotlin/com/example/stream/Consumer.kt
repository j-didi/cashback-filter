package com.example.stream

import com.example.stream.Topics.SALES
import com.example.stream.Topics.SALES_REWARDS
import com.example.cross.getClients
import com.google.gson.Gson
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.*

private val clients = getClients()
private val filter = BloomFilter(clients)
private val gson = Gson()

fun consume() {
    KafkaConsumer<String, String>(getProperties()).use { consumer ->
        consumer.subscribe(listOf(SALES))
        while (true) {
            val records = consumer.poll(Duration.ofMillis(1000))
            val items: MutableList<ProductSale> = mutableListOf()

            for (record in records) {
                val productSale = gson.fromJson(record.value(), ProductSale::class.java)
                items.add(productSale)
            }

            if (items.size > 0) {
                val filteredItems = filter.contains(items)

                if(filteredItems.isNotEmpty()) {
                    produce(SALES_REWARDS, filteredItems)
                }

            }
        }
    }
}

private fun getProperties() =
    Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
        put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group")
    }