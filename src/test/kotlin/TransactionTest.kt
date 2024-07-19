import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.kotlin.Transaction
import org.kotlin.Block

class BlockTest {
    @Test
    fun testCalculateHashWithTransactions() {
        val transactions = mutableListOf(
            Transaction("Alice", "Bob", 50.0),
            Transaction("Bob", "Charlie", 25.0)
        )
        val block = Block(0, System.currentTimeMillis(), "Genesis Block", "0", transactions)
        val hash = block.getHash()

        // Check that the hash is a valid SHA-256 hash (64 hex characters)
        assertEquals(64, hash.length)
        hash.forEach {
            assert(it in '0'..'9' || it in 'a'..'f')
        }
    }

    @Test
    fun testAddTransaction() {
        val block = Block(1, System.currentTimeMillis(), "Block Data", "0")

        val initialHash = block.getHash()

        // Add a transaction
        val transaction = Transaction("Alice", "Bob", 100.0)
        block.addTransaction(transaction)

        val newHash = block.getHash()

        // Ensure that the new hash is different from the initial hash
        assertNotEquals(initialHash, newHash)
    }

    @Test
    fun testMineBlockWithTransactions() {
        val transactions = mutableListOf(
            Transaction("Alice", "Bob", 50.0),
            Transaction("Bob", "Charlie", 25.0)
        )
        val block = Block(2, System.currentTimeMillis(), "Block Data", "0", transactions)
        val difficulty = 4

        // Mine the block with the specified difficulty
        block.mineBlock(difficulty)

        val minedHash = block.getHash()

        // Check that the mined hash starts with the required number of zeroes
        assertEquals("0".repeat(difficulty), minedHash.substring(0, difficulty))
    }

    @Test
    fun testIsValidChain() {
        val genesisBlock = Block(0, System.currentTimeMillis(), "Genesis Block", "0")
        val secondBlock = Block(1, System.currentTimeMillis(), "Second Block", genesisBlock.getHash())
        val thirdBlock = Block(2, System.currentTimeMillis(), "Third Block", secondBlock.getHash())

        val blockchain = listOf(genesisBlock, secondBlock, thirdBlock)

        // Validate the blockchain
        assertEquals(true, Block.Companion.isValidChain(blockchain))
    }

    @Test
    fun testIsInvalidChain() {
        val genesisBlock = Block(0, System.currentTimeMillis(), "Genesis Block", "0")
        val secondBlock = Block(1, System.currentTimeMillis(), "Second Block", genesisBlock.getHash())
        val thirdBlock = Block(2, System.currentTimeMillis(), "Third Block", secondBlock.getHash())

        val tamperedBlock = Block(2, System.currentTimeMillis(), "Tampered Block", secondBlock.getHash())

        val blockchain = listOf(genesisBlock, secondBlock, thirdBlock, tamperedBlock)

        // Validate the blockchain
        assertEquals(false, Block.Companion.isValidChain(blockchain))
    }

}
