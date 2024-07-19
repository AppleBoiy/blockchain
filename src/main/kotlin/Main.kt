package org.kotlin

fun main() {

    val blockchain = mutableListOf<Block>()
    blockchain.add(Block(0, System.currentTimeMillis(), "Genesis Block", "0"))
    blockchain[0].mineBlock(4)

    for(i in 1..10){
        blockchain.add(Block(i, System.currentTimeMillis(), "Block $i", blockchain[i-1].getHash()))
        blockchain[i].mineBlock(4)
    }

    // randomly make a transaction
    val transactions = mutableListOf(
        Transaction("Alice", "Bob", 50.0),
        Transaction("Bob", "Charlie", 25.0)
    )
    blockchain[5].transactions.addAll(transactions)
    blockchain[5].mineBlock(4)
    blockchain[1].addTransaction(Transaction("Alice", "Bob", 100.0))



    // validate the blockchain
    println("Is blockchain valid: ${Block.isValidChain(blockchain)}")

    // print the blockchain
    blockchain.forEach {
        println(it)
    }
}
