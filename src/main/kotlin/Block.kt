package org.kotlin
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Block(
    var index: Int,
    var timestamp: Long,
    var data: String,
    var previousHash: String,
    var transactions: MutableList<Transaction> = mutableListOf()
) {

    private var hash: String
    private var nonce: Int = 0

    init {
        hash = calculateHash()
    }

    private fun calculateHash(): String {
        val transactionsData = transactions.joinToString { "${it.sender}:${it.receiver}:${it.amount}" }
        return try {
            val input = (
                    index.toString() +
                            timestamp.toString() +
                            data +
                            previousHash +
                            nonce.toString() +
                            transactionsData
                    ).toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val hashBytes = md.digest(input)
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("SHA-256 algorithm not found")
        }
    }

    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
        hash = calculateHash() // Recalculate hash as the block content has changed
    }

    fun mineBlock(difficulty: Int) {
        val target = "0".repeat(difficulty)
        while (!hash.startsWith(target)) {
            nonce++
            hash = calculateHash()
        }
        println("Block mined: $hash")
    }

    fun getHash(): String {
        return hash
    }

    override fun toString(): String {
        return """
            |Block:
            |  Index: $index
            |  Timestamp: $timestamp
            |  Data: $data
            |  Previous Hash: $previousHash
            |  Hash: $hash
            |  Transactions:
            |${transactions.joinToString("\n") { "    $it" }}
        """.trimMargin()
    }

    companion object {
        fun isValidChain(blockchain: List<Block>): Boolean {
            for (i in 1 until blockchain.size) {
                val currentBlock = blockchain[i]
                val previousBlock = blockchain[i - 1]

                if (currentBlock.getHash() != currentBlock.calculateHash()) {
                    println("Current Hashes not equal")
                    return false
                }

                if (previousBlock.getHash() != currentBlock.previousHash) {
                    println("Previous Hashes not equal")
                    return false
                }
            }
            return true
        }
    }
}