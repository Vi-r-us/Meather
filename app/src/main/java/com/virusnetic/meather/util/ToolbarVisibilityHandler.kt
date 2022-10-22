package com.virusnetic.meather.util

import com.virusnetic.meather.models.LocationEntity

interface ToolbarVisibilityHandler {
    fun showToolbar()

    fun hideToolbar()

    fun insertRecord(locationEntity: LocationEntity)
}