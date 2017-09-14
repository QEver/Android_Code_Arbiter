/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.android;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
/**
 * Created by balckarbiter on 17/8/2.
 */
public class StickyBroadcastDetector extends OpcodeStackDetector{
    private static final String ANDROID_STICKY_BROADCAST_TYPE = "ANDROID_STICKY_BROADCAST";
    private BugReporter bugReporter;

    public StickyBroadcastDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        // getClassConstantOperand().equals("java/net/Socket")

        if (seen == Constants.INVOKEVIRTUAL && ( //List of method mark as external file access
                getNameConstantOperand().equals("sendStickyBroadcast") ||
                        getNameConstantOperand().equals("sendStickyOrderedBroadcast") ||
                        getNameConstantOperand().equals("sendStickyBroadcastAsUser") ||
                        getNameConstantOperand().equals("sendStickyOrderedBroadcastAsUser")
        )) {
//            System.out.println(getSigConstantOperand());
            bugReporter.reportBug(new BugInstance(this, ANDROID_STICKY_BROADCAST_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}
