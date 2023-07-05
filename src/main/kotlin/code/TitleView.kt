package code

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox

class TitleView {

    fun getTitleBar(title: String): HBox {
        val titleLabel = Label(title)
        titleLabel.style = "-fx-font-size: 14px; -fx-font-weight: bold;"

        val titleBar = HBox(titleLabel)
        titleBar.alignment = Pos.CENTER
        titleBar.style = "-fx-background-color: lightgray; -fx-padding: 8px;"

        return titleBar
    }
}