package com.example.submission1inter.upload.kamera

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.example.submission1inter.databinding.ActivityKameraBinding
import java.io.File


class KameraActivity : AppCompatActivity() {

    private val filenameFormat = "dd-MMM-yyyy"
    private lateinit var currentPhotoPath: String
    private lateinit var binding: ActivityKameraBinding
    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    private val requestCodePermission = 100
    private var getFile: File? = null


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodePermission) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan Izin Akses Media",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Akses media diizinkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@NewStoryActivity)

        if (checkSelfPermission(requiredPermissions.toString()) == PackageManager.PERMISSION_GRANTED){
            requestPermissions(requiredPermissions, requestCodePermission)
        } else {
            binding.btnCamera.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.resolveActivity(packageManager)

                createCustomTempFile(application).also {
                    val photoUri: Uri = FileProvider.getUriForFile(
                        this@NewStoryActivity,
                        "com.saifer.storyapp",
                        it
                    )
                    currentPhotoPath = it.absolutePath
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    launcherIntentCamera.launch(intent)
                }
            }

            binding.btnGallery.setOnClickListener {
                val intent = Intent()
                intent.action = ACTION_GET_CONTENT
                intent.type = "image/*"
                val picker = Intent.createChooser(intent, "Pick a Picture")
                launcherIntentGallery.launch(picker)
            }

            binding.btnAdd.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                postImage()
                val i = Intent(this@NewStoryActivity, StoryActivity::class.java)
                finish()
                startActivity(i)
            }

        }
    }


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

            Glide.with(this@NewStoryActivity)
                .load(result)
                .apply( RequestOptions().override(1000,1000))
                .into(binding.imgPreview)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@NewStoryActivity)

            getFile = myFile

            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private val timeStamp: String = SimpleDateFormat(
        filenameFormat,
        Locale.US
    ).format(System.currentTimeMillis())

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
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

    private fun postImage(){
        if (getFile != null) {

            val file = reduceFileImage(getFile as File)

            val description = binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val client = ApiConfig.getApiService().uploadImage(imageMultipart, description, "Bearer ${sessionManager.getToken()}")
            client.enqueue(object : Callback<NewStoryResponse> {
                override fun onResponse(
                    call: Call<NewStoryResponse>,
                    response: Response<NewStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error!!) {
                            Toast.makeText(this@NewStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(this@NewStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                    Toast.makeText(this@NewStoryActivity, "Upload Gagal", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
        } else {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this@NewStoryActivity, "Upload Gagal.", Toast.LENGTH_SHORT).show()
        }
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
}

