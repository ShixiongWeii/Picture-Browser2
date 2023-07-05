package code

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.FileInputStream
import java.io.InputStream

class ToolbarView {

    val addButton: Button = Button("Add Image")
    val deleteButton: Button = Button("Delete Image")
    val rotateLeftButton: Button = Button("Rotate Left")
    val rotateRightButton: Button = Button("Rotate Right")
    val zoomInButton: Button = Button("Zoom In")
    val zoomOutButton: Button = Button("Zoom Out")
    val resetButton: Button = Button("Reset")
    val cascadeButton: ToggleButton = ToggleButton("Cascade")
    val tileButton: ToggleButton = ToggleButton("Tile")
    var toggleGroup: ToggleGroup = ToggleGroup()

    fun getToolBar(): ToolBar {
        cascadeButton.toggleGroup = toggleGroup
        tileButton.toggleGroup = toggleGroup
        toggleGroup.selectToggle(cascadeButton)

        addButton.graphic = getButtonImageView("./src/main/resources/add.png")
        deleteButton.graphic = getButtonImageView("./src/main/resources/delete.png")
        rotateLeftButton.graphic = getButtonImageView("./src/main/resources/rotate_left.jpg")
        rotateRightButton.graphic = getButtonImageView("./src/main/resources/rotate_right.jpg")
        zoomInButton.graphic = getButtonImageView("./src/main/resources/zoom_in.png")
        zoomOutButton.graphic = getButtonImageView("./src/main/resources/zoom_out.jpg")
        resetButton.graphic = getButtonImageView("./src/main/resources/reset.jpg")
        cascadeButton.graphic = getButtonImageView("./src/main/resources/cascade.png")
        tileButton.graphic = getButtonImageView("./src/main/resources/tile.jpg")



        val toolBar = ToolBar(
            addButton, deleteButton, rotateLeftButton, rotateRightButton, zoomInButton, zoomOutButton,
            resetButton, cascadeButton, tileButton
        )
        toolBar.padding = Insets(10.0, 10.0, 0.0, 10.0)
        return toolBar
    }

    private fun getButtonImageView(imagePath: String): ImageView {
        val stream: InputStream = FileInputStream(imagePath)
        val image = Image(stream)
        val imageView = ImageView(image)
        imageView.fitHeight = 20.0
        imageView.fitWidth = 20.0
        return imageView
    }

}