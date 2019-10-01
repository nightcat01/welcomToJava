package com.example.demo;

import com.example.demo.common.Constant;
import com.example.demo.domain.Scale;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        Scale scale = mock(Scale.class);
        when(scale.getResult()).thenReturn("no result");
        Assert.assertTrue("no result".equals(scale.getResult()));
    }

    @Test
    public void enumTest() {
        System.out.println(com.example.demo.common.Scale.DO.getScale());
        System.out.println(com.example.demo.common.Scale.RE.toString());
    }

}
