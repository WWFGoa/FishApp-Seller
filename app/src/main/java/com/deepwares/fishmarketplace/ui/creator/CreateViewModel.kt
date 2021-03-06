package com.deepwares.fishmarketplace.ui.creator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Inventory
import com.deepwares.fishmarketplace.App
import com.deepwares.fishmarketplace.model.FishRepository
import com.deepwares.fishmarketplace.model.Species


class CreateViewModel : ViewModel() {

    val TAG = CreateViewModel::class.java.name
    val IN_FLIGHT = 0
    val COMPLETE = 1
    val ERROR = 2
    val species = ArrayList<Species>().apply { addAll(FishRepository.species) }
    var creatorProgress = MutableLiveData<Int>(-1)
    private val _species = MutableLiveData<List<Species>>().apply {
        value = species
    }

    val speciesSearch = ArrayList<Species>().apply { addAll(FishRepository.species) }

    val speciesFiltered = ArrayList<Species>().apply { addAll(FishRepository.species) }

    private val _speciesSearch = MutableLiveData<List<Species>>().apply {
        value = speciesSearch
    }

    private val _speciesFiltered = MutableLiveData<List<Species>>().apply {
        value = speciesFiltered
    }

    val speciesLiveData: MutableLiveData<List<Species>> = _species
    val speciesSearchLiveData: MutableLiveData<List<Species>> = _speciesSearch
    val speciesFilteredLiveData: MutableLiveData<List<Species>> = _speciesFiltered


    var inventory: Inventory.Builder? = null
    var currentSpecies: com.amplifyframework.datastore.generated.model.Species.Builder? = null

    var userName: String? = null

    var currentStep = CreatorSteps.SIZE

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
                    || App.INSTANCE.resources.getString(it.konkaniName).toLowerCase()
                        .equals(name.toLowerCase())
                    || App.INSTANCE.resources.getString(it.commonName).toLowerCase()
                        .equals(name.toLowerCase())
                    || App.INSTANCE.resources.getString(it.konkaniName).toLowerCase()
                        .contains(name.toLowerCase())
                    || App.INSTANCE.resources.getString(it.commonName).toLowerCase()
                        .contains(name.toLowerCase())

                ) {
                    list.add(it)
                }
            }
        }
        speciesSearchLiveData.postValue(list)
    }

    fun filter(category: Int?) {

        var list = ArrayList<Species>()
        if (category == null) {
            list.addAll(FishRepository.species)
        } else {
            list.addAll(FishRepository.species.filter { App.INSTANCE.resources.getInteger(it.status) == category })
        }
        speciesFilteredLiveData.postValue(list)
    }

    fun createListing() {
        if (creatorProgress.value == IN_FLIGHT) {
            Log.d(TAG, "Creation already in progress. Possible double tap")
            return
        }
        creatorProgress.postValue(IN_FLIGHT)
        val inv = inventory
        if (userName == null) {
            Amplify.Auth.fetchUserAttributes({ list ->
                val name = list.find { it.key == AuthUserAttributeKey.name() }
                userName = name?.value
                if (name == null) {
                    userName = "Seller"
                }
                createListingInternal(inv)
            }, {
                creatorProgress.postValue(ERROR)
            })
        } else {
            createListingInternal(inv)
        }
    }

    fun createListingInternal(inv: Inventory.Builder?) {
        inv?.let {

            val user = Amplify.Auth.currentUser
            it.contact(user.username)
            it.name(userName)
            it.userId(user.userId)
            val item = it.build()
            Amplify.API.mutate(
                ModelMutation.create(item),
                { response ->
                    Log.d(
                        TAG,
                        "Added Inventory with id: " + response?.data?.id
                    )
                    creatorProgress.postValue(COMPLETE)
                },
                { error: ApiException? ->
                    Log.e(TAG, "Create Inventory failed", error)
                    creatorProgress.postValue(ERROR)
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
                    Log.d(TAG, "Added Species with id: " + response.data?.id)
                },
                { error: ApiException? ->
                    Log.e(TAG, "Create Species failed", error)
                }
            )
        }
    }

    fun isCurrentStepValid(): Boolean {
        val inv = inventory?.build()
        inv?.let {
            when (currentStep) {
                CreatorSteps.SIZE -> return inv.size != null
                CreatorSteps.WEIGHT -> return inv.quantity != null
                CreatorSteps.PRICE -> return inv.price != null
                CreatorSteps.CATCH_TIME -> return inv.catchTime.isNullOrBlank().not()
                CreatorSteps.CATCH_LOCATION -> return inv.catchLocation.isNullOrBlank().not()
                CreatorSteps.SELL_LOCATION -> return inv.sellLocation.isNullOrBlank().not()
                CreatorSteps.FINISH -> return true
            }
        }
        return false
    }
}