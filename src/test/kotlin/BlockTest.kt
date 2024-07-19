package org.kotlin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlockTest {

    @Test
    fun testCalculateHash() {
        val block = Block(0, System.currentTimeMillis(), "Genesis Block", "0")
        val hash = block.getHash()

        // Check that the hash is a valid SHA-256 hash (64 hex characters)
        assertEquals(64, hash.length)
        hash.forEach {
            assert(it in '0'..'9' || it in 'a'..'f')
        }
    }

    @Test
    fun testMineBlock() {
        val difficulty = 4
        val block = Block(1, System.currentTimeMillis(), "Some Data", "0")

        // Mine the block with the specified difficulty
        block.mineBlock(difficulty)

        val minedHash = block.getHash()

        // Check that the mined hash starts with the required number of zeroes
        assertEquals("0".repeat(difficulty), minedHash.substring(0, difficulty))
    }

    @Test
    fun testBlockHashChangesWithNonce() {
        val block = Block(2, System.currentTimeMillis(), "Test Data", "0")
        val initialHash = block.getHash()

        // Increment nonce and calculate a new hash
        block.mineBlock(1) // This will change the nonce and the hash
        val newHash = block.getHash()

        // Ensure that the new hash is different from the initial hash
        assertNotEquals(initialHash, newHash)
    }

    @Test
    fun testBlockProperties() {
        val index = 3
        val timestamp = System.currentTimeMillis()
        val data = "Sample Data"
        val previousHash = "abcd1234"
        val block = Block(index, timestamp, data, previousHash)

        // Validate block properties
        assertEquals(index, block.index)
        assertEquals(timestamp, block.timestamp)
        assertEquals(data, block.data)
        assertEquals(previousHash, block.previousHash)
    }

    @Test
    fun testIsValidChain() {
        val blockchain = mutableListOf<Block>()
        blockchain.add(Block(0, System.currentTimeMillis(), "Genesis Block", "0"))
        blockchain[0].mineBlock(4)

        for (i in 1..10) {
            blockchain.add(Block(i, System.currentTimeMillis(), "Block $i", blockchain[i - 1].getHash()))
            blockchain[i].mineBlock(4)
        }

        // Validate the blockchain
        assert(Block.isValidChain(blockchain))
    }

    @Test
    fun testInvalidChain() {
        val blockchain = mutableListOf<Block>()
        blockchain.add(Block(0, System.currentTimeMillis(), "Genesis Block", "0"))
        blockchain[0].mineBlock(4)

        for (i in 1..10) {
            blockchain.add(Block(i, System.currentTimeMillis(), "Block $i", blockchain[i - 1].getHash()))
            blockchain[i].mineBlock(4)
        }

        // Modify the data of a block to invalidate the chain
        blockchain[5].data = "Modified Data"

        // Validate the blockchain
        assert(!Block.isValidChain(blockchain))
    }
}
