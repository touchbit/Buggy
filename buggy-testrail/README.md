# RestRail module

```java
public abstract class MyTest extends BaseBuggyTest {

        @BeforeMethod
        public void attributeHandler(ITestResult result) {
            Suite suite = result.getTestClass()
                    .getRealClass().getDeclaredAnnotation(Suite.class);
            if (suite == null) {
                return;
            }
            if (suite.service().equals(MyService.class)) {
                result.setAttribute(TESTRAIL_RUN_ID, 33L);
            }
        }

}
```