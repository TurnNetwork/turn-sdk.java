/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.bubble.protocol.core.methods.response;


import com.bubble.protocol.core.Response;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

/** bubble_chainId. */
public class BubbleChainId extends Response<String> {
    public BigInteger getChainId() {
        return Numeric.decodeQuantity(getResult());
    }
}
