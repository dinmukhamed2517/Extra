package kz.sdk.extraa.fragments

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.extraa.R
import kz.sdk.extraa.base.BaseFragment
import kz.sdk.extraa.databinding.FragmentProfileBinding
import kz.sdk.extraa.firebase.UserDao
import javax.inject.Inject


@AndroidEntryPoint

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var userDao: UserDao
    override fun onBindView() {
        userDao.getData()
        super.onBindView()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signOutBtn.setOnClickListener {
            signOut()
        }
        binding.email.text = firebaseAuth.currentUser?.email;
        binding.activateBtn.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_adminFragment)
        }

        userDao.getDataLiveData.observe(this){
            if(it?.isAdmin == false){
                binding.adminBtn.isVisible = false
            }
            binding.name.text = it?.name
            if (it?.pictureUrl != null) {
                Glide.with(requireContext())
                    .load(it?.pictureUrl)
                    .into(binding.ava)
            } else {
                binding.ava.setImageResource(R.drawable.profile_icon)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser == null){
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

    }

    private fun signOut() {
        var alertDialog: AlertDialog? = null
        alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Выйти с аккаунта")
            .setMessage("Вы уверены что хотите выйти с аккаунта?")
            .setPositiveButton("Да") { _, _ ->
                firebaseAuth.signOut()
                alertDialog?.dismiss()
                findNavController().navigate(
                    R.id.action_profileFragment_to_loginFragment
                )
            }
            .setNegativeButton("Отмена") { _, _ ->
                alertDialog?.dismiss()
            }
            .show()
    }


}