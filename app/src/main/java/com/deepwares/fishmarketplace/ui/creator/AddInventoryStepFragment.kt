package com.deepwares.fishmarketplace.ui.creator

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.datastore.generated.model.CatchSize
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.ui.login.afterTextChanged
import java.text.SimpleDateFormat
import java.util.*

class AddInventoryStepFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    var weightPicker: NumberPicker? = null
    var pricePicker: NumberPicker? = null
    var catchTimePicker: TimePicker? = null
    private lateinit var createViewModel: CreateViewModel
    private var catchLocation: EditText? = null
    private var sellLocation: EditText? = null

    private var createSize: TextView? = null
    private var createQty: TextView? = null
    private var createCost: TextView? = null
    private var createTime: TextView? = null
    private var createCatchLoc: TextView? = null
    private var createSellLoc: TextView? = null

    private var radioButton: RadioGroup? = null
    val creatorStep = CreatorSteps.values().firstOrNull { it.layout == contentLayoutId }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewModel =
            ViewModelProvider(requireParentFragment()).get(CreateViewModel::class.java)
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
                    radioButton!!.setOnCheckedChangeListener { group, checkedId ->
                        when (checkedId) {
                            R.id.creator_size_small -> createViewModel.inventory?.size(CatchSize.SMALL)
                            R.id.creator_size_medium -> createViewModel.inventory?.size(CatchSize.MEDIUM)
                            R.id.creator_size_large -> createViewModel.inventory?.size(CatchSize.LARGE)
                            else -> {
                                createViewModel.inventory?.size(CatchSize.MEDIUM)
                            }
                        }
                    }
                }
                CreatorSteps.WEIGHT -> {
                    weightPicker = view.findViewById(R.id.weight_picker)
                    weightPicker!!.maxValue = 50
                    weightPicker!!.minValue = 1

                    weightPicker!!.setOnValueChangedListener { picker, oldVal, newVal ->
                        createViewModel.inventory?.quantity(weightPicker?.value!!.toFloat())
                        createViewModel.inventory?.availableQuantity(weightPicker?.value!!.toFloat())
                    }
                    createViewModel.inventory?.quantity(weightPicker?.value!!.toFloat())
                    createViewModel.inventory?.availableQuantity(weightPicker?.value!!.toFloat())

                }
                CreatorSteps.PRICE -> {
                    pricePicker = view.findViewById(R.id.price_picker)
                    if (parentFragment is AddInventoryFragment) {
                        val inventoryFragment = parentFragment as AddInventoryFragment
                        val spec = inventoryFragment.getSpecies()
                        pricePicker!!.maxValue = resources.getInteger(spec.maxPrice)
                        pricePicker!!.minValue = resources.getInteger(spec.minPrice)
                    } else {
                        pricePicker!!.maxValue = 250
                        pricePicker!!.minValue = 100
                    }

                    pricePicker!!.setOnValueChangedListener { picker, oldVal, newVal ->
                        createViewModel.inventory?.price(pricePicker?.value)
                    }
                    createViewModel.inventory?.price(pricePicker?.value)
                }
                CreatorSteps.CATCH_TIME -> {
                    catchTimePicker = view.findViewById(R.id.catch_time)
                    catchTimePicker!!.setOnTimeChangedListener { view, hourOfDay, minute ->
                        catchTimeUpdated(hourOfDay, minute)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        catchTimeUpdated(catchTimePicker!!.hour, catchTimePicker!!.minute)
                    } else {
                        catchTimeUpdated(
                            catchTimePicker!!.currentHour,
                            catchTimePicker!!.currentMinute
                        )
                    }
                }
                CreatorSteps.CATCH_LOCATION -> {
                    catchLocation = view.findViewById(R.id.catch_location)
                    catchLocation!!.afterTextChanged {
                        createViewModel.inventory?.catchLocation(
                            it
                        )
                    }
                }
                CreatorSteps.SELL_LOCATION -> {
                    sellLocation = view.findViewById(R.id.sell_location)
                    sellLocation!!.afterTextChanged {
                        createViewModel.inventory?.sellLocation(
                            it
                        )
                    }
                }
                CreatorSteps.FINISH -> {
                    createSize = view.findViewById(R.id.size)
                    createQty = view.findViewById(R.id.quantity)
                    createCost = view.findViewById(R.id.cost)
                    createTime = view.findViewById(R.id.time)
                    createCatchLoc = view.findViewById(R.id.catch_location)
                    createSellLoc = view.findViewById(R.id.sell_location)

                    val inv = createViewModel.inventory!!.build()
                    inv?.let {
                        createSize!!.text = inv.size.name
                        createQty!!.text =
                            resources.getString(R.string.qty_in_kg, inv.quantity.toString())
                        createCost!!.text =
                            resources.getString(R.string.price_in_kg, inv.price.toString())
                        createTime!!.text = inv.catchTime
                        createCatchLoc!!.text = inv.catchLocation
                        createSellLoc!!.text = inv.sellLocation
                    }

                    /*
                    createListing = view.findViewById(R.id.create_listing)
                    createListing?.setOnClickListener {
                        createViewModel.createListing()
                        Toast.makeText(context, R.string.creating_inventory, Toast.LENGTH_LONG)
                            .show()
                        findNavController().navigate(R.id.navigation_existing)
                    }

                     */
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (creatorStep == CreatorSteps.FINISH) {
            val inv = createViewModel.inventory!!.build()
            inv?.let {
                createSize!!.text = inv.size.name
                createQty!!.text =
                    resources.getString(R.string.qty_in_kg, inv.quantity.toString())
                createCost!!.text =
                    resources.getString(R.string.price_in_kg, inv.price.toString())
                createTime!!.text = inv.catchTime
                createCatchLoc!!.text = inv.catchLocation
                createSellLoc!!.text = inv.sellLocation
            }
        }
    }

    private fun catchTimeUpdated(hourOfDay: Int, minute: Int) {
        val dateTime = Date()
        val c = Calendar.getInstance()
        val g = GregorianCalendar(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH),
            hourOfDay,
            minute
        )
        g.timeInMillis

        val catchTime =
            SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(g.timeInMillis))
        createViewModel.inventory?.catchTime(catchTime)
    }

}


