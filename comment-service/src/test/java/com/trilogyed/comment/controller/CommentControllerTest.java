package com.trilogyed.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.comment.dao.CommentRepo;
import com.trilogyed.comment.dto.Comment;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepo repo;

    public static final Comment COMMENT_TO_SAVE_1 = new Comment(20001, LocalDate.of(2021,04,23), "Adlin", "Wow, so cool");
    public static final Comment SAVED_COMMENT_1 = new Comment(10001, 20001, LocalDate.of(2021,04,23), "Adlin","Wow, so cool");
    public static final String SAVED_COMMENT_2_COMMENT = "Wow, not cool";
    public static final String COMMENT_WITH_NO_RESULTS = "willy wonka";
    public static final Comment SAVED_COMMENT_2 = new Comment(10002, 20002, LocalDate.of(2021,04,24), "Adlin",SAVED_COMMENT_2_COMMENT);
    public static final int GOOD_COMMENT_ID = 10001;
    public static final int BAD_COMMENT_ID = 1;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        when(repo.save(COMMENT_TO_SAVE_1)).thenReturn(SAVED_COMMENT_1);
        when(repo.save(SAVED_COMMENT_1)).thenReturn(SAVED_COMMENT_1);
        when(repo.findById(GOOD_COMMENT_ID)).thenReturn(Optional.of(SAVED_COMMENT_1));
        when(repo.findById(BAD_COMMENT_ID)).thenReturn(Optional.empty());

        List<Comment> commentList = new ArrayList<>();
        commentList.add(SAVED_COMMENT_1);
        commentList.add(SAVED_COMMENT_2);
        when(repo.findAll()).thenReturn(commentList);
    }

    @Test
    public void shouldCreateComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
        .post("/comment")
        .content(mapper.writeValueAsString(COMMENT_TO_SAVE_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void shouldGetCommentByIdWhenCommentExists() throws Exception {

        String outputJson = mapper.writeValueAsString(SAVED_COMMENT_1);
        mockMvc
                .perform(get("/comment/{comment_id}", GOOD_COMMENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        outputJson
                ));
    }

    @Test
    public void shouldReturnNotFoundWhenGetCommentByIdWhereCommentDoesNotExist() throws Exception {

        String outputJson = mapper.writeValueAsString(SAVED_COMMENT_1);
        mockMvc
                .perform(get("/comment/{comment_id}", BAD_COMMENT_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Invalid id")));
    }

    @Test
    public void shouldUpdateComment() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/comment/{comment_id}", SAVED_COMMENT_1.getCommentId())
                .content(mapper.writeValueAsString(SAVED_COMMENT_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("" + SAVED_COMMENT_1.getCommentId()))
                .andExpect(jsonPath("$.commenter_name").value(SAVED_COMMENT_1.getCommenterName()))
                .andExpect(jsonPath("$.create_date").value(SAVED_COMMENT_1.getCreateDate()));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdateCommentWithNonExistentId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/comment/{comment_id}", BAD_COMMENT_ID)
                .content(mapper.writeValueAsString(SAVED_COMMENT_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Invalid id:")));
    }

    @Test
    public void shouldGetAllComments() throws Exception {

        List<Comment> equipmentLocationList = new ArrayList<>();
        equipmentLocationList.add(SAVED_COMMENT_1);
        equipmentLocationList.add(SAVED_COMMENT_2);
        String jsonOutput = mapper.writeValueAsString(equipmentLocationList);

        mockMvc
                .perform(get("/comment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        jsonOutput
                ));
    }

    @Test
    public void shouldReturnEmptyListWhenGetAllCommentsWithNoResults() throws Exception {

        // arrange
        when(repo.findAll()).thenReturn(new ArrayList<Comment>());

        List<Comment> equipmentLocationList = new ArrayList<>();
        String jsonOutput = mapper.writeValueAsString(equipmentLocationList);

        // act and assert
        mockMvc
                .perform(get("/comment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        jsonOutput
                ));
    }

    @Test
    public void shouldReturnNoContentWhenDeleteComment() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/comment/{comment_id}", GOOD_COMMENT_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
