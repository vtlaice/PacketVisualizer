package edu.vt.ece.laice.packetvisualizer

import edu.vt.ece.laice.packetvisualizer.packet.SingleCommandPacket
import java.io.File

/**
 * @author Cameron Earle
 * @version 7/26/2018
 *
 */
object ImportHelper {
    fun importCsv(file: File): List<SingleCommandPacket> {
        TODO()
    }

    fun importBin(file: File): List<SingleCommandPacket> {
        val packets = arrayListOf<SingleCommandPacket>()
        val lines = file.readLines()
        lines.forEach {
            packets.add(SingleCommandPacket.reverse(it))
        }
        return packets
    }

    fun importCrc(file: File): List<SingleCommandPacket> {
        val packets = arrayListOf<SingleCommandPacket>()
        val lines = file.readLines()
        lines.forEach {
            packets.add(SingleCommandPacket.reverse(LookupTable.lookup(it.toLong())))
        }
        return packets
    }
}