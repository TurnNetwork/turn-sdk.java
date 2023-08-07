package com.bubble.abi.wasm.datatypes;


import com.bubble.parameters.NetworkParameters;
import com.bubble.utils.Numeric;

import java.math.BigInteger;
import java.util.Objects;

public class WasmAddress {
	private byte[] value;
	private BigInteger bigIntValue;
	private String address;

	public static final int LENGTH = 160;
	public static final int LENGTH_IN_HEX = LENGTH >> 2;

	public WasmAddress(byte[] value) {
		this.value = value;
		this.bigIntValue = Numeric.toBigInt(value);
		this.address = Numeric.toHexStringWithPrefixZeroPadded(bigIntValue, LENGTH_IN_HEX);
	}

	public WasmAddress(String value) {
		this.value = Numeric.hexStringToByteArray(value);
		this.bigIntValue = Numeric.toBigInt(value);
		this.address = value;
	}

	public WasmAddress(BigInteger value) {
		this(value.toByteArray());
	}

	public byte[] getValue() {
		return value;
	}

	public String getAddress() {
		return address;
	}

	public BigInteger getBigIntValue() {
		return bigIntValue;
	}

	@Override
	public String toString() {
		return address;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WasmAddress address = (WasmAddress) o;
		return Objects.equals(bigIntValue, address.bigIntValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bigIntValue);
	}
}