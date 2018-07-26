package edu.vt.ece.laice.packetvisualizer.packet

import java.util.zip.CRC32

data class SingleCommandPacket(val commandLIIB: CommandLIIB,
                               val commandRPA: CommandRPA = CommandRPA(),
                               val commandSNeuPI: CommandSNeuPI = CommandSNeuPI(),
                               val commandLINAS: CommandLINAS = CommandLINAS()) {
    companion object {
            val NULL = SingleCommandPacket(
                    CommandLIIB.NULL,
                    CommandRPA.NULL,
                    CommandSNeuPI.NULL,
                    CommandLINAS.NULL
            )


        private const val START_LIIB = 0
        private const val START_RPA = START_LIIB + 8 + 5 + 3
        private const val START_SNEUPI = START_RPA + 8 + 16 + 8 + 5 + 1 + 2
        private const val START_LINAS = START_SNEUPI + 8 + 5 + 1 + 2
        private const val END_LINAS = START_LINAS + 1 + 1 + 4 + 6 + 4 + 8

        fun reverse(bin: String): SingleCommandPacket {
            return SingleCommandPacket(
                    CommandLIIB(
                            StartWordLIIB.values().first { it.bin == bin.substring(START_LIIB, START_LIIB + 8)},
                            LIIBMode.values().first { it.bin == bin.substring(START_LIIB + 8, START_LIIB + 8 + 5)},
                            OpMode.values().first { it.bin == bin.substring(START_LIIB + 8 + 5, START_RPA)}
                    ),
                    CommandRPA(
                            StartWordRPA.values().first { it.bin == bin.substring(START_RPA, START_RPA + 8) },
                            StepSizeRPA.values().first { it.bin == bin.substring(START_RPA + 8, START_RPA + 8 + 16)},
                            PointPerSweepRPA.values().first { it.bin == bin.substring(START_RPA + 8 + 16, START_RPA + 8 + 16 + 8)},
                            bin.substring(START_RPA + 8 + 16 + 8, START_RPA + 8 + 16 + 8 + 5),
                            RG2ModeRPA.values().first { it.bin == bin.substring(START_RPA + 8 + 16 + 8 + 5, START_RPA + 8 + 16 + 8 + 5 + 1)},
                            SweepModeRPA.values().first { it.bin == bin.substring(START_RPA + 8 + 16 + 8 + 5 + 1, START_SNEUPI)}
                    ),
                    CommandSNeuPI(
                            StartWordSNeuPI.values().first { it.bin == bin.substring(START_SNEUPI, START_SNEUPI + 8)},
                            bin.substring(START_SNEUPI + 8, START_SNEUPI + 8 + 5),
                            HVStatusSNeuPI.values().first { it.bin == bin.substring(START_SNEUPI + 8 + 5, START_SNEUPI + 8 + 5 + 1)},
                            EmissionModeSNeuPI.values().first { it.bin == bin.substring(START_SNEUPI + 8 + 5 + 1, START_LINAS)}
                    ),
                    CommandLINAS(
                            FilamentSelectLINAS.values().first { it.bin == bin.substring(START_LINAS, START_LINAS + 1)},
                            GridBiasOnOffLINAS.values().first { it.bin == bin.substring(START_LINAS + 1, START_LINAS + 1 + 1)},
                            GridBiasSettingLINAS.values().first { it.bin == bin.substring(START_LINAS + 1 + 1, START_LINAS + 1 + 1 + 4)},
                            CollectorGainStateLINAS.values().first { it.bin == bin.substring(START_LINAS + 1 + 1 + 4, START_LINAS + 1 + 1 + 4 + 6)},
                            FilamentOnOffLINAS.values().first { it.bin == bin.substring(START_LINAS + 1 + 1 + 4 + 6, START_LINAS + 1 + 1 + 4 + 6 + 4)},
                            EndWordLINAS.values().first { it.bin == bin.substring(START_LINAS + 1 + 1 + 4 + 6 + 4, END_LINAS)}
                    )
            )
        }
    }

    fun bin() = commandLIIB.bin() + commandRPA.bin() + commandSNeuPI.bin() + commandLINAS.bin()

    fun crc32(): Long {
        val crc = CRC32()
        crc.update(bin().toByteArray())
        return crc.value
    }
}