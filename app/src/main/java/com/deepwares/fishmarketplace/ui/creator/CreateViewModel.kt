package com.deepwares.fishmarketplace.ui.creator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Inventory
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.model.Species


class CreateViewModel : ViewModel() {

    val TAG = CreateViewModel::class.java.name

    val species = arrayListOf(

        Species("Anchovy", R.drawable.anchovy),
        Species("Barramundi", R.drawable.barramundi),
        Species("Base", R.drawable.basa),
        Species("Bombay Duck", R.drawable.bombay_duck),
        Species("Ghole", R.drawable.ghole),
        Species("Grouper", R.drawable.grouper),
        Species("Kane", R.drawable.kane),
        Species("Kingfish", R.drawable.kingfish),
        Species("Mahi Mahi", R.drawable.mahi_mahi),
        Species("Mullet", R.drawable.mullet),
        Species("Pearl Spot", R.drawable.pearl_spot),
        Species("Pomfret", R.drawable.pomfret),
        Species("Red Snapper", R.drawable.red_snapper),
        Species("Rohu", R.drawable.rohu),
        Species("Salmon", R.drawable.salmon),
        Species("Sea Veral", R.drawable.sea_veral),
        Species("Seer", R.drawable.seer),
        Species("Shrimp", R.drawable.shrimp),
        Species("Snake head murrel", R.drawable.snake_head_murrel),
        Species("Sole", R.drawable.sole),
        Species("Tuna", R.drawable.tuna)
    )
    /*
    val species = arrayListOf(

        Species("Carp", R.drawable.anchovy),
        Species("Sea Bass", R.drawable.bombay_duck),
        Species("Trout", R.drawable.bombay),
        Species("Mackerel", R.drawable.baramundi),
        Species("Tuna", R.drawable.ic_asset_8),
        Species("Grouper", R.drawable.ic_asset_9),
        Species("Pomfret", R.drawable.ic_asset_10),
        Species("Cat Fish", R.drawable.ic_asset_11),
        Species("Lion Fish", R.drawable.ic_asset_12),
        Species("Cod", R.drawable.ic_asset_13),
        Species("European Sea Bass", R.drawable.ic_asset_14),
        Species("Halibut", R.drawable.ic_asset_15),
        Species("Mahi Mahi", R.drawable.ic_asset_16),
        Species("Pollock", R.drawable.ic_asset_17),
        Species("King Fish", R.drawable.ic_asset_18),
        Species("Tilapia", R.drawable.ic_asset_19),
        Species("Salmon", R.drawable.ic_asset_20),
        Species("Sturgeon", R.drawable.ic_asset_21),
        Species("Arctic Char", R.drawable.ic_asset_22),
        Species("Alaskan Salmon", R.drawable.ic_asset_23),
        Species("Snapper", R.drawable.ic_asset_24),
        Species("Dory", R.drawable.ic_asset_25),
        Species("Bream", R.drawable.ic_asset_26),
        Species("Haddock", R.drawable.ic_asset_27),
        Species("Drum", R.drawable.ic_asset_28),
        Species("Bream", R.drawable.ic_asset_29),
        Species("Flounder", R.drawable.ic_asset_30)
    )

     */

    private val _species = MutableLiveData<List<Species>>().apply {
        value = species
    }
    val speciesLiveData: MutableLiveData<List<Species>> = _species
    var inventory: Inventory.Builder? = null
    var currentSpecies: com.amplifyframework.datastore.generated.model.Species.Builder? = null

    fun createListing() {
        inventory?.let {

            val item = it.build()
            Amplify.API.mutate(
                ModelMutation.create(item),
                { response ->
                    Log.d(
                        TAG,
                        "Added Inventory with id: " + response?.data?.id
                    )
                },
                { error: ApiException? ->
                    Log.e(TAG, "Create Inventory failed", error)
                }
            )
        }
    }

    fun createSpecies(species: Species, image: Int) {
        currentSpecies?.let {
            it.active(true)
            it.maxPrice(500)
            it.minPrice(200)
            it.maxWeight(20)
            it.minWeight(2)
            it.image(image)
            it.name(species.name)
            val item = it.build()
            Amplify.API.mutate(
                ModelMutation.create(item),
                { response ->
                    Log.d(TAG, "Added Species with id: " + response?.data?.id)
                },
                { error: ApiException? ->
                    Log.e(TAG, "Create Species failed", error)
                }
            )


        }
    }
}