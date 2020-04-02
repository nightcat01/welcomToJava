package ValidatorUtil;

import com.nightcat.framework.demo.DemoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import util.ValidatorUtil;

import java.util.Arrays;
import java.util.logging.Logger;

//@SpringBootTest(classes = DemoApplication.class)
@RunWith(MockitoJUnitRunner.class)
class ValidatorUtilTests {

    private final String[] phoneNum = {"010-0000-0000", "01000000000", "010-1234-5678", "01012345678", "010-123-4567", "0101234567", "010-0123-4567", "01001234567", "010-012-3456", "0100123456"};
    private final String[] email = {"test@test.com", "test1234@test.com", "test_12@test.com"
                                    , "test@test.co.kr", "test1234@test.co.kr", "test_12@test.co.kr"};

    @Test
    void contextLoads() {
        for(String s : Arrays.asList(phoneNum)) {
            System.out.println("phoneNum : " + s + "  ->  " + ValidatorUtil.getMaskedValue(s));
        }
        for(String s : Arrays.asList(email)) {
            System.out.println("email : " + s + "  ->  " + ValidatorUtil.getMaskedValue(s));
        }
    }

}
