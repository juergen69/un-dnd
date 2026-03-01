package com.smsdndmanager.domain.model

/**
 * Represents a parsed volume command from an SMS message
 * @property percentage Volume level from 0 to 100
 */
data class VolumeCommand(
    val percentage: Int
) {
    init {
        require(percentage in 0..100) { "Volume percentage must be between 0 and 100" }
    }
}
