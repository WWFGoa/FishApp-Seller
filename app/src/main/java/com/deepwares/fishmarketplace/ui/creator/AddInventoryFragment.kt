package com.deepwares.fishmarketplace.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
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
            ViewModelProvider(this).get(CreateViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentSpecies = createViewModel.species.get(args.image)
        species_image.setImageResource(currentSpecies.image)
        name.setText(currentSpecies.name)
        kname.setText(currentSpecies.konkaniName)

        val stat = App.INSTANCE.resources.getInteger(currentSpecies.status)
        if (stat == 4 || stat == 3) {
            pager.visibility = View.INVISIBLE
            card.visibility = View.GONE
            conservation_status_label.visibility = View.VISIBLE
            conservation_status.visibility = View.VISIBLE
            unavailable.visibility = View.VISIBLE
            desc.visibility = View.VISIBLE
            desc.setText(currentSpecies.desc)
            conservation_status.setText(R.string.status_banned)
            conservation_status.setTextColor(resources.getColor(if (stat == 3) R.color.species_background_red else R.color.species_background_grey))
            next.visibility = View.GONE
            prev.visibility = View.GONE
            create.visibility = View.GONE
            return
        }

        createViewModel.inventory = Inventory.Builder()
        createViewModel.currentSpecies = Species.Builder()


        createViewModel.createSpecies(currentSpecies, args.image)
        createViewModel.inventory!!.species(args.image)

        /*
        pager.adapter = CreatorPagerAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )

         */
        pager.adapter = CreatorRVAdapter(this)
        pager.isUserInputEnabled = false


        //species_image.setImageResource(args.image)
        next.setOnClickListener {
            if (createViewModel.isCurrentStepValid()) {
                if (pager.currentItem < pager.adapter!!.itemCount - 1) {
                    pager.currentItem = pager.currentItem + 1
                }
            } else {
                showErrorMessage()
            }
        }

        prev.setOnClickListener {
            if (pager.currentItem > 0) {
                pager.currentItem = pager.currentItem - 1
            }
        }

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                createViewModel.currentStep = CreatorSteps.values()[position]
                update()
            }
        })
        update()
        createViewModel.creatorProgress.observe(viewLifecycleOwner, Observer {
            if (it == createViewModel.IN_FLIGHT) {
                progress_bar?.visibility = View.VISIBLE
            } else if (it == createViewModel.ERROR) {
                progress_bar?.visibility = View.GONE
                Toast.makeText(context, R.string.create_error, Toast.LENGTH_LONG).show()
            } else if (it == createViewModel.COMPLETE) {
                progress_bar?.visibility = View.GONE
                Toast.makeText(context, R.string.create_success, Toast.LENGTH_LONG).show()

                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.popBackStack(R.id.navigation_add, true)
                navController.navigate(R.id.navigation_existing)
            } else {
                progress_bar?.visibility = View.GONE
            }
        })

        create.setOnClickListener {
            createViewModel.createListing()
            Toast.makeText(context, R.string.creating_inventory, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showErrorMessage() {
        var errorId: Int? = when (createViewModel.currentStep) {
            CreatorSteps.SIZE -> R.string.create_error_size
            CreatorSteps.WEIGHT -> R.string.create_error_qty
            CreatorSteps.PRICE -> R.string.create_error_cost
            CreatorSteps.CATCH_TIME -> R.string.create_error_catch_time
            CreatorSteps.CATCH_LOCATION -> R.string.create_error_catch_loc
            CreatorSteps.SELL_LOCATION -> R.string.create_error_sell_loc
            else -> null
        }
        if (errorId != null) {
            Toast.makeText(context, errorId, Toast.LENGTH_SHORT).show()
        }
    }

    fun update() {
        prev.visibility = if (pager.currentItem == 0) View.GONE else View.VISIBLE
        next.visibility =
            if (pager.currentItem == pager.adapter!!.itemCount - 1) View.INVISIBLE else View.VISIBLE
        create.visibility =
            if (pager.currentItem == pager.adapter!!.itemCount - 1) View.VISIBLE else View.GONE
        val lp = prev.layoutParams as ConstraintLayout.LayoutParams
        if (pager.currentItem == pager.adapter!!.itemCount - 1) {
            lp.rightToLeft = R.id.create
        } else {
            lp.rightToLeft = R.id.next
        }
        prev.layoutParams = lp
    }


    fun getSpecies(): com.deepwares.fishmarketplace.model.Species {
        return currentSpecies
    }

    override fun onDestroyView() {
        createViewModel.inventory = null
        super.onDestroyView()
    }

}