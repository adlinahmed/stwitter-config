package com.trilogyed.post.dto;

import com.trilogyed.post.dao.PostRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepoTest {

    @Autowired
    PostRepo repo;

    @Before
    public void setUp() {
        repo.deleteAll();
    }

    @Test
    public void shouldCreateAPost() {
        Post post = new Post(LocalDate.of(2021,04,23), "Adlin", "Pulling an all-nighter to finish my summative");

        repo.save(post);

        List<Post> postList = repo.findAll();

        assertEquals(1, postList.size());
    }

    @Test
    public void shouldReturnOnlyMatchesWhenFindByPoster() {
        Post post = new Post(LocalDate.of(2021,04,23), "Adlin", "Pulling an all-nighter to finish my summative");
        repo.save(post);

        Post post2 = new Post(LocalDate.of(2021,04,24), "Adlin", "Will I finish it");
        repo.save(post2);

        Post post3 = new Post(LocalDate.of(2021,04,24), "Dan", "no you wont");
        repo.save(post3);

        List<Post> postList = repo.findPostByPoster("Adlin");
        assertEquals(2, postList.size());

    }
}
