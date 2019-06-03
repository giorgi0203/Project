package com.example.project.module.user

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import com.example.project.R
import com.example.project.module.user.dashboard.DashboardActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_add_image.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AddImageActivity : AppCompatActivity() {

    private val GALLERY = 1
    private val CAMERA = 2
    private val IMAGE_DIRECTORY = "/demonuts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)

        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        chooseImageBtn.setOnClickListener {
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
            pictureDialog.setItems(pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallery()
                    1 -> takePhotoFromCamera()
                }
            }
            pictureDialog.show()
        }

        saveImageBtn.setOnClickListener {

            var storage = FirebaseStorage.getInstance()
            var storageRef = storage.reference
            var imagesRef: StorageReference? = storageRef.child("images")

            var file = Uri.parse(imagePreview.tag.toString())

            var metadata = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()

            saveImageData("images/${file.lastPathSegment}")
            var uploadTask = storageRef.child("images/${file.lastPathSegment}").putFile(file, metadata)

            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                System.out.println("Upload is $progress% done")
            }.addOnCompleteListener {

            }.addOnPausedListener {
                System.out.println("Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                // Handle successful uploads on complete
                // ...
            }

        }

        cancelImageBtn.setOnClickListener {
            finish()
        }
    }

    private fun saveImageData(url:String) {

        var image =  Image(url,titleEditText.text.toString(),descEditText.text.toString(),AuthData.auth!!.currentUser!!.uid)
        val database = FirebaseFirestore.getInstance()

        database.collection("gallery")
            .add(image)
            .addOnSuccessListener {
                val intent = Intent(applicationContext, DashboardActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Alerter.create(this)
                    .setTitle("Error")
                    .setBackgroundColorRes(R.color.error)
                    .setText("Image upload failed")
                    .show()
            }
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    imagePreview!!.setImageBitmap(bitmap)
                    imagePreview!!.tag = contentURI

                }
                catch (e: IOException) {
                    e.printStackTrace()
//                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
//        else if (requestCode == CAMERA)
//        {
//            val thumbnail = data!!.extras!!.get("data") as Bitmap
//            imagePreview!!.setImageBitmap(thumbnail)
//            saveImage(thumbnail)
//            Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
//        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
//            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .timeInMillis).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null)
            fo.close()
//            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.absolutePath
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
}
