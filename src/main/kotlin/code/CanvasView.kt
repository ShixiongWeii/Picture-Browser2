package code

import javafx.geometry.Orientation
import javafx.scene.layout.Pane
import javafx.scene.layout.TilePane

class CanvasView {
    private val cascadeCanvas: Pane = Pane()
    private val tileCanvas:TilePane = TilePane()

    fun getCascadeView(): Pane {
        return cascadeCanvas
    }

    fun getTileCanvasView(): TilePane{
        tileCanvas.orientation = Orientation.VERTICAL
        tileCanvas.hgap = 10.0
        tileCanvas.vgap = 10.0
        return tileCanvas
    }

}