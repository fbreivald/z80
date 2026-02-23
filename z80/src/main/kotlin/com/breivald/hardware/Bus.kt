package com.breivald.hardware

import kotlin.math.min

class Bus(val width: Int) {
  private val lines = ArrayList<List<Pin>>(3)

  fun connect(vararg pins: Pin) {
    lines.forEach { it.zip(pins, Pin::connect) }
    lines.addFirst(pins.toList())
  }

  fun toUByte(): UByte = toUInt(8).toUByte()

  fun toUShort(): UShort = toUInt(16).toUShort()

  @OptIn(ExperimentalStdlibApi::class)
  fun toUInt(bits: Int = 32): UInt {
    if (lines.isEmpty()) {
      return 0U
    } else {
      val pins = lines[0]
      var res = 0U
      for (i in 0..<min(pins.size, bits)) {
        res = res or ((if (pins[i].active) 1U else 0U) shl i)
      }
      return res
    }
  }
}
