package uz.uicgroup.presentation.screen.edit.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.uicgroup.R
import uz.uicgroup.databinding.PagerDictionaryBinding
import uz.uicgroup.domain.model.WordData
import uz.uicgroup.presentation.screen.edit.adapter.DictionaryAdapter
import uz.uicgroup.presentation.screen.edit.dialog.BottomDialog
import uz.uicgroup.presentation.screen.edit.dialog.DeleteDialog
import uz.uicgroup.presentation.screen.edit.pager.viewmodel.DictionaryViewModel
import uz.uicgroup.presentation.screen.edit.pager.viewmodel.impl.DictionaryViewModelImpl
import uz.uicgroup.utils.extension.*


@AndroidEntryPoint
class DictionaryPager : Fragment(R.layout.pager_dictionary), SearchView.OnQueryTextListener {
    private val binding by viewBinding(PagerDictionaryBinding::bind)
    private val viewModel: DictionaryViewModel by viewModels<DictionaryViewModelImpl>()
    private val adapter: DictionaryAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        DictionaryAdapter(
            requireContext(), lifecycleScope
        )
    }


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHistory()
        listDic.adapter = adapter
        listDic.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        adapter.setOnItemClickListener {
            hideKeyboard()
            if (viewModel.click) viewModel.getById(it)
            else viewModel.getWords(it)
        }
        searchView.setOnQueryTextListener(this@DictionaryPager)
        searchView.setOnCloseListener {
            hideKeyboard()
            return@setOnCloseListener true
        }
        historyImage2.setOnClickListener {
            val dialog = DeleteDialog()
            dialog.setOkListener {
                viewModel.delete()
                viewModel.getHistory()
            }
            dialog.show(childFragmentManager, "")
        }
        lifecycleScope.launch {
            viewModel.showLoadingFlow.collect {
                historyText.isVisible = !it
                historyImage2.isVisible = !it
            }
        }
        lifecycleScope.launch {
            viewModel.searchList.collect {
                adapter.submitList(it.data)
                adapter.click = viewModel.click
            }
        }
        lifecycleScope.launch {
            viewModel.searchQueryFlow.collect {
                it.let { adapter.query = it }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wordsFlow
                .flowWithLifecycle(lifecycle)
                .collect {
                    hideKeyboard()
                    val dialog = BottomDialog(it.data!!)
                    dialog.show(childFragmentManager, "")
                    viewModel.addWordHistory(
                        WordData(
                            it.data.id, it.data.word, it.data.syllable
                        )
                    )
                }
        }
        lifecycleScope.launch {
            viewModel.showImageEmptyFlow.collect {
                imageEmpty.isVisible = it
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchWord(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchWord(newText)
        return true
    }

    private fun searchWord(text: String?) {
        if (text != null) {
            viewModel.click = false
            viewModel.getSearchWord(text)
            viewModel.onSearch(text)
        }
        if (text!!.isEmpty()) {
            viewModel.getHistory()
        }
    }
}