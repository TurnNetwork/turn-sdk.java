package com.bubble.abi.solidity.datatypes.generated;

import com.bubble.abi.solidity.datatypes.StaticArray;
import com.bubble.abi.solidity.datatypes.Type;

import java.util.List;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray32<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray32(List<T> values) {
        super(32, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray32(T... values) {
        super(32, values);
    }

    public StaticArray32(Class<T> type, List<T> values) {
        super(type, 32, values);
    }

    @SafeVarargs
    public StaticArray32(Class<T> type, T... values) {
        super(type, 32, values);
    }
}
