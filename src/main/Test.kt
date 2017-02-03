package main

import logic.Data
import logic.Request
import logic.processRequest

fun runTest() {
    println(processRequest(Request("", Data(null))))
    println(processRequest(Request("Franta", Data(null))))
    println(processRequest(Request("Franta", Data("101010101"))))
}

fun main(args: Array<String>) {
    runTest()
}