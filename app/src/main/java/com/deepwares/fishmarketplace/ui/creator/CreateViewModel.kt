package com.deepwares.fishmarketplace.ui.creator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Inventory
import com.deepwares.fishmarketplace.App
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.model.FishRepository
import com.deepwares.fishmarketplace.model.Species


class CreateViewModel : ViewModel() {

    val TAG = CreateViewModel::class.java.name

    val species = ArrayList<Species>().apply { addAll(FishRepository.species) }

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
            it.maxPrice(App.INSTANCE.resources.getInteger(species.maxPrice))
            it.minPrice(App.INSTANCE.resources.getInteger(species.minPrice))
            it.maxWeight(20)
            it.minWeight(1)
            it.image(image)
            it.name(App.INSTANCE.getString(species.name))
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