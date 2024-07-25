/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package io.temporal.samples.tracing;

import io.temporal.api.common.v1.Payload;
import io.temporal.common.context.ContextPropagator;
import io.temporal.common.converter.DefaultDataConverter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.slf4j.MDC;

public class TestContextPropagator implements ContextPropagator {

  @Override
  public String getName() {
    return this.getClass().getName();
  }

  @Override
  public Map<String, Payload> serializeContext(Object context) {
    // System.out.println("Serialize context: " + context);
    String testKey = (String) context;
    if (testKey != null) {
      Optional<Payload> payload = DefaultDataConverter.STANDARD_INSTANCE.toPayload(testKey);
      // System.out.println("Serialize Payload: " + payload.get());

      return Collections.singletonMap("test", payload.get());
    } else {
      return Collections.emptyMap();
    }
  }

  @Override
  public Object deserializeContext(Map<String, Payload> context) {
    // System.out.println("Deserialize context: " + context);
    if (context.containsKey("test")) {
      return DefaultDataConverter.STANDARD_INSTANCE.fromPayload(
          context.get("test"), String.class, String.class);

    } else {
      return null;
    }
  }

  @Override
  public Object getCurrentContext() {
    // System.out.println("getCurrentContext");
    return MDC.get("test");
  }

  @Override
  public void setCurrentContext(Object context) {
    // System.out.println("setCurrentContext: " + context);
    MDC.put("test", String.valueOf(context));
  }
}
