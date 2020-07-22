package com.deepwares.fishmarketplace.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.amplifyframework.datastore.generated.model.Inventory
import com.amplifyframework.datastore.generated.model.Species
import com.deepwares.fishmarketplace.R
import kotlinx.android.synthetic.main.fragment_add_inventory.*

class AddInventoryFragment : Fragment() {

    val args: AddInventoryFragmentArgs by navArgs()
    private lateinit var createViewModel: CreateViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewModel =
            ViewModelProvider(requireActivity()).get(CreateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create, container, false)
        return inflater.inflate(R.layout.fragment_add_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var species = createViewModel.species[args.image]
        species_image.setImageResource(species.image)

        createViewModel.inventory = Inventory.Builder()
        createViewModel.currentSpecies = Species.Builder()

        createViewModel.createSpecies(species, args.image)
        pager.adapter = CreatorPagerAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        //species_image.setImageResource(args.image)
    }

    override fun onDestroyView() {
        createViewModel.inventory = null
        super.onDestroyView()
    }

}