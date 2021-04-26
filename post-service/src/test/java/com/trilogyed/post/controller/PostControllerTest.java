package com.trilogyed.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.post.dao.PostRepo;
import com.trilogyed.post.dto.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepo repo;

    public static final Post POST_TO_SAVE_1 = new Post(LocalDate.of(2021,04,23), "Adlin", "Pulling an all-nighter to finish my summative");
    public static final Post SAVED_POST_1 = new Post(20001, LocalDate.of(2021,04,23), "Adlin", "Pulling an all-nighter to finish my summative");
    public static final String SAVED_POST_2_POSTER = "Adlin";
    public static final String POSTER_WITH_NO_RESULTS = "Pocahontas";
    public static final Post SAVED_POST_2 = new Post(20002,LocalDate.of(2021,04,24), SAVED_POST_2_POSTER, "Will I finish it");
    public static final int GOOD_POST_ID = 20001;
    public static final int BAD_POST_ID = 1;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        when(repo.save(POST_TO_SAVE_1)).thenReturn(SAVED_POST_1);
        when(repo.save(SAVED_POST_1)).thenReturn(SAVED_POST_1);
        when(repo.findById(GOOD_POST_ID)).thenReturn(Optional.of(SAVED_POST_1));
        when(repo.findById(BAD_POST_ID)).thenReturn(Optional.empty());

        List<Post> postList = new ArrayList<>();
        postList.add(SAVED_POST_1);
        postList.add(SAVED_POST_2);
        when(repo.findAll()).thenReturn(postList);

        List<Post> listThatMatchesPosterOf2 = new ArrayList<>();
        listThatMatchesPosterOf2.add(SAVED_POST_2);
        when(repo.findPostByPoster(SAVED_POST_2_POSTER)).thenReturn(listThatMatchesPosterOf2);

        when(repo.findPostByPoster(POSTER_WITH_NO_RESULTS)).thenReturn(new ArrayList<Post>());
    }

    @Test
    public void shouldCreatePost() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/post")
                .content(mapper.writeValueAsString(POST_TO_SAVE_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void shouldGetPostByIdWhenPostExists() throws Exception {

        String outputJson = mapper.writeValueAsString(SAVED_POST_1);
        mockMvc
                .perform(get("/post/{id}", GOOD_POST_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        outputJson
                ));
    }

    @Test
    public void shouldReturnNotFoundWhenGetPostByIdWherePostDoesNotExist() throws Exception {

        String outputJson = mapper.writeValueAsString(SAVED_POST_1);
        mockMvc
                .perform(get("/post/{id}", BAD_POST_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Invalid id")));
    }

    @Test
        public void shouldUpdatePost() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/post/{id}", SAVED_POST_1.getPostID())
                .content(mapper.writeValueAsString(SAVED_POST_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("" + SAVED_POST_1.getPostID()))
                .andExpect(jsonPath("$.poster_name").value(SAVED_POST_1.getPosterName()))
                .andExpect(jsonPath("$.post").value(SAVED_POST_1.getPost()));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatePostWithNonExistentId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/post/{id}", BAD_POST_ID)
                .content(mapper.writeValueAsString(SAVED_POST_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Invalid id:")));
    }

    @Test
    public void shouldGetAlLpOSTs() throws Exception {

        List<Post> equipmentLocationList = new ArrayList<>();
        equipmentLocationList.add(SAVED_POST_1);
        equipmentLocationList.add(SAVED_POST_2);
        String jsonOutput = mapper.writeValueAsString(equipmentLocationList);

        mockMvc
                .perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        jsonOutput
                ));
    }

    @Test
    public void shouldReturnEmptyListWhenGetAllPostsWithNoResults() throws Exception {

        // arrange
        when(repo.findAll()).thenReturn(new ArrayList<Post>());

        List<Post> postList = new ArrayList<>();
        String jsonOutput = mapper.writeValueAsString(postList);

        // act and assert
        mockMvc
                .perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        jsonOutput
                ));
    }

    @Test
    public void shouldReturnNoContentWhenDeletePost() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/post/{id}", GOOD_POST_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldGetPostByPoster() throws Exception {

        List<Post> postList = new ArrayList<>();
        postList.add(SAVED_POST_2);
        String jsonOutput = mapper.writeValueAsString(postList);

        mockMvc
                .perform(get("/post/poster_name/{poster_name}",SAVED_POST_2_POSTER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        jsonOutput
                ));
    }

    @Test
    public void shouldReturnEmptyListWhenGetPostByPosterAndThereAreNoResults() throws Exception {

        List<Post> postList = new ArrayList<>();
        String jsonOutput = mapper.writeValueAsString(postList);

        mockMvc
                .perform(get("/post/poster_name/{poster_name}",POSTER_WITH_NO_RESULTS))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        jsonOutput
                ));
    }

}
