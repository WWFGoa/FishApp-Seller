package com.deepwares.fishmarketplace.ui.creator

import com.amplifyframework.datastore.generated.model.CatchSize

interface CreateInventoryInterface {

    fun getCatchSize():CatchSize
}