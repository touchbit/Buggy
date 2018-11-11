package org.touchbit.buggy.example.min.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.test.BaseBuggyTest;
import org.touchbit.buggy.example.min.goals.API;
import org.touchbit.buggy.example.min.goals.GitLab;

/**
 * Created by Oleg Shaburov on 31.10.2018
 * shaburov.o.a@gmail.com
 */
@Suite(service = GitLab.class, interfaze = API.class, task = "temp")
public class B extends BaseBuggyTest {

    @BeforeClass(description = "description B_BeforeClass")
    public void B_BeforeClass() {
        step("BeforeClass step");
        step("BeforeClass step");
        throw new RuntimeException("B_BeforeClass RuntimeException ");
    }

//    @DataProvider(name = "test1")
//    public static Object[][] primeNumbers() {
//        return new Object[][] {
//                {2, true},
//                {6, false},
////                {19, true},
////                {22, false},
////                {23, true}
//        };
//    }
//
//    @Test(description = "TODO 4", dataProvider = "test1")
//    @Details
//    public void B_test_20181031164244(Integer inputNumber, Boolean expectedResult) {
//        step("asdadasddsa");
//    }

    @Test(description = "description B1_test_20181031164245")
    @Details
    public void B1_test_20181031164245() {
        step("B_test_20181031164245 step");
        step("B_test_20181031164245 step");
    }

    @Test(description = "description B2_test_20181031164245")
    @Details
    public void B2_test_20181031164245() {
        step("B_test_20181031164245 step");
        step("B_test_20181031164245 step");
    }

}
