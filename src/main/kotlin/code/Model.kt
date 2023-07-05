package code

import javafx.scene.image.Image
import javafx.stage.FileChooser
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class Model {

    // choose image
    fun selectImage(): File? {
        val fileChooser = FileChooser()
        fileChooser.title = "Select Image"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        )

        val selectedFile = fileChooser.showOpenDialog(null)
        if (selectedFile != null) {
            // 处理选中的图片文件
            val stream: InputStream = FileInputStream(selectedFile.absolutePath)
            //val image = Image(stream)
            println("Selected image: ${stream}")
            return selectedFile
        } else {
            // 用户取消了选择
            println("No image selected")
        }
        return null
    }
}