package `in`.co.androidapp.ecozoom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import `in`.co.androidapp.ecozoom.databinding.FragmentCameraBinding
import `in`.co.androidapp.ecozoom.databinding.FragmentOptionsBinding
import java.net.URLEncoder

class OptionsFragment : Fragment() {
    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = OptionsFragmentArgs.fromBundle(requireArguments()).imageUrl
        binding.imageView.load(imageUrl)
        getData(imageUrl)

        binding.button.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun fillRecyclerView(list: List<Option>) {
        val adapter = OptionAdapter(list, findNavController())

        // Setting the Adapter with the recyclerview
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    fun getData(image_url: String) {
        val rsp = listOf(
            Option("https://s3-ap-southeast-2.amazonaws.com/wc-prod-pim/JPEG_1000x1000/BIOBCK8GS_biopak_single_wall_coffee_cups_8oz_kraft_1000_box.jpg","Bioback Single Wall Coffee Cups","https://www.dulux.com.au/colours/details/Specialty_000001"),
            Option("https://marketplacer.imgix.net/TT/I9CKdd4trLQxRF-93LZs5oi3c.jpg?auto=format&fm=pjpg&fit=clip&w=635&h=508&s=464bec02a301d0eb9982c28ddc2eedf9","Ripple Wall Coffee Cups","https://www.dulux.com.au/colours/details/Specialty_000001"),
            Option("https://au.shop.allpressespresso.com/cdn/shop/products/ReusableCup_large.png?v=1578631777","Allpress Reuseable Coffee Cup","https://www.dulux.com.au/colours/details/Specialty_000001"),
            Option("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSryCew3FaemHNh6lzqBbK2jc6brZR9YzLAeQ&usqp=CAU","Black Paper Coffee Cups","https://www.dulux.com.au/colours/details/Specialty_000001"),
        )


        // Display the first 500 characters of the response string.
        fillRecyclerView(rsp)

        val imgUrl = URLEncoder.encode(image_url, "utf-8")
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://128.250.0.196:5000/image_search?url=${imgUrl}"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->

                val list = Gson().fromJson<List<Option>>(response, List::class.java)
                // Display the first 500 characters of the response string.
                fillRecyclerView(list)
            },
            { exception ->
                Log.e("Errr", "That didnt work")
                exception.printStackTrace()
            })
        queue.add(stringRequest)

    }
}