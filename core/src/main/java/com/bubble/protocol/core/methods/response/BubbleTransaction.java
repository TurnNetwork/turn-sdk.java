package com.bubble.protocol.core.methods.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.bubble.protocol.ObjectMapperFactory;
import com.bubble.protocol.core.Response;

import java.io.IOException;
import java.util.Optional;

/**
 * Transaction object returned by:
 * <ul>
 * <li>eth_getTransactionByHash</li>
 * <li>eth_getTransactionByBlockHashAndIndex</li>
 * <li>eth_getTransactionByBlockNumberAndIndex</li>
 * </ul>
 *
 * <p>This differs slightly from the request {@link BubbleSendTransaction} Transaction object.</p>
 *
 * <p>See
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash">docs</a>
 * for further details.</p>
 */
public class BubbleTransaction extends Response<Transaction> {

    public Optional<Transaction> getTransaction() {
        return Optional.ofNullable(getResult());
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Transaction> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Transaction deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Transaction.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
