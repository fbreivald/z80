package com.breivald.z80

import com.breivald.hardware.Register
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RegisterTest {
  @Test
  fun settingValueRestrictsToSize() {
    val reg = Register(8)

    reg.value = 0xFFFF_U

    assertEquals(0xFF_U, reg.value)
  }

  @Test
  fun shl() {
    val reg = Register(0b00000001U.toUByte())

    assertEquals(0b00000010U, (reg shl 1).value)
    assertEquals(0b00001000U, (reg shl 2).value)
    assertEquals(0b01000000U, (reg shl 3).value)
    assertEquals(0b00000000U, (reg shl 3).value)
  }

  @Test
  fun shr() {
    val reg = Register(0b11110000U.toUByte())

    assertEquals(0b01111000U, (reg shr 1).value)
    assertEquals(0b00011110U, (reg shr 2).value)
    assertEquals(0b00000011U, (reg shr 3).value)
    assertEquals(0b00000000U, (reg shr 3).value)
  }

  @Test
  fun get() {
    val reg = Register(1U.toUByte())

    assertTrue(reg[0])
    assertFalse(reg[1])
    assertFalse(reg[2])
    assertFalse(reg[3])
    assertFalse(reg[4])
    assertFalse(reg[5])
    assertFalse(reg[6])
    assertFalse(reg[7])
  }

  @Test()
  fun get_shouldFailWhenOutOfBounds() {
    assertThrows<IndexOutOfBoundsException> {
      Register(8)[-1]
    }

    assertThrows<IndexOutOfBoundsException> {
      Register(8)[8]
    }
  }

  @Test
  fun set() {
    val reg = Register(1U)

    reg[0] = false
    assertEquals(0U, reg.value)

    reg[1] = true
    assertEquals(2U, reg.value)

    reg[2] = true
    assertEquals(6U, reg.value)
  }

  @Test
  fun set_shouldFailWhenOutOfBounds() {
    assertThrows<IndexOutOfBoundsException> {
      Register(8)[-1] = true
    }
    assertThrows<IndexOutOfBoundsException> {
      Register(8)[8] = true
    }
  }

  @Test
  fun inc() {
    val reg = Register(8)

    assertEquals(1U, reg.inc().value)
    assertEquals(2U, reg.inc().value)
    assertEquals(3U, reg.inc().value)
    assertEquals(4U, reg.inc().value)
  }

  @Test
  fun inc_restrictsToSize() {
    val reg = Register(0xFF.toUByte())

    assertEquals(0U, reg.inc().value)
  }

  @Test
  fun iterator_iteratesFromLeastSignificantBit() {
    val reg = Register(0b1010_1010_U.toUByte())
    for (b in reg) {
      b
    }
    assertIterableEquals(listOf(true, false, true, false, true, false, true, false), reg)
    assertIterableEquals(listOf(true, false, true, false, true, false, true, false), reg)
  }

  @Test
  fun shl_16bit() {
    val reg = Register(0b0000_0000_0000_1111U)

    assertEquals(0b0000_0000_0001_1110U, (reg shl 1).value)
    assertEquals(0b0000_0000_0111_1000U, (reg shl 2).value)
    assertEquals(0b0000_0011_1100_0000U, (reg shl 3).value)
    assertEquals(0b0011_1100_0000_0000U, (reg shl 4).value)
    assertEquals(0b1000_0000_0000_0000U, (reg shl 5).value)
  }

  @Test
  fun shr_16bit() {
    val reg = Register(0b1111_0000_0000_0000U)

    assertEquals(0b0111_1000_0000_0000U, (reg shr 1).value)
    assertEquals(0b0001_1110_0000_0000U, (reg shr 2).value)
    assertEquals(0b0000_0011_1100_0000U, (reg shr 3).value)
    assertEquals(0b0000_0000_0011_1100U, (reg shr 4).value)
    assertEquals(0b0000_0000_0000_0001U, (reg shr 5).value)
  }
}