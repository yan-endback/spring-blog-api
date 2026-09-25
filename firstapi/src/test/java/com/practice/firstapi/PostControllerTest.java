package com.practice.firstapi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
public class PostControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @MockitoBean
    PostService postService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @MockitoBean
    AuthorRepository authorRepository;

    @Test
    void contextLoads(){

    }
    @Test
    void all_returnsTwoPosts() throws Exception{
        when(postService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(
                new PostResponse(1L, "Первый", "Текст один", "murder",
                        LocalDateTime.now(), "murder"),
                new PostResponse(2L, "Второй", "Текст два", "second",
                        LocalDateTime.now(), "second"))
        ));
        mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value("Первый"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
    @Test
    void byId_notFound_returns404() throws Exception{
        when(postService.getById(99L))
                .thenThrow(new PostNotFoundException(99L));
        mockMvc.perform(get("/posts/99"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().
                        contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").exists());
    }
    @Test
    void createdPost_noToken_returns401() throws Exception{
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title":"Тест","body":"Текст"}
                        """))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Нужен токен"));
        verifyNoInteractions(postService);

    }
    @Test
    @WithMockUser(username = "murder")
    void create_asUser_returns201() throws Exception{
        when(postService.create(any(), eq("murder"))).thenReturn(
                new PostResponse(1L,"Тест","Текст","murder",
                        LocalDateTime.now(),"murder")
        );
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Тест\",\"body\":\"Текст\",\"authorId\":1}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Тест"));
        verify(postService).create(any(), eq("murder"));
    }
    @Test
    @WithMockUser(username = "murder",roles = "USER")
    void deleteByAdmin_asUser_returns403() throws Exception{
        mockMvc.perform(delete("/admin/posts/5"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(postService, never()).deleteAnyPost(anyLong());

    }
    @Test
    @WithMockUser(username = "boss",roles = "ADMIN")
    void deleteByAdmin_asAdmin_returns204()throws Exception{
        mockMvc.perform(delete("/admin/posts/5"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(postService).deleteAnyPost(5L);
    }
    @Test
    @WithMockUser(username = "second")
    void update_notOwner_returns403() throws Exception{
        when(postService.update(eq(8L),any()))
                .thenThrow(new AccessDeniedException("Это не ваш пост"));
        mockMvc.perform(put("/posts/8")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"X\",\"body\":\"Y\",\"authorId\":1}"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Это не ваш пост"));
    }
    @Test
    @WithMockUser(username = "murder")
    void create_blankTitle_returns400() throws Exception{
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\",\"body\":\"Текст\",\"authorId\":1}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
        verifyNoInteractions(postService);
    }
    @Test
    void all_passesPageParamsToService() throws Exception {
        when(postService.findAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/posts")
                .param("page", "1")
                .param("size","5")
                .param("sort","title,asc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(postService).findAll(captor.capture());
        Pageable p = captor.getValue();

        assertEquals(1, p.getPageNumber());
        assertEquals(5, p.getPageSize());
        assertEquals(Sort.Direction.ASC, p.getSort().getOrderFor("title").getDirection());
    }
}
