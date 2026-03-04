package org.codehaus.gmavenplus.util;

import org.junit.Test;
import java.security.Permission;
import static org.junit.Assert.*;

/**
 * Unit tests for the NoExitSecurityManager class.
 */
public class NoExitSecurityManagerTest {

    @Test(expected = SecurityException.class)
    public void testCheckExit() {
        NoExitSecurityManager sm = new NoExitSecurityManager();
        sm.checkExit(0);
    }

    @Test
    public void testCheckPermissionDelegatesToParent() {
        final boolean[] called = {false};
        SecurityManager parent = new SecurityManager() {
            @Override
            public void checkPermission(Permission perm) {
                called[0] = true;
            }
        };
        NoExitSecurityManager sm = new NoExitSecurityManager(parent);
        sm.checkPermission(new RuntimePermission("test"));
        assertTrue("Parent checkPermission was not called", called[0]);
    }

    @Test
    public void testCheckPermissionWithNullParent() {
        NoExitSecurityManager sm = new NoExitSecurityManager(null);
        sm.checkPermission(new RuntimePermission("test"));
        // Should not throw exception
    }

    @Test
    public void testConstructorWithNoArgs() {
        NoExitSecurityManager sm = new NoExitSecurityManager();
        // Just verify it can be instantiated
        assertNotNull(sm);
    }
}
