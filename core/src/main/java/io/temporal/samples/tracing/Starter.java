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

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.tracing.workflow.TracingWorkflow;
import io.temporal.serviceclient.WorkflowServiceStubs;
import java.util.Collections;
import org.slf4j.MDC;

public class Starter {
  public static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
  public static final String TASK_QUEUE_NAME = "tracingTaskQueue";

  public static void main(String[] args) throws InterruptedException {

    MDC.put("test", "testing123");
    WorkflowClientOptions clientOptions =
        WorkflowClientOptions.newBuilder()
            .setContextPropagators(Collections.singletonList(new TestContextPropagator()))
            .build();
    WorkflowClient client = WorkflowClient.newInstance(service, clientOptions);

    WorkflowOptions workflowOptions =
        WorkflowOptions.newBuilder()
            .setWorkflowId("tracingWorkflow")
            .setTaskQueue(TASK_QUEUE_NAME)
            .build();

    // Create typed workflow stub
    TracingWorkflow workflow = client.newWorkflowStub(TracingWorkflow.class, workflowOptions);

    String greeting = workflow.greet("Hello");

    System.out.println("Greeting: " + greeting);
    Thread.sleep(10000);

    System.exit(0);
  }
}
