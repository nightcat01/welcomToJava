package com.nightcat.study;

import com.nightcat.study.hash.Question1;
import com.nightcat.study.hash.Question2;
import com.nightcat.study.hash.Question3;
import com.nightcat.study.test.level1.Level1Q1;
import com.nightcat.study.test.level1.Level1Q2;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LevelTest {

    /**
     * 문제 및 정답은 각 class에서 확인
     */

    @Test
    void levelQ1(){
        Level1Q1 q = new Level1Q1();
        q.solution("+1234");
    }


}
