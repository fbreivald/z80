package com.breivald.z80

import com.breivald.hardware.Pin

abstract class Memory {
  var addressBus: UShort = 0U
  var dataBus: UByte = 0U
  abstract val chipEnable: Pin
  abstract val writeEnable: Pin
  abstract val outputEnable: Pin
}

@OptIn(ExperimentalUnsignedTypes::class)
class Ram(val size: Int): Memory() {
  private val mem = UByteArray(size)

  override val chipEnable = Pin.input(onActivation = this::handleEnabled)
  override val outputEnable = Pin.input(onActivation = this::handleEnabled)
  override val writeEnable = Pin.input(onActivation = this::handleEnabled)

  private fun handleEnabled() {
    if (chipEnable.active) {
      if (writeEnable.active) {
        mem[addressBus.toInt()] = dataBus
      } else if (writeEnable.active) {
        dataBus = mem[addressBus.toInt()]
      }
    }
  }
}