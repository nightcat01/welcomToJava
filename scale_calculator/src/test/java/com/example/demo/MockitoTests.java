package com.example.demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockitoTests {

    // mock
    public class Person {
        private String name;
        private int age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    @Test
    public void example(){
        Person p = mock(Person.class);
        Assert.assertTrue( p != null );
    }

    @Mock
    Person p;

    @Test
    public void example1(){
        MockitoAnnotations.initMocks(this);
        Assert.assertTrue(p != null);
    }

    @Test
    public void exampleWhen(){
        Person p = mock(Person.class);
        when(p.getName()).thenReturn("JDM");
        when(p.getAge()).thenReturn(20);
        Assert.assertTrue("JDM".equals(p.getName()));
        Assert.assertTrue(20 == p.getAge());
    }

}
