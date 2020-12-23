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

    val speciesFiltered = ArrayList<Species>().apply { addAll(FishRepository.species) }

    private val _speciesFiltered = MutableLiveData<List<Species>>().apply {
        value = speciesFiltered
    }

    val speciesLiveData: MutableLiveData<List<Species>> = _species
    val speciesFilteredLiveData: MutableLiveData<List<Species>> = _speciesFiltered
    var inventory: Inventory.Builder? = null
    var currentSpecies: com.amplifyframework.datastore.generated.model.Species.Builder? = null

    fun filter(name: String?) {

        var list = ArrayList<Species>()
        if (name.isNullOrEmpty()) {
            list.addAll(FishRepository.species)
        } else {
            FishRepository.species.forEach {

                if (App.INSTANCE.resources.getString(it.name).toLowerCase()
                        .contains(name.toLowerCase())
                    || name.toLowerCase().contains(
                        com.deepwares.fishmarketplace.App.INSTANCE.resources.getString(it.name)
                            .toLowerCase()
                    )
                    || App.INSTANCE.resources.getString(it.name).toLowerCase()
                        .equals(name.toLowerCase())

                ) {
                    list.add(it)
                }
            }
        }
        speciesFilteredLiveData.postValue(list)
    }

    fun createListing() {
        inventory?.let {

            val user = Amplify.Auth.currentUser
            it.contact(user.username)
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
            val user = Amplify.Auth.currentUser
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