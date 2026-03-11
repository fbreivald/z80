package com.breivald.z80

import com.breivald.hardware.Register
import com.breivald.hardware.RegisterPair
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegisterPairTest {

  @Test
  fun changingValuePropagatesDownToByteRegister() {
    val lo = Register(8)
    val hi = Register(8)
    val reg = RegisterPair(hi, lo)

    reg.value = 0xBEEF_U

    assertEquals(0xBEEF_U, reg.value)
    assertEquals(0xEF_U, lo.value)
    assertEquals(0xBE_U, hi.value)
  }

  @Test
  fun shlPropagatesDownToByteRegister() {
    val lo = Register(0b00001101_U.toUByte())
    val hi = Register(0b10001010_U.toUByte())
    val reg = RegisterPair(hi, lo)

    reg shl 1
    assertEquals(0b00010100_00011010_U, reg.value)
    assertEquals(0b00010100_U, hi.value)
    assertEquals(0b00011010_U, lo.value)

    reg shl 2
    assertEquals(0b01010000_01101000_U, reg.value)
    assertEquals(0b01010000_U, hi.value)
    assertEquals(0b01101000_U, lo.value)

    reg shl 3
    assertEquals(0b10000011_01000000_U, reg.value)
    assertEquals(0b10000011_U, hi.value)
    assertEquals(0b01000000_U, lo.value)

    reg shl 4
    assertEquals(0b00110100_00000000_U, reg.value)
    assertEquals(0b00110100_U, hi.value)
    assertEquals(0b00000000_U, lo.value)
  }

  @Test
  fun shrPropagatesDownToByteRegister() {
    val lo = Register(0b00001101_U.toUByte())
    val hi = Register(0b10001010_U.toUByte())
    val reg = RegisterPair(hi, lo)

    reg shr 1
    assertEquals(0b01000101_00000110_U, reg.value)
    assertEquals(0b01000101_U, hi.value)
    assertEquals(0b00000110_U, lo.value)

    reg shr 2
    assertEquals(0b00010001_01000001_U, reg.value)
    assertEquals(0b00010001_U, hi.value)
    assertEquals(0b01000001_U, lo.value)

    reg shr 3
    assertEquals(0b00000010_00101000_U, reg.value)
    assertEquals(0b00000010_U, hi.value)
    assertEquals(0b00101000_U, lo.value)

    reg shr 4
    assertEquals(0b00000000_00100010_U, reg.value)
    assertEquals(0b00000000_U, hi.value)
    assertEquals(0b00100010_U, lo.value)
  }

  @Test
  fun changesInComponentRegisterAreVisible() {
    val lo = Register(8)
    val hi = Register(8)
    val reg = RegisterPair(hi, lo)

    lo.value = 0xEF_U
    hi.value = 0xBE_U
    assertEquals(0xBEEF_U, reg.value)

  }
}