package com.deepwares.fishmarketplace.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.CatchSize
import com.deepwares.fishmarketplace.R
import java.text.SimpleDateFormat
import java.util.*

class AddInventoryStepFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    var weightPicker: NumberPicker? = null
    var pricePicker: NumberPicker? = null
    var catchTimePicker: TimePicker? = null
    var sellTimePicker: TimePicker? = null
    private lateinit var createViewModel: CreateViewModel
    private var catchLocation: EditText? = null
    private var sellLocation: EditText? = null
    private var createListing: View? = null

    private var radioButton: RadioGroup? = null
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
                    radioButton = view.findViewById(R.id.size_option)
                }
                CreatorSteps.WEIGHT -> {
                    weightPicker = view.findViewById(R.id.weight_picker)

                    weightPicker!!.maxValue = 50
                    weightPicker!!.minValue = 1
                }
                CreatorSteps.PRICE -> {
                    pricePicker = view.findViewById(R.id.price_picker)
                    val fragment = parentFragment
                    if (parentFragment is AddInventoryFragment) {

                        val inventoryFragment = parentFragment as AddInventoryFragment
                        val spec = inventoryFragment.getSpecies()
                        pricePicker!!.maxValue = resources.getInteger(spec.maxPrice)
                        pricePicker!!.minValue = resources.getInteger(spec.minPrice)
                    } else {
                        pricePicker!!.maxValue = 250
                        pricePicker!!.minValue = 100
                    }
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

    fun validate(): Boolean {
        // val copyInv = createViewModel.inventory.has
        return true
    }

    override fun onPause() {
        super.onPause()
        creatorStep?.let {
            when (creatorStep) {
                CreatorSteps.SIZE -> {

                    val pos = radioButton?.checkedRadioButtonId
                    when (pos) {
                        R.id.creator_size_small -> createViewModel.inventory?.size(CatchSize.SMALL)
                        R.id.creator_size_medium -> createViewModel.inventory?.size(CatchSize.MEDIUM)
                        R.id.creator_size_large -> createViewModel.inventory?.size(CatchSize.LARGE)
                        else -> {
                            createViewModel.inventory?.size(CatchSize.MEDIUM)
                        }
                    }
                }
                CreatorSteps.WEIGHT -> {
                    createViewModel.inventory?.quantity(weightPicker?.value!!.toFloat())
                    createViewModel.inventory?.availableQuantity(weightPicker?.value!!.toFloat())
                }
                CreatorSteps.PRICE -> {
                    createViewModel.inventory?.price(pricePicker?.value)
                }
                CreatorSteps.CATCH_TIME -> {
                    createViewModel.inventory?.catchLocation(catchLocation?.text.toString())


                    val dateTime = Date()
                    dateTime
                    val c = Calendar.getInstance()
                    val g = GregorianCalendar(
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH),
                        catchTimePicker!!.currentHour,
                        catchTimePicker!!.currentMinute
                    )
                    g.timeInMillis

                    val catchTime =
                        SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(g.timeInMillis))
                    createViewModel.inventory?.catchTime(catchTime)
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


