package it.intesys.codylab.rookie.test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        PatientTest.class,
        DoctorTest.class,
        PatientRecordTest.class
})
public class RookieTest {
}
