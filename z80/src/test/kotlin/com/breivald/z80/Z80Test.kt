package com.breivald.z80

import com.breivald.hardware.Bus
import com.breivald.hardware.Pin
import com.breivald.unsigned.shl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Z80Test {

  private var clock = Clock()
  private var dataBus = Bus(8)
  private var addressBus = Bus(16)
  private var cpu = Z80(clock, dataBus, addressBus)

  @BeforeEach
  fun setUp() {
    clock = Clock()
    dataBus = Bus(8)
    addressBus = Bus(16)
    cpu = Z80(clock, dataBus, addressBus)
  }

  @AfterEach
  fun tearDown() {
  }

  @Test
  fun M1Cycle() {
    cpu.PC.value = 0xBEEF_U
    cpu.CLK.active = true

    clock.tick()

    assertTrue(cpu.M1.active)
    assertEquals(0xBEEF_U, addressBus.toUInt())
//    assertEquals(0xBEEF_U +
  }

  fun pinsToUShort(vararg pins: Pin): UShort {
    var res: UShort = 0U
    for (pin in pins) {
      res = (res shl 1) and (if (pin.active) 1U else 0U)
    }
    return res
  }
}