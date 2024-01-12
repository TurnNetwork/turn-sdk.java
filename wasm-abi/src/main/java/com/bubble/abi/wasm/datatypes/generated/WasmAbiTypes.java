package com.bubble.abi.wasm.datatypes.generated;

import com.bubble.rlp.wasm.datatypes.*;

import java.math.BigInteger;

public final class WasmAbiTypes {
	private WasmAbiTypes() {
	}

	public static Class<?> getType(String type) {
		switch (type) {
		case "address":
			return String.class;
		case "bool":
			return Boolean.class;
		case "string":
			return String.class;
		case "uint8":
			return Uint8.class;
		case "int8":
			return Int8.class;
		case "uint16":
			return Uint16.class;
		case "int16":
			return Int16.class;
		case "uint32":
			return Uint32.class;
		case "int32":
			return Int32.class;
		case "uint64":
			return Uint64.class;
		case "int64":
			return Int64.class;
		case "uint128":
			return Uint128.class;
		case "int128":
			return Int128.class;
		case "uint160":
			return BigInteger.class;
		case "uint256":
			return BigInteger.class;
		case "uint512":
			return BigInteger.class;
		case "float":
			return Float.class;
		case "double":
			return Double.class;
		default:
			throw new UnsupportedOperationException("Unsupported type encountered: " + type);
		}
	}

	public static Class<?> getRawType(String type, boolean isVector) {
		switch (type) {
		case "address":
			return String.class;
		case "bool":
			return boolean.class;
		case "string":
			return String.class;
		case "uint8":
			if(isVector){
				return byte.class;
			}
			return Uint8.class;
		case "int8":
			return Int8.class;
		case "uint16":
			return Uint16.class;
		case "int16":
			return Int16.class;
		case "uint32":
			return Uint32.class;
		case "int32":
			return Int32.class;
		case "uint64":
			return Uint64.class;
		case "int64":
			return Int64.class;
		case "uint128":
			return Uint128.class;
		case "int128":
			return Int128.class;
		case "uint160":
			return BigInteger.class;
		case "uint256":
			return BigInteger.class;
		case "uint512":
			return BigInteger.class;
		case "float":
			return float.class;
		case "double":
			return double.class;
		default:
			throw new UnsupportedOperationException("Unsupported type encountered: " + type);
		}
	}
}
