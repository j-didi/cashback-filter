package com.example.stream

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val producer = launch(Dispatchers.IO) { produceRandom() }
    val consumer = launch(Dispatchers.IO) { consume() }

    joinAll(producer, consumer)
}