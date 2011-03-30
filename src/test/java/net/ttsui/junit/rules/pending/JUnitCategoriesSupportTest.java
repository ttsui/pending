package net.ttsui.junit.rules.pending;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class JUnitCategoriesSupportTest {
    
    public static class TestMethodsWithJUnitCategory {
        @Rule public PendingRule pendingRule = new PendingRule(PendingCategory.class);
        
        @Category(PendingCategory.class)
        @Test public void
        failingTestWithPendingCategoryShouldPass() {
            fail();
        }
        
        @Category(PendingCategory.class)
        @Test(expected=AssertionError.class) public void
        passingTestWithPendingCategoryShouldFail() {
            assertEquals(1, 1);
        }
        
        @Category(OtherCategory.class)
        @Test public void
        passTestWithOtherCategoryShouldPass() {
            assertEquals(1, 1);
        }
        
        @Category({OtherCategory.class, PendingCategory.class})
        @Test public void
        failingTestWithMultipleCategoriesIncludingPendingShouldPass() {
            fail();
        }
        
        @Category({OtherCategory.class, PendingCategory.class})
        @Test(expected=AssertionError.class) public void
        passingTestWithMultipleCategoriesIncludingPendingShouldFail() {
            assertEquals(1, 1);
        }
    }

    @Category(PendingCategory.class)
    public static class TestClassWithJUnitCategory {
        @Rule public PendingRule pendingRule = new PendingRule(PendingCategory.class);
        
        @Test public void
        failingTestWithPendingCategoryShouldPass() {
            fail();
        }
        
        @Test(expected=AssertionError.class) public void
        passingTestWithPendingCategoryShouldFail() {
            assertEquals(1, 1);
        }
    } 
}
