package kz.sdk.extraa.fragments

import android.net.Uri
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.extraa.base.BaseFragment
import kz.sdk.extraa.databinding.FragmentAdminBinding
import kz.sdk.extraa.models.Product
import javax.inject.Inject

@AndroidEntryPoint
class AdminFragment:BaseFragment<FragmentAdminBinding>(FragmentAdminBinding::inflate) {


    private var imageUri: Uri? = null
    @Inject
    lateinit var storageReference: StorageReference

    private val imageResultLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.img.setImageURI(it)
            imageUri = it
            binding.textImg.isVisible = false
        }
    }


    override fun onBindView() {
        super.onBindView()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.uploadImg.setOnClickListener {
            selectEventImage()
        }
        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty() || binding.noteInput.text.isNullOrEmpty() || binding.caloriesInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage { imageUrl ->
                    saveEventToDatabase(binding.nameInput.text.toString(), binding.noteInput.text.toString(), imageUrl, binding.caloriesInput.text.toString().toInt())
                }
            }
        }

    }

    fun selectEventImage() {
        imageResultLauncher.launch("image/*")
    }
    private fun uploadImage(callback: (String) -> Unit) {
        imageUri?.let { uri ->
            binding.img.setImageURI(uri)
            val ref = storageReference.child(uri.lastPathSegment ?: "temp")
            ref.putFile(uri).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    callback(downloadUri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Ошибка при загрузки изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveEventToDatabase(name: String, note: String, imageUrl: String, calories:Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Products")
        val eventId = databaseReference.push().key  // Generates a unique id for the event

        val event = Product(title =  name, description = note, img = imageUrl, calories = calories)
        eventId?.let {
            databaseReference.child(it).setValue(event).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Продукт добавлен успешно!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Не получилось добавить продукт: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}