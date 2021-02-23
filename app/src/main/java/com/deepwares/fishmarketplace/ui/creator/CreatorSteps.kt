package com.deepwares.fishmarketplace.ui.creator

import com.deepwares.fishmarketplace.R

enum class CreatorSteps(val layout: Int) {
    SIZE(R.layout.creator_step_size),
    WEIGHT(R.layout.creator_step_weight),
    PRICE(R.layout.creator_step_price),
    CATCH_TIME(R.layout.creator_step_catch_time),
    CATCH_LOCATION(R.layout.creator_step_catch_location),
    SELL_LOCATION(R.layout.creator_step_sell_location),
    FINISH(R.layout.creator_step_finish)
}