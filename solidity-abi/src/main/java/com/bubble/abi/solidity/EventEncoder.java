package com.bubble.abi.solidity;

import com.bubble.abi.solidity.datatypes.Event;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.crypto.Hash;
import com.bubble.utils.Numeric;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Ethereum filter encoding. Further limited details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI#events">here</a>.
 * </p>
 */
public class EventEncoder {

	private EventEncoder() {
	}

	public static String encode(Event event) {
		String methodSignature = buildMethodSignature(event.getName(), event.getParameters());
		return buildEventSignature(methodSignature);
	}

	@SuppressWarnings("rawtypes")
	static <T extends Type> String buildMethodSignature(String methodName, List<TypeReference<T>> parameters) {
		StringBuilder result = new StringBuilder();
		result.append(methodName);
		result.append("(");
		String params = parameters.stream().map(p -> Utils.getTypeName(p)).collect(Collectors.joining(","));
		result.append(params);
		result.append(")");
		return result.toString();
	}

	public static String buildEventSignature(String methodSignature) {
		byte[] input = methodSignature.getBytes();
		byte[] hash = Hash.sha3(input);
		return Numeric.toHexString(hash);
	}
}
