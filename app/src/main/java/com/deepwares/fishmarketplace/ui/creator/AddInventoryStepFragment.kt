package com.deepwares.fishmarketplace.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amplifyframework.datastore.generated.model.CatchSize
import com.deepwares.fishmarketplace.R

class AddInventoryStepFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    var weightPicker: NumberPicker? = null
    var pricePicker: NumberPicker? = null
    var catchTimePicker: TimePicker? = null
    var sellTimePicker: TimePicker? = null
    private lateinit var createViewModel: CreateViewModel
    private var catchLocation: EditText? = null
    private var sellLocation: EditText? = null
    private var createListing: View? = null

    val creatorStep = CreatorSteps.values().firstOrNull { it.layout == contentLayoutId }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewModel =
            ViewModelProvider(requireActivity()).get(CreateViewModel::class.java)
        val parent = inflater.inflate(R.layout.creator_step_base, container, false) as ViewGroup
        val view = super.onCreateView(inflater, parent, savedInstanceState)
        parent.addView(view)

        return parent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        creatorStep?.let {
            when (creatorStep) {
                CreatorSteps.SIZE -> {

                }
                CreatorSteps.WEIGHT -> {
                    weightPicker = view.findViewById(R.id.weight_picker)
                    weightPicker!!.maxValue = 50
                    weightPicker!!.minValue = 10
                }
                CreatorSteps.PRICE -> {
                    pricePicker = view.findViewById(R.id.price_picker)
                    pricePicker!!.maxValue = 250
                    pricePicker!!.minValue = 100
                }
                CreatorSteps.CATCH_TIME -> {
                    catchLocation = view.findViewById(R.id.catch_location)
                    catchTimePicker = view.findViewById(R.id.catch_time)
                }
                CreatorSteps.SELL_TIME -> {
                    sellLocation = view.findViewById(R.id.catch_location)
                    sellTimePicker = view.findViewById(R.id.catch_time)
                }
                CreatorSteps.FINISH -> {
                    createListing = view.findViewById(R.id.create_listing)
                    createListing?.setOnClickListener {
                        createViewModel.createListing()
                        Toast.makeText(context, R.string.creating_inventory, Toast.LENGTH_LONG)
                            .show()
                        findNavController().navigate(R.id.navigation_existing)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        creatorStep?.let {
            when (creatorStep) {
                CreatorSteps.SIZE -> {
                    createViewModel.inventory?.size(CatchSize.Medium)

                }
                CreatorSteps.WEIGHT -> {
                    createViewModel.inventory?.quantity(weightPicker?.value)
                    createViewModel.inventory?.availableQuantity(weightPicker?.value)
                }
                CreatorSteps.PRICE -> {
                    createViewModel.inventory?.price(pricePicker?.value)
                }
                CreatorSteps.CATCH_TIME -> {
                    createViewModel.inventory?.catchLocation(catchLocation?.text.toString())
                    createViewModel.inventory?.catchTime(catchTimePicker?.currentHour?.toString())
                }
                CreatorSteps.SELL_TIME -> {
                    createViewModel.inventory?.sellLocation(sellLocation?.text.toString())
                    createViewModel.inventory?.sellTime(sellTimePicker?.currentHour?.toString())
                }
                CreatorSteps.FINISH -> {

                }
            }
        }
    }


    override fun onDestroyView() {

        super.onDestroyView()
    }
}


