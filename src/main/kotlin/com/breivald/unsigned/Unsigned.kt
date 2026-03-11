package com.breivald.unsigned

const val UBYTE_0: UByte = 0U
const val UBYTE_1: UByte = 1U
const val USHORT_0: UShort = 0U
const val USHORT_1: UShort = 1U

// UByte
infix fun UByte.shl(bitCount: Int): UByte {
  return (this.toInt() shl bitCount).toUByte()
}

infix fun UByte.shr(bitCount: Int): UByte {
  return (this.toInt() shr bitCount).toUByte()
}

// UShort
infix fun UShort.shl(bitCount: Int): UShort {
  return (this.toInt() shl bitCount).toUShort()
}

infix fun UShort.shr(bitCount: Int): UShort {
  return (this.toInt() shr bitCount).toUShort()
}