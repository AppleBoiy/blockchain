package org.kotlin

class Transaction(
    val sender: String,
    val receiver: String,
    val amount: Double
) {
    override fun toString(): String {
        return "Transaction(sender='$sender', receiver='$receiver', amount=$amount)"
    }
}