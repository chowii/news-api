package com.example.news.sources.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.news.sources.databinding.FragmentSourcesBinding
import com.example.news.sources.presentation.SourcesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourcesFragment : Fragment() {

    private val sourcesViewModel: SourcesViewModel by viewModels()
    private val controller: SourcesController by lazy { SourcesController(sourcesViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sourcesBinding = FragmentSourcesBinding.inflate(inflater, container, false).apply {
            recyclerView.setController(controller)
            lifecycleOwner = viewLifecycleOwner
            viewModel = sourcesViewModel
            handler = sourcesViewModel
        }
        return sourcesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sourcesViewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState.data?.let {
                controller.setData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sourcesViewModel.fetchSourceList()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SourcesFragment()
    }
}