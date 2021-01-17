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
import com.deepwares.fishmarketplace.App
import com.deepwares.fishmarketplace.R
import kotlinx.android.synthetic.main.fragment_add_inventory.*
import kotlinx.android.synthetic.main.order_item.view.*

class AddInventoryFragment : Fragment() {

    val args: AddInventoryFragmentArgs by navArgs()
    private lateinit var createViewModel: CreateViewModel
    lateinit var currentSpecies: com.deepwares.fishmarketplace.model.Species
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
        currentSpecies = createViewModel.species.get(args.image)
        species_image.setImageResource(currentSpecies.image)

        createViewModel.inventory = Inventory.Builder()
        createViewModel.currentSpecies = Species.Builder()


        createViewModel.createSpecies(currentSpecies, args.image)
        createViewModel.inventory!!.species(args.image)

        pager.adapter = CreatorPagerAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )

        val stat = App.INSTANCE.resources.getInteger(currentSpecies.status)

        if (stat == 4) {
            pager.visibility = View.INVISIBLE
            card.visibility = View.GONE
            conservation_status_label.visibility = View.VISIBLE
            conservation_status.visibility = View.VISIBLE
            desc.visibility = View.VISIBLE
            desc.setText(currentSpecies.desc)
            conservation_status.setText(R.string.status_banned)
            conservation_status.setTextColor(resources.getColor(R.color.browser_actions_bg_grey))
        }
        //species_image.setImageResource(args.image)
    }


    fun getSpecies(): com.deepwares.fishmarketplace.model.Species {
        return currentSpecies
    }

    override fun onDestroyView() {
        createViewModel.inventory = null
        super.onDestroyView()
    }

}