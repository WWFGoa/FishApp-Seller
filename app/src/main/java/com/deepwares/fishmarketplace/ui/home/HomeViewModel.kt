package com.deepwares.fishmarketplace.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.predicate.QueryPredicate
import com.amplifyframework.datastore.generated.model.Inventory

class HomeViewModel : ViewModel() {

    val items = MutableLiveData<List<Inventory>>()
    val TAG = HomeViewModel::class.java.name
    fun fetch() {

        Amplify.API.query(
            ModelQuery.list(
                Inventory::class.java,
                Inventory.USER_ID.eq(Amplify.Auth.currentUser.userId)
            ),
            { response ->
                val newitems = ArrayList<Inventory>()
                response?.data?.let {

                    newitems.addAll(it)
                }
                items.postValue(newitems)
                Log.d(TAG, "Got items : " + response.data?.toString())
            },
            { error -> Log.e(TAG, "Get inventory listtt failed", error) }


        )
    }
}