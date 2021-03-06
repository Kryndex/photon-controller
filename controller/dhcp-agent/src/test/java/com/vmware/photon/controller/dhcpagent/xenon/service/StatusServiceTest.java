/*
 * Copyright 2015 VMware, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.vmware.photon.controller.dhcpagent.xenon.service;

import com.vmware.photon.controller.dhcpagent.dhcpdrivers.DHCPDriver;
import com.vmware.photon.controller.dhcpagent.xenon.helpers.TestEnvironment;
import com.vmware.photon.controller.status.gen.Status;
import com.vmware.photon.controller.status.gen.StatusType;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Executors;

/**
 * Tests {@link StatusService}.
 */
public class StatusServiceTest {

  /**
   * Dummy test case to make Intellij recognize this as a test class.
   */
  @Test
  private void dummy() {
  }

  /**
   * Tests {@link StatusService#handleGet(com.vmware.xenon.common.Operation)}.
   */
  public class HandleGetTest {

    private TestEnvironment testEnvironment;

    private ListeningExecutorService listeningExecutorService;

    private DHCPDriver dhcpDriver;

    @BeforeMethod
    public void setUp() throws Throwable {
      dhcpDriver = mock(DHCPDriver.class);
      listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
      testEnvironment = TestEnvironment.create(dhcpDriver , 1, listeningExecutorService);
    }

    @AfterMethod
    public void tearDown() throws Throwable {
      testEnvironment.stop();
      testEnvironment = null;
    }

    @AfterClass
    public void tearDownClass() throws Throwable {
      listeningExecutorService.shutdown();
    }

    @Test
    public void testReady() throws Throwable {
      doReturn(true).when(dhcpDriver).isRunning();

      Status status = testEnvironment.getServiceState(StatusService.SELF_LINK, Status.class);

      assertThat(status.getType(), is(StatusType.READY));
    }
  }
}
