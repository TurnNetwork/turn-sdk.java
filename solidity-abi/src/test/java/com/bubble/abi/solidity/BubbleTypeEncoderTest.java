package com.bubble.abi.solidity;

import com.bubble.abi.solidity.datatypes.Int;
import com.bubble.abi.solidity.datatypes.Uint;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.abi.solidity.datatypes.generated.Int64;
import com.bubble.abi.solidity.datatypes.generated.Uint64;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class BubbleTypeEncoderTest {


    @Test
    public void testUintEncode() {
        Uint zero = new Uint64(BigInteger.ZERO);
        assertThat(BubbleTypeEncoder.encode(zero),
                is("0000000000000000"));

        Uint maxLong = new Uint64(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(BubbleTypeEncoder.encode(maxLong),
                is("7fffffffffffffff"));	
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidUintEncode() {
        new Uint64(BigInteger.valueOf(-1));
    }


    @Test
    public void testIntEncode() {
        Int zero = new Int64(BigInteger.ZERO);
        assertThat(BubbleTypeEncoder.encode(zero),
                is("0000000000000000"));

        Int maxLong = new Int64(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(BubbleTypeEncoder.encode(maxLong),
                is("7fffffffffffffff"));
        
        Int minLong = new Int64(BigInteger.valueOf(Long.MIN_VALUE));
        assertThat(BubbleTypeEncoder.encode(minLong),
                is("8000000000000000"));

        Int minusOne = new Int64(BigInteger.valueOf(-1));
        assertThat(BubbleTypeEncoder.encode(minusOne),
                is("ffffffffffffffff"));
    }

    @Test
    public void testUtf8String() {
        Utf8String string = new Utf8String("Hello, world!");
        assertThat(BubbleTypeEncoder.encode(string),
                is("48656c6c6f2c20776f726c6421"));
    }
}
