package code

import javafx.scene.control.Label

class StatusBarView {
    private var statusBar:Label = Label("status")

    fun getStatusbar():Label {
        return statusBar
    }
}