package edu.vt.ece.laice.packetvisualizer

import java.io.File

/**
 * @author Cameron Earle
 * @version 7/26/2018
 *
 */
object LookupTable {
    private val lut = File(javaClass.classLoader.getResource("datacrc32.csv").toURI()).readLines().map {
        val split = it.split(",")
        Pair(split[0].toLong(), split[1])
    }

    /**
     * Looks up a CRC32 value and returns a binary string
     */
    fun lookup(crc: Long): String {
        return lut.first { it.first == crc }.second
    }
}