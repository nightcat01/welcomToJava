package com.nightcat.study;

import com.nightcat.study.hash.Question1;
import com.nightcat.study.hash.Question2;
import com.nightcat.study.hash.Question3;
import com.nightcat.study.hash.Question4;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyApplicationTests {

    /**
     * 문제 및 정답은 각 class에서 확인
     */

    @Test
    void question1(){
        Question1 q = new Question1();
        q.solution(q.participantGroupFirst, q.completionGroupFirst);
        q.solution(q.participantGroupSecond, q.completionGroupSecond);
        q.solution(q.participantGroupThird, q.completionGroupThird);
    }

    @Test
    void question2(){
        Question2 q = new Question2();
        Assert.assertEquals(q.solution(q.phoneBookGroupFirst), q.groupFirstAnswer);
        Assert.assertEquals(q.solution(q.phoneBookGroupSecond), q.groupSecondAnswer);
        Assert.assertEquals(q.solution(q.phoneBookGroupThird), q.groupThirdAnswer);
    }

    @Test
    void question3(){
        Question3 q = new Question3();
        Assert.assertEquals(q.solution(q.clothGroupFirst), q.groupFirstAnswer);
        Assert.assertEquals(q.solution(q.clothGroupSecond), q.groupSecondAnswer);
    }

    @Test
    void question4(){
        Question4 q = new Question4();
        Assert.assertArrayEquals(q.solution(q.genres, q.plays), q.answer);
        Assert.assertArrayEquals(q.solution(q.genres2, q.plays2), q.answer2);
        Assert.assertArrayEquals(q.solution(q.genres3, q.plays3), q.answer3);
        Assert.assertArrayEquals(q.solution(q.genres4, q.plays4), q.answer4);
        Assert.assertArrayEquals(q.solution(q.genres5, q.plays5), q.answer5);
        Assert.assertArrayEquals(q.solution(q.genres6, q.plays6), q.answer6);
    }

}
