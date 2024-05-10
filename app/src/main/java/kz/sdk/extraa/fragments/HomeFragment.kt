package kz.sdk.extraa.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.extraa.adapters.ProductAdapter
import kz.sdk.extraa.base.BaseFragment
import kz.sdk.extraa.databinding.FragmentHomeBinding
import kz.sdk.extraa.models.Product


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var adapter: ProductAdapter

    override fun onBindView() {
        super.onBindView()
        adapter = ProductAdapter()
        with(binding) {
            productAdapter.adapter = adapter
            productAdapter.layoutManager = LinearLayoutManager(requireContext())
        }
        loadEvents()

        adapter.itemClick = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(it))
        }
    }

    private fun loadEvents() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Products")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val events = mutableListOf<Product>()
                snapshot.children.forEach {
                    val event = it.getValue(Product::class.java)
                    event?.let { events.add(it) }
                }
                adapter.submitList(events)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load events: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}