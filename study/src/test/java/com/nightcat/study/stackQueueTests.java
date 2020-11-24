package com.nightcat.study;

import com.nightcat.study.stackQueue.Question1;
import com.nightcat.study.stackQueue.Question2;
import com.nightcat.study.stackQueue.Question3;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class stackQueueTests {

    /**
     * 문제 및 정답은 각 class에서 확인
     */

    @Test
    void question1(){
        Question1 q = new Question1();
        Assert.assertArrayEquals(q.solution(q.heights1), q.answer1);
        Assert.assertArrayEquals(q.solution(q.heights2), q.answer2);
        Assert.assertArrayEquals(q.solution(q.heights3), q.answer3);
        Assert.assertArrayEquals(q.solution(q.heights4), q.answer4);
    }

    @Test
    void question2(){
        Question2 q = new Question2();
        Assert.assertEquals(q.solution(q.bridge_length1, q.weight1, q.truck_weights1), q.answer1);
        Assert.assertEquals(q.solution(q.bridge_length2, q.weight2, q.truck_weights2), q.answer2);
        Assert.assertEquals(q.solution(q.bridge_length3, q.weight3, q.truck_weights3), q.answer3);
    }

    @Test
    void question3(){
        Question3 q = new Question3();
        Assert.assertEquals(q.solution(q.priorities1, q.location1), q.answer1);
    }



}
