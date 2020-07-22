package com.deepwares.fishmarketplace.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "TODO: Show Past listings"
    }
    val text: LiveData<String> = _text
}