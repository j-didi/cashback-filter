package com.example.stream

import com.example.stream.Topics.SALES
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import java.lang.Thread.sleep
import java.util.*
import java.util.UUID.randomUUID

fun produceRandom() {
    var counter = 1
    KafkaProducer<String, String>(getProperties()).use { producer ->
        while (true) {
            val productSale = generateProductSale()
            val record = ProducerRecord(SALES, productSale.name, productSale.toJson())
            producer.send(record)
            println("Message $counter successfully!")
            ++counter
            sleep(1000)
        }
    }
}

fun produce(topic: String, productSales: List<ProductSale>) {
    KafkaProducer<String, String>(getProperties()).use { producer ->
        productSales.forEach {
            val record = ProducerRecord(topic, it.id, it.toJson())
            producer.send(record)
        }
    }
}

private fun getProperties() =
    Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    }

private fun generateProductSale() =
    ProductSale(
        id = randomUUID().toString(),
        name = listOf("iPhone", "Samsung", "Xiaomi", "Motorola").random(),
        price = (50..1000).random(),
        clientId = listOf(
            "4f2a68b8-9c3a-4d27-b1f5-6a1d9f6826e7",
            randomUUID().toString(),
            randomUUID().toString(),
            randomUUID().toString(),
            randomUUID().toString()
        ).random()
    )