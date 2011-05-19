package net.ttsui.junit.rules.pending;

import org.junit.Rule;
import org.junit.Test;

import junit.framework.Assert;

public class BugWithMultiplePendingAnnotationsTest {
    @Rule public PendingRule rule = new PendingRule();
    
    @PendingImplementation
    @Test public void
    pendingTest1() {
        Assert.fail();
    }
    
    @Test public void
    passingTest() {
        
    }
    
    @Test public void
    failingTest() {
        Assert.fail();
    }

    @PendingImplementation
    @Test public void
    pendingTest2() {
        Assert.fail();
    }
}
