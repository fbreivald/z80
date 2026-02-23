package com.breivald.z80

import com.breivald.hardware.Bus
import com.breivald.hardware.Pin
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BusTest {

  @Test
  fun connectedPinsGettingTheCorrectSignal() {
    val bus = Bus(3)

    val a1 = Pin.input()
    val a2 = Pin.inout()
    val a3 = Pin.output()

    val b1 = Pin.output()
    val b2 = Pin.inout()
    val b3 = Pin.inout()

    bus.connect(a1, a2, a3)
    bus.connect(b1, b2, b3)

    // b1 -> a2
    b1.active = true
    assertTrue(b1.active)

    // b2 -> a2
    b2.active = true
    assertTrue(a2.active)

    // a3 -> b3
    a3.active = true
    assertTrue(b1.active)

    // a2 -> b2
    a2.active = false
    assertFalse(b2.active)

    // b3 -> a3
    b3.active = false
    assertFalse(a3.active)
  }

  @Test
  fun toUInt_noConnectedPinsShouldReturn0() {
    assertEquals(0U, Bus(32).toUInt())
  }

  @Test
  fun toUByte_shouldReturnAUIntRepresentingTheBusLines() {
    val bus = Bus(8)

    bus.connect(*Array(4) { i ->
      val pin = Pin.input()
      pin.active = i % 2 == 0
      pin
    })

    assertEquals(0b0000_1010_U.toUByte(), bus.toUByte())
  }

  @Test
  fun toUByte_shouldReturnLeastSignificantButsWhenMoreThan8BusLines() {
    val width = 9
    val bus = Bus(width)

    bus.connect(*Array(width) { i ->
      val pin = Pin.input()
      pin.active = i % 2 == 0
      pin
    })

    assertEquals(0b1010_1010_U.toUByte(), bus.toUByte())
  }

  @Test
  fun toUShort_shouldReturnAUShortRepresentingTheBusLines() {
    val bus = Bus(16)

    bus.connect(*Array(4) { i ->
      val pin = Pin.input()
      pin.active = i % 2 == 0
      pin
    })

    assertEquals(0b0000_1010U.toUShort(), bus.toUShort())
  }

  @Test
  fun toUShort_shouldReturnLeastSignificantBitsWhenMoreThan16Lines() {
    val width = 17
    val bus = Bus(width)

    bus.connect(*Array(width) { i ->
      val pin = Pin.input()
      pin.active = i % 2 == 0
      pin
    })

    assertEquals(0b1010_1010_1010_1010_U.toUShort(), bus.toUShort())
  }

  @Test
  fun toUInt_shouldReturnLeastSignificantBitsWhenMoreThan32Lines() {
    val width = 33
    val bus = Bus(width)

    bus.connect(*Array(width) { i ->
      val pin = Pin.input()
      pin.active = i % 2 == 0
      pin
    })

    assertEquals(0xAAAA_AAAA_AAAA_AAAAU.toUShort(), bus.toUShort())
  }
}