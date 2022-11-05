package com.example.submission1inter.upload

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission1inter.ValidasiLoginViewModel
import com.example.submission1inter.akun.register.RegisterData
import com.example.submission1inter.data.api.ApiConfig
import com.example.submission1inter.databinding.ActivityDetailStoryBinding
import com.example.submission1inter.databinding.ActivityUploadBinding
import com.example.submission1inter.model.GetStoryViewModel
import com.example.submission1inter.model.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var validasiLoginViewModel: ValidasiLoginViewModel
    private lateinit var binding: ActivityUploadBinding

    private val uploadViewModel : UploadViewModel by viewModels()

    private lateinit var uploadData: uploadData

    private var getFile: File? = null
    private val FILENAME_FORMAT = "dd-MMM-yyyy"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validasiLoginViewModel = obtainViewModel(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        playAnimation()
        binding.btnKamera.setOnClickListener { ambilFoto() }
        binding.btnGaleri.setOnClickListener { ambilGaleri() }
        binding.btnUpload.setOnClickListener { upFoto() }
    }

    private fun ambilFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.example.submission1inter", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun ambilGaleri() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val picker = Intent.createChooser(intent, "Pick a Picture")
        launcherIntentGallery.launch(picker)
    }

//    private fun upFoto(){
//
//    }
    private fun upFoto() {
        if (getFile != null) {

            val file = reduceFileImage(getFile as File)
            val description = binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val client = ApiConfig.getApiService().upload(imageMultipart, description, "bearer ${validasiLoginViewModel.getToken()}")
            client.enqueue(object : Callback<UploadStoryResponse> {
                override fun onResponse(
                    call: Call<UploadStoryResponse>,
                    response: Response<UploadStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error!!) {
                            Toast.makeText(this@UploadActivity, responseBody.message, Toast.LENGTH_SHORT).show()
//                            binding.progressBar.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(this@UploadActivity, response.message(), Toast.LENGTH_SHORT).show()
//                        binding.progressBar.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                    Toast.makeText(this@UploadActivity, "Upload Foto Gagal", Toast.LENGTH_SHORT).show()
//                    binding.progressBar.visibility = View.GONE
                }
            })
        } else {
//            binding.progressBar.visibility = View.GONE
            Toast.makeText(this@UploadActivity, "Upload Foto Gagal.", Toast.LENGTH_SHORT).show()
        }
    }


    private lateinit var currentPhotoPath: String

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )

            Glide.with(this@UploadActivity)
                .load(result)
                .apply( RequestOptions().override(1000,1000))
                .into(binding.imageView)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@UploadActivity)

            getFile = myFile

            binding.imageView.setImageURI(selectedImg)
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    // Untuk kasus Intent Camera
    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak Ada Izin.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }


    private  fun playAnimation(){

        val kamera = ObjectAnimator.ofFloat(binding.btnKamera, View.ALPHA, 1f).setDuration(1000)
        val galeri = ObjectAnimator.ofFloat(binding.btnGaleri, View.ALPHA, 1f).setDuration(1000)
        val add = ObjectAnimator.ofFloat(binding.btnUpload, View.ALPHA, 1f).setDuration(1000)

       AnimatorSet().apply {
           playTogether(kamera,galeri,add)
           start()
        }
    }


    private fun obtainViewModel(activity: AppCompatActivity) : ValidasiLoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[ValidasiLoginViewModel::class.java]
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}