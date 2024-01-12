package com.bubble.abi.solidity.datatypes;

import com.bubble.abi.solidity.datatypes.generated.Uint160;

import com.bubble.parameters.NetworkParameters;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

/**
 * Address type, which is equivalent to uint160.
 */
public class Address implements Type<String> {

    public static final String TYPE_NAME = "address";
    public static final int LENGTH = 160;
    public static final int LENGTH_IN_HEX = LENGTH >> 2;

    private final Uint160 value;
    private final String address;

    public Address(Uint160 value) {
        this.value = value;
        this.address = Numeric.toHexStringWithPrefixZeroPadded(value.getValue(), LENGTH_IN_HEX);;
    }

    public Address(BigInteger inputValue) {
        this.value = new Uint160(inputValue);
        this.address = Numeric.toHexStringWithPrefixZeroPadded(inputValue, LENGTH_IN_HEX);
    }

    public Address(String value) {
        this.value = new Uint160(Numeric.toBigInt(value));
        this.address = value;
    }

    public Uint160 toUint160() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        return value != null ? value.equals(address.value) : address.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
