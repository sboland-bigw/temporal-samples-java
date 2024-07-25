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
import io.temporal.samples.tracing.workflow.TracingActivitiesImpl;
import io.temporal.samples.tracing.workflow.TracingWorkflowImpl;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.util.Collections;

public class TracingWorker {
  public static final String TASK_QUEUE_NAME = "tracingTaskQueue";

  public static void main(String[] args) {

    final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    WorkflowClientOptions workflowClientOptions =
        WorkflowClientOptions.newBuilder()
            .setContextPropagators(Collections.singletonList(new TestContextPropagator()))
            .build();
    final WorkflowClient client = WorkflowClient.newInstance(service, workflowClientOptions);
    WorkerFactory factory = WorkerFactory.newInstance(client);

    Worker worker = factory.newWorker(TASK_QUEUE_NAME);
    worker.registerWorkflowImplementationTypes(TracingWorkflowImpl.class);
    worker.registerActivitiesImplementations(new TracingActivitiesImpl());

    factory.start();
  }
}
