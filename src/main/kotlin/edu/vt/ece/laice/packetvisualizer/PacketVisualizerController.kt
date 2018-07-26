package edu.vt.ece.laice.packetvisualizer

import edu.vt.ece.laice.packetvisualizer.packet.BinEnum
import edu.vt.ece.laice.packetvisualizer.packet.SingleCommandPacket
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import java.io.File

/**
 * @author Cameron Earle
 * @version 7/26/2018
 *
 */

class PacketVisualizerController {
    //Controls
    @FXML lateinit var importCsvButton: Button
    @FXML lateinit var importBinButton: Button
    @FXML lateinit var importCrcButton: Button
    @FXML lateinit var prevButton: Button
    @FXML lateinit var nextButton: Button

    //Bin labels
    @FXML lateinit var binStartWordLiib: Label
    @FXML lateinit var binLiibMode: Label
    @FXML lateinit var binOpMode: Label
    @FXML lateinit var binStartWordRpa: Label
    @FXML lateinit var binStepSizeRpa: Label
    @FXML lateinit var binPpsRpa: Label
    @FXML lateinit var binZeroPaddingRpa: Label
    @FXML lateinit var binRg2ModeRpa: Label
    @FXML lateinit var binSweepModeRpa: Label
    @FXML lateinit var binStartWordSneupi: Label
    @FXML lateinit var binZeroPaddingSneupi: Label
    @FXML lateinit var binHvStatusSneupi: Label
    @FXML lateinit var binEmissionModeSneupi: Label
    @FXML lateinit var binFilamentSelectLinas: Label
    @FXML lateinit var binGridBiasOnOffLinas: Label
    @FXML lateinit var binGridBiasSettingLinas: Label
    @FXML lateinit var binCollectorGainStateLinas: Label
    @FXML lateinit var binFilamentOnOffLinas: Label
    @FXML lateinit var binEndWordLinas: Label

    @FXML lateinit var crcTextField: TextField


    private fun openFileChooserLock() {
        importCsvButton.isDisable = true
        importBinButton.isDisable = true
        importCrcButton.isDisable = true
    }

    private fun openFileChooserUnlock() {
        importCsvButton.isDisable = false
        importBinButton.isDisable = false
        importCrcButton.isDisable = false
    }

    private fun fileChooser(): File? {
        openFileChooserLock()

        val fc = FileChooser()
        val file = fc.showOpenDialog(null)

        openFileChooserUnlock()
        return file
    }

    private fun load(data: List<SingleCommandPacket>) {
        packets.clear()
        packets.addAll(data)

        if (packets.size > 0) {
            currentIndex = 0
        }

        updateInterface()
    }

    @FXML fun onImportCsv(e: ActionEvent) {
        val f = fileChooser()
        if (f != null) {
            val csv = ImportHelper.importCsv(f)
            load(csv)
        }
    }

    @FXML fun onImportBin(e: ActionEvent) {
        val f = fileChooser()
        if (f != null) {
            val bin = ImportHelper.importBin(f)
            load(bin)
        }
    }

    @FXML fun onImportCrc(e: ActionEvent) {
        val f = fileChooser()
        if (f != null) {
            val crc = ImportHelper.importCrc(f)
            load(crc)
        }
    }

    @FXML fun onNext(e: ActionEvent) {
        currentIndex++
        updateInterface()
    }

    @FXML fun onPrev(e: ActionEvent) {
        currentIndex--
        updateInterface()
    }

    private val packets = arrayListOf<SingleCommandPacket>()
    private var currentIndex = -1

    /**
     * Updates a binary label with the appropriately formatted binary string
     */
    private fun updateBinLabel(label: Label, command: BinEnum) {
        updateBinLabel(label, command.bin)
    }

    private fun updateBinLabel(label: Label, bin: String) {
        val bits = bin.length
        val sb = StringBuilder()

        //Split 6 bits into
        //000 000
        when (bits) {
            6 -> {
                sb.append(bin.substring(0, 3))
                sb.append(" ")
                sb.append(bin.substring(3, 6))
            }

        //Split 16 bits into
        //00000000 00000000
            16 -> {
                sb.append(bin.substring(0, 8))
                sb.append(" ")
                sb.append(bin.substring(8, 16))
            }
            else -> {
                sb.append(bin)
            }
        }

        sb.append(" ") //Trailing space
        label.text = sb.toString()
    }

    private fun updateInterface() {
        //Update buttons
        nextButton.isDisable = currentIndex >= packets.lastIndex || currentIndex == -1
        prevButton.isDisable = currentIndex <= 0

        //Update binary view
        if (currentIndex != -1) {
            val p = packets[currentIndex]

            updateBinLabel(binStartWordLiib, p.commandLIIB.startWord)
            updateBinLabel(binLiibMode, p.commandLIIB.liibMode)
            updateBinLabel(binOpMode, p.commandLIIB.opMode)

            updateBinLabel(binStartWordRpa, p.commandRPA.startWord)
            updateBinLabel(binStepSizeRpa, p.commandRPA.stepSize)
            updateBinLabel(binPpsRpa, p.commandRPA.pointsPerSweep)
            updateBinLabel(binZeroPaddingRpa, p.commandRPA.zeroPadding)
            updateBinLabel(binRg2ModeRpa, p.commandRPA.rg2Mode)
            updateBinLabel(binSweepModeRpa, p.commandRPA.sweepMode)

            updateBinLabel(binStartWordSneupi, p.commandSNeuPI.startWord)
            updateBinLabel(binZeroPaddingSneupi, p.commandSNeuPI.zeroPadding)
            updateBinLabel(binHvStatusSneupi, p.commandSNeuPI.hvStatus)
            updateBinLabel(binEmissionModeSneupi, p.commandSNeuPI.emissionMode)

            updateBinLabel(binFilamentSelectLinas, p.commandLINAS.filamentSelect)
            updateBinLabel(binGridBiasOnOffLinas, p.commandLINAS.gridBiasOnOff)
            updateBinLabel(binGridBiasSettingLinas, p.commandLINAS.gridBiasSetting)
            updateBinLabel(binCollectorGainStateLinas, p.commandLINAS.collectorGainState)
            updateBinLabel(binFilamentOnOffLinas, p.commandLINAS.filamentOnOff)
            updateBinLabel(binEndWordLinas, p.commandLINAS.endWord)

            crcTextField.text = p.crc32().toString()
        }
    }
}