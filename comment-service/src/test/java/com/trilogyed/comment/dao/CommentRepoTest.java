package com.trilogyed.comment.dao;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentRepoTest {

    @Autowired
    CommentRepo repo;

    @Before
    public void setUp() {
        repo.deleteAll();
    }
}
