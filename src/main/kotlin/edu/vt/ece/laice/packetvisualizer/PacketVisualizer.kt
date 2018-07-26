package edu.vt.ece.laice.packetvisualizer

import edu.vt.ece.laice.packetvisualizer.packet.SingleCommandPacket
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Cameron Earle
 * @version 7/26/2018
 *
 */

fun main(args: Array<String>) {
    Application.launch(PacketVisualizer::class.java, *args)
}

class PacketVisualizer: Application() {
    override fun start(stage: Stage) {
        val root: Parent = FXMLLoader.load(javaClass.classLoader.getResource("main_view.fxml"))
        val scene = Scene(root)
        stage.title = "LAICE Packet Visualizer"
        stage.scene = scene
        stage.show()
    }
}