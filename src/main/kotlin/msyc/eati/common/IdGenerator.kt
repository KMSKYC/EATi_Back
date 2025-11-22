package msyc.eati.common

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import java.security.SecureRandom

object IdGenerator {
    private val random = SecureRandom()
    private val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()

    fun generate(size: Int = 10): String {
        return NanoIdUtils.randomNanoId(random, alphabet, size)
    }
}
