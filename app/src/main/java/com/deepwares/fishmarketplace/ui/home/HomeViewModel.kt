package com.deepwares.fishmarketplace.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Inventory

class HomeViewModel : ViewModel() {

    val items = MutableLiveData<List<Inventory>>()
    val TAG = HomeViewModel::class.java.name
    fun fetch() {

        Amplify.API.query(
            ModelQuery.list(Inventory::class.java),
            { response ->
                response?.data?.let {
                    val newitems = ArrayList<Inventory>()
                    val filter = it.filter { it.userId == Amplify.Auth.currentUser.userId }
                    if(!filter.isNullOrEmpty()){
                        newitems.addAll(filter)
                    }
                    items.postValue(newitems)

                }
                Log.d(TAG, "Got items : " + response.data?.toString())

            },
            { error -> Log.e(TAG, "Get inventory listtt failed", error) }


        )
    }
}