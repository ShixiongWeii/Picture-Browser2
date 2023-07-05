
import code.*
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.math.max
import kotlin.random.Random

class Main: Application() {

    private val canvasView = CanvasView()
    private val statusBarView = StatusBarView()
    private val toolbarView = ToolbarView()
    private val cascadeView = canvasView.getCascadeView()
    private val tileView = canvasView.getTileCanvasView()
    private val titleView = TitleView()
    private var contentView = cascadeView
    private val statusbar = statusBarView.getStatusbar()

    private val borderPane = BorderPane()
    private val topContainer = BorderPane()
    private val scrollPane = ScrollPane()

    private val model = Model()
    private var nowImageView: ImageView? = null

    // record button status, cascadeButton---true tileButton---false
    private var toggleButtonStatus = true

    override fun start(primaryStage: Stage?) {

        val titleBar = titleView.getTitleBar("LightBox Shixiong Wei")
        val toolbar = toolbarView.getToolBar()

        scrollPane.content = contentView


        // layout
        topContainer.top = titleBar
        topContainer.center = toolbar

        borderPane.top = topContainer
        borderPane.center = scrollPane
        borderPane.bottom = statusbar

        // action binding
        toolbarView.addButton.setOnMouseClicked { addImageAction() }
        toolbarView.deleteButton.setOnMouseClicked { delImageButtonAction() }
        toolbarView.rotateLeftButton.setOnMouseClicked { rotateLeftAction() }
        toolbarView.rotateRightButton.setOnMouseClicked { rotateRightAction() }
        toolbarView.zoomInButton.setOnMouseClicked { zoomInAction() }
        toolbarView.zoomOutButton.setOnMouseClicked { zoomOutAction() }
        toolbarView.resetButton.setOnMouseClicked { resetAction() }

        borderPane.setOnMouseClicked { event ->
            if (event.target != nowImageView) {
                if (nowImageView != null) {
                    nowImageView!!.opacity = 1.0
                }
                nowImageView = null
                statusbar.text = "count: ${contentView.children.size}"
            }
        }

        toolbarView.tileButton.setOnMouseClicked {
            if (toggleButtonStatus) {
                tileView.children.clear()
                tileView.children.addAll(contentView.children)
                contentView = tileView
                scrollPane.content = contentView
                toggleButtonStatus = !toggleButtonStatus


                //clear all translation
                contentView.children.forEach { child ->

                    child.rotate = 0.0
                    child.scaleX = 1.0
                    child.scaleY = 1.0
                }

                scrollPane.isFitToHeight = true

                // disable buttons
                toolbarView.rotateLeftButton.isDisable = true
                toolbarView.rotateRightButton.isDisable = true
                toolbarView.zoomInButton.isDisable = true
                toolbarView.zoomOutButton.isDisable = true
                toolbarView.resetButton.isDisable = true
            }
        }

        toolbarView.cascadeButton.setOnMouseClicked {
            if (!toggleButtonStatus) {
                cascadeView.children.clear()
                cascadeView.children.addAll(contentView.children)
                contentView = cascadeView
                scrollPane.content = contentView
                toggleButtonStatus = !toggleButtonStatus

                scrollPane.isFitToHeight = false

                // activate buttons
                toolbarView.rotateLeftButton.isDisable = false
                toolbarView.rotateRightButton.isDisable = false
                toolbarView.zoomInButton.isDisable = false
                toolbarView.zoomOutButton.isDisable = false
                toolbarView.resetButton.isDisable = false
            }
        }

        with(primaryStage!!) {
            title = ""
            scene = Scene(borderPane, 800.0, 600.0)
            show()
        }
    }

    // addImageButton action
    private fun addImageAction() {
        val imageFile = model.selectImage()
        if (imageFile != null) {
            //val image = Image(imageFile!!.absolutePath)
             val stream: InputStream = FileInputStream(imageFile.absolutePath)
            val image = Image(stream)
            val imageView = ImageView(image)

            imageView.fitWidth = 400.0
            imageView.fitHeight = 300.0
            imageView.isPreserveRatio = true
            imageView.isCache = true

            // Calculate random position within the scroll pane's viewport
            val maxX = scrollPane.viewportBounds.width - imageView.fitWidth
            val maxY = scrollPane.viewportBounds.height - imageView.fitHeight
            val randomX = Random.nextDouble(0.0, maxX)
            val randomY = Random.nextDouble(0.0, maxY)

            // Set the position of the image view within the content pane
            imageView.layoutX = randomX
            imageView.layoutY = randomY

            // mouse clicked position
            var sourceX = 0.0
            var sourceY = 0.0

            val dragDelta = DragDelta()

            imageView.setOnMousePressed { event ->
                // 鼠标点击位置
                sourceX = event.sceneX
                sourceY = event.sceneY

                if (toolbarView.toggleGroup.selectedToggle == toolbarView.cascadeButton) {
                    imageView.toFront()
                }

                if (nowImageView != null) {
                    nowImageView!!.opacity = 1.0
                }

                nowImageView = imageView

                // 设置选中状态并将图片设置为半透明
                imageView.opacity = 0.9

                // 设置状态栏
                //statusbar.text = File(imageView.image.url).name
                val paths = imageFile.absolutePath.split('\\')
                statusbar.text = paths[paths.size - 1]

                dragDelta.x = imageView.layoutX - event.sceneX
                dragDelta.y = imageView.layoutY - event.sceneY
            }

            imageView.setOnMouseDragged { event ->
                val offsetX = event.sceneX - sourceX
                val offsetY = event.sceneY - sourceY

                val newLayoutX = imageView.layoutX + offsetX
                val newLayoutY = imageView.layoutY + offsetY

                val maxX = scrollPane.viewportBounds.width
                val maxY = scrollPane.viewportBounds.height

                val clampedLayoutX = newLayoutX.coerceIn(-imageView.fitWidth/2, maxX)
                val clampedLayoutY = newLayoutY.coerceIn(-imageView.fitHeight/2, maxY)

                imageView.layoutX = clampedLayoutX
                imageView.layoutY = clampedLayoutY

                sourceX = event.sceneX
                sourceY = event.sceneY
            }


            contentView.children.add(imageView)

            // set images count
            statusbar.text = "count: ${contentView.children.size}"

            // Update scroll pane viewport to show the newly added image
            scrollPane.requestLayout()
            scrollPane.layout()
        }
    }

    // delImageButton action
    private fun delImageButtonAction() {
        if (nowImageView != null) {
            contentView.children.remove(nowImageView)
            statusbar.text = "count: ${contentView.children.size}"
            nowImageView = null
        } else {
            val alertDialog = Alert(Alert.AlertType.ERROR)
            alertDialog.title = "error"
            alertDialog.contentText = "no image is selected"
            alertDialog.headerText = null
            alertDialog.showAndWait()
        }
    }

    // rotate left
    private fun rotateLeftAction() {
        if (nowImageView != null) {
            nowImageView!!.rotate = nowImageView!!.rotate - 10.0
        } else {
            showNoImageSelectedError()
        }
    }

    // rotate right
    private fun rotateRightAction() {
        if (nowImageView != null) {
            nowImageView!!.rotate = nowImageView!!.rotate + 10.0
        } else {
            showNoImageSelectedError()
        }
    }

    // zoom in
    private fun zoomInAction() {
        if (nowImageView != null) {
            nowImageView!!.scaleX = nowImageView!!.scaleX + 0.25
            nowImageView!!.scaleY = nowImageView!!.scaleY + 0.25
        } else {
            showNoImageSelectedError()
        }
    }

    // zoom out
    private fun zoomOutAction() {
        if (nowImageView != null) {
            nowImageView!!.scaleX = max(nowImageView!!.scaleX - 0.25, 0.25)
            nowImageView!!.scaleY = max(nowImageView!!.scaleY - 0.25, 0.25)
        } else {
            showNoImageSelectedError()
        }
    }

    private fun resetAction() {
        if (nowImageView != null) {
            nowImageView!!.scaleX = 1.0
            nowImageView!!.scaleY = 1.0
            nowImageView!!.rotate = 0.0
        } else {
            showNoImageSelectedError()
        }
    }

    private fun showNoImageSelectedError() {
        val alertDialog = Alert(Alert.AlertType.ERROR)
        alertDialog.title = "Error"
        alertDialog.contentText = "No image is selected"
        alertDialog.headerText = null
        alertDialog.showAndWait()
    }

    inner class DragDelta {
        var x: Double = 0.0
        var y: Double = 0.0
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
