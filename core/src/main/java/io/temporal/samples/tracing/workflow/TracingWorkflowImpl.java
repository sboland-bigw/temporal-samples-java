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

package io.temporal.samples.tracing.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.samples.tracing.TestContextPropagator;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.Collections;
import org.slf4j.MDC;

public class TracingWorkflowImpl implements TracingWorkflow {

  private String language = "English";

  @Override
  public String greet(String name) {

    System.out.println("Worker MDC context map: " + MDC.getCopyOfContextMap());

    ActivityOptions activityOptions =
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(2))
            .setContextPropagators(Collections.singletonList(new TestContextPropagator()))
            .build();
    TracingActivities activities =
        Workflow.newActivityStub(TracingActivities.class, activityOptions);

    return activities.greet(name, language);
  }

  @Override
  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public String getLanguage() {
    return language;
  }
}
