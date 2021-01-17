package com.deepwares.fishmarketplace.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepwares.fishmarketplace.R
import kotlinx.android.synthetic.main.fragment_existing.list
import kotlinx.android.synthetic.main.fragment_existing.swipe_refresh

class ExistingOrderFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_existing, container, false)
        homeViewModel.items.observe(viewLifecycleOwner, Observer {
            swipe_refresh.isRefreshing = false
            adapter.items.clear()
            adapter.items.addAll(it)
            adapter.notifyDataSetChanged()
        })
        adapter = OrderAdapter()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            homeViewModel.fetch()
        }
    }

    override fun onResume() {
        homeViewModel.fetch()
        super.onResume()
    }
}