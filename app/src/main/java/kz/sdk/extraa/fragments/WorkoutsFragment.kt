package kz.sdk.extraa.fragments

import kz.sdk.extraa.base.BaseFragment
import kz.sdk.extraa.databinding.FragmentWorkoutsBinding

class WorkoutsFragment:BaseFragment<FragmentWorkoutsBinding>(FragmentWorkoutsBinding::inflate) {
    override fun onBindView() {
        super.onBindView()
        binding.text123.text = "Hel"
    }
}