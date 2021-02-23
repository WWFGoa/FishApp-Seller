package com.deepwares.fishmarketplace.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Inventory
import com.amplifyframework.datastore.generated.model.Order
import com.amplifyframework.datastore.generated.model.Order.INVENTORY
import com.google.android.gms.auth.api.Auth
import org.joda.time.DateTime

class OrdersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text


    val items = MutableLiveData<List<Order>>()
    val TAG = OrdersViewModel::class.java.name
    fun fetch() {
        Amplify.API.query(
            ModelQuery.list(
                Order::class.java,
                Order.CREATED_AT.gt(DateTime.now().minusDays(7).toDate())
                //, Inventory.USER_ID.eq(Amplify.Auth.currentUser.userId)
            ),
            { response ->
                val newitems = ArrayList<Order>()
                response?.data?.let {
                    val filter =
                        it.filter { it.inventory.userId == Amplify.Auth.currentUser.userId }
                    if (!filter.isNullOrEmpty()) {
                        newitems.addAll(filter)
                    }
                }
                items.postValue(newitems)
                Log.d(TAG, "Got items : " + response.data?.toString())

            },
            { error -> Log.e(TAG, "Get order list failed from AWS", error) }
        )
    }

}