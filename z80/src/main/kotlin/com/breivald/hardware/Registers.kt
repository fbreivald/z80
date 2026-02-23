package com.breivald.hardware

interface IRegister: Iterable<Boolean> {
  var value: UInt
  val size: Int
  val mask: UInt
  infix fun shl(bitCount: Int): IRegister
  infix fun shr(bitCount: Int): IRegister
  operator fun get(i: Int): Boolean
  operator fun set(i: Int, bit: Boolean)
  operator fun inc(): IRegister
  override operator fun iterator(): Iterator<Boolean>
}

open class Register(final override val size: Int, initial: UInt = 0U): IRegister {
  protected var value_: UInt = 0U
  override val mask = mask()

  override var value: UInt
    get() = value_
    set(value) { value_ = value and mask }

  init {
    value_ = initial
  }

  constructor(initial: UByte) : this(8, initial.toUInt())
  constructor(initial: UShort) : this(16, initial.toUInt())

  private fun mask(): UInt {
    var mask_ = 1U
    for (i in 1 until size) {
      mask_ = mask_ or (mask_ shl 1)
    }
    return mask_
  }
  override infix fun shl(bitCount: Int): IRegister {
    value = value shl bitCount
    return this
  }

  override infix fun shr(bitCount: Int): IRegister {
    value = value shr bitCount
    return this
  }

  override fun get(i: Int): Boolean {
    if (0 > i || i > size - 1) throw IndexOutOfBoundsException()
    val mask = 1U shl i
    return (value and mask) != 0U
  }

  override fun set(i: Int, bit: Boolean) {
    if (0 > i || i > size - 1) throw IndexOutOfBoundsException()
    val mask = 1U shl i
    value = if (bit) value or mask else value and mask.inv()
  }

  override fun inc(): IRegister {
    value++
    return this
  }

  override fun iterator(): Iterator<Boolean> {
    return object : Iterator<Boolean> {
      var i = size - 1
      override fun hasNext(): Boolean = i >= 0
      override fun next(): Boolean = value_ and (1U shl i--) != 0U
    }
  }

  override fun toString(): String {
    return String.format("%1d (%1$#0${2+size/4}x)", value.toInt())
  }
}

/**
 * A pair of registers that can is treated as a single register, but still allowing its constituent registers to
 * be accessible. The value of the register will be the concatenation of the two registers, with the `high` register
 * occupying the high order bits. Setting a value on a `RegisterPair` will set the corresponding bits on the two
 * component registers.
 */
class RegisterPair(val high: IRegister, val low: IRegister): Register(low.size + high.size) {
  override var value: UInt
    get() = (high.value shl high.size) or low.value
    set(value) {
      low.value = (value and low.mask)
      high.value = ((value and low.mask.inv()) shr low.size)
    }

  override operator fun iterator(): Iterator<Boolean> {
    return object: Iterator<Boolean> {
      val highIterator = high.iterator()
      val lowIterator = low.iterator()

      override fun hasNext(): Boolean {
        return highIterator.hasNext() or lowIterator.hasNext()
      }

      override fun next(): Boolean {
        return if (highIterator.hasNext()) {
          highIterator.next()
        } else
          lowIterator.next()
      }
    }
  }
}