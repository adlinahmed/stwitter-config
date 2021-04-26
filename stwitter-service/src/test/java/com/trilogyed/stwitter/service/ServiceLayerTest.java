package com.trilogyed.stwitter.service;

import com.trilogyed.stwitter.domain.Post;
import com.trilogyed.stwitter.exception.NoSuchPostException;
import com.trilogyed.stwitter.util.feign.CommentClient;
import com.trilogyed.stwitter.util.feign.PostClient;
import feign.FeignException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ServiceLayerTest {

    private static final int POST_ID_THAT_EXISTS = 20001;
    private static final int ID_THAT_DOES_NOT_EXIST = 1;
    private static final String POSTER_THAT_EXISTS = "Adlin";
    private static final String POSTER_THAT_DOES_NOT_EXIST = "doctor";
    private static final Post POST_1 = new Post(20001, LocalDate.of(2021,04,23), POSTER_THAT_EXISTS, "Pulling an all-nighter to finish my summative");
    private static final Post POST_2 = new Post(20001, LocalDate.of(2021,04,23), POSTER_THAT_EXISTS, "Will I finish it");
    private static final Post POST_WITH_INVALID_ID = new Post(ID_THAT_DOES_NOT_EXIST, "wlly wonka", "what");

    private ServiceLayer service;
    private CommentClient commentClient;
    private PostClient postClient;

    @Before
    public void setUp() throws Exception {
        setUpFeignClientMock();
        service = new ServiceLayer(postClient);
    }

    private void setUpFeignClientMock() {
        postClient = mock(PostClient.class);

        doReturn(POST_1).when(postClient).getPost(POST_ID_THAT_EXISTS);
        doThrow(new FeignException.NotFound("Status 404", null)).when(postClient).getPost(ID_THAT_DOES_NOT_EXIST);
        doReturn(POST_1).when(postClient).updatePost(POST_ID_THAT_EXISTS, POST_1);

        List<Post> returnList = new ArrayList<>();
        returnList.add(POST_1);
        returnList.add(POST_2);
        doReturn(returnList).when(postClient).getPostsByPoster(POSTER_THAT_EXISTS);

        doReturn(new ArrayList()).when(postClient).getPostsByPoster(POSTER_THAT_DOES_NOT_EXIST);
    }

    @Test
    public void shouldUpdatePost() {


        Post returnVal = service.updatePost(POST_1);

        assertEquals(returnVal, POST_1);
    }

    @Test(expected = NoSuchPostException.class)
    public void shouldThrowExceptionWhenUpdatePostWithBadId() {


        Post returnVal = service.updatePost(POST_WITH_INVALID_ID);
        fail("Should have thrown an exception");
    }

    @Test
    public void shouldGetPostByPoster() {

        List<Post> returnVal = service.getPostByPoster(POSTER_THAT_EXISTS);

        assertEquals(2, returnVal.size());
    }

    @Test
    public void shouldReturnEmptyListWhenGetPostByPosterReturnsNoData() {

        //arrange (all the setup above)
        //act
        List<Post> whatIGot = service.getPostByPoster(POSTER_THAT_DOES_NOT_EXIST);
        //assert
        assertEquals(0, whatIGot.size());
    }
}
