package kz.sdk.extraa.fragments

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.extraa.R
import kz.sdk.extraa.base.BaseFragment
import kz.sdk.extraa.databinding.FragmentProductDetailsBinding
import kz.sdk.extraa.firebase.UserDao
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment: BaseFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate) {
    private val args:ProductDetailsFragmentArgs by navArgs()
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onBindView() {
        super.onBindView()
        val product = args.product
        with(binding){
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            title.text = product.title
            description.text = product.description
            price.text = product.calories.toString()+" Калорий"
            Glide.with(requireContext())
                .load(product.img)
                .placeholder(R.drawable.placeholder_event)
                .into(imageView3)
            addBtn.setOnClickListener {
                userDao.saveProductToList(product)
                showCustomDialog("Успешно" , "Добавлено в корзину")
            }
        }
    }
}