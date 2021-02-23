package com.deepwares.fishmarketplace.ui.creator

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.interfaces.SpeciesSelector
import kotlinx.android.synthetic.main.fragment_create.*

class CreateFragment : Fragment() {

    private lateinit var createViewModel: CreateViewModel
    private var speciesSelector: SpeciesSelector? = null
    private lateinit var speciesAdapter: SpeciesAdapter
    private lateinit var speciesSearchAdapter: SpeciesAdapter
    private lateinit var speciesFilterAdapter: SpeciesAdapter
    private var isSearching = false
    private var isFiltered = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speciesSelector = activity as SpeciesSelector
        speciesAdapter = SpeciesAdapter(speciesSelector)
        speciesSearchAdapter = SpeciesAdapter(speciesSelector)
        speciesFilterAdapter = SpeciesAdapter(speciesSelector)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewModel =
            ViewModelProvider(requireActivity()).get(CreateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = speciesAdapter
        list.layoutManager = GridLayoutManager(context, 3)
        createViewModel.speciesLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                speciesAdapter.species.clear()
                speciesAdapter.species.addAll(it)
                speciesAdapter.notifyDataSetChanged()

            }
        })

        createViewModel.speciesSearchLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                speciesSearchAdapter.species.clear()
                speciesSearchAdapter.species.addAll(it)
                speciesSearchAdapter.notifyDataSetChanged()

            }
        })

        createViewModel.speciesFilteredLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                speciesFilterAdapter.species.clear()
                speciesFilterAdapter.species.addAll(it)
                speciesFilterAdapter.notifyDataSetChanged()

            }
        })

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                createViewModel.filter(query)
                isSearching = query.isNullOrBlank().not()
                isFiltered = -1
                updateAdapter()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                createViewModel.filter(newText)
                isSearching = newText.isNullOrBlank().not()
                isFiltered = -1
                updateAdapter()
                return true
            }

        })

        val padding = resources.getDimensionPixelSize(R.dimen.padding_standard)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val index = parent.getChildAdapterPosition(view)
                val pos = index % 3
                if (pos == 0) {
                    outRect.left = padding * 2
                    outRect.right = padding
                } else if (pos == 2) {
                    outRect.left = padding
                    outRect.right = padding * 2
                } else {
                    outRect.left = padding
                    outRect.right = padding
                }
                outRect.top = padding * 2

            }
        })


        legend_1.setOnClickListener {
            if (isFiltered == 3) {
                isFiltered = -1
            } else {
                isFiltered = 3
            }
            createViewModel.filter(isFiltered)
            updateAdapter()
        }
        legend_2.setOnClickListener {
            if (isFiltered == 2) {
                isFiltered = -1
            } else {
                isFiltered = 2
            }
            createViewModel.filter(isFiltered)
            updateAdapter()
        }
        legend_3.setOnClickListener {
            if (isFiltered == 1) {
                isFiltered = -1
            } else {
                isFiltered = 1
            }
            createViewModel.filter(isFiltered)
            updateAdapter()
        }
        legend_4.setOnClickListener {
            if (isFiltered == 4) {
                isFiltered = -1
            } else {
                isFiltered = 4
            }
            createViewModel.filter(isFiltered)
            updateAdapter()
        }
        legend_5.setOnClickListener {
            if (isFiltered == 0) {
                isFiltered = -1
            } else {
                isFiltered = 0
            }
            createViewModel.filter(isFiltered)
            updateAdapter()
        }
    }

    fun updateAdapter() {
        if (!isSearching && isFiltered == -1) {
            list.adapter = speciesAdapter
        } else if (isSearching) {
            list.adapter = speciesSearchAdapter
        } else if (isFiltered != -1) {
            list.adapter = speciesFilterAdapter
        }
        legend_1.background =
            if (isFiltered == 3) resources.getDrawable(R.drawable.species_selector) else null
        legend_2.background =
            if (isFiltered == 2) resources.getDrawable(R.drawable.species_selector) else null
        legend_3.background =
            if (isFiltered == 1) resources.getDrawable(R.drawable.species_selector) else null
        legend_4.background =
            if (isFiltered == 4) resources.getDrawable(R.drawable.species_selector) else null
        legend_5.background =
            if (isFiltered == 0) resources.getDrawable(R.drawable.species_selector) else null
    }

    override fun onDestroy() {
        speciesSelector = null
        speciesAdapter.speciesSelector = null
        speciesSearchAdapter.speciesSelector = null
        speciesFilterAdapter.speciesSelector = null
        super.onDestroy()
    }

}