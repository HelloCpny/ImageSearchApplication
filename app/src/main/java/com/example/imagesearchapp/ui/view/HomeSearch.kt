package com.example.imagesearchapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesearchapp.databinding.FragmentHomeSearchBinding
import com.example.imagesearchapp.ui.adapter.ImageSearchAdapter
import com.example.imagesearchapp.ui.viewmodel.ImageSearchViewModel


class HomeSearch : Fragment(){
    private var _binding : FragmentHomeSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageSearchViewModel: ImageSearchViewModel
    private lateinit var imageSearchAdapter: ImageSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageSearchViewModel = (activity as MainActivity).imageSearchViewModel

        setRecyclerView()
        searchBooks()


        imageSearchViewModel.searchResult.observe(viewLifecycleOwner){
            val images = it.documents
            imageSearchAdapter.submitList(images)
        }
    }
    private fun setRecyclerView(){
        imageSearchAdapter = ImageSearchAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(false)
            layoutManager = GridLayoutManager(this.context, 3)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = imageSearchAdapter
        }
    }

    private fun searchBooks(){
        var startTime = System.currentTimeMillis()
        var endTime : Long

        binding.searchBoxContainer.searchEditText.addTextChangedListener {
            text : Editable? ->
            endTime = System.currentTimeMillis()
            if(endTime - startTime >= 1000L){
                text?.let {
                    val query = it.toString().trim()
                    if(query.isNotEmpty()){
                        imageSearchViewModel.searchImage(query)
                    }
                }
            }
            startTime = endTime
        }

        if(!binding.searchBoxContainer.searchEditText.text.toString().equals("")){
            binding.searchBoxContainer.clearSearchQuery.visibility
        }
    }
    override fun onDestroy() {
        _binding =null
        super.onDestroy()
    }
}