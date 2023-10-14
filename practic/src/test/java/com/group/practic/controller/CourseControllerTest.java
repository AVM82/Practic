package com.group.practic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.practic.dto.ChapterDto;
import com.group.practic.entity.*;
import com.group.practic.repository.CourseRepository;
import com.group.practic.security.jwt.TokenProvider;
import com.group.practic.service.CourseService;
import com.group.practic.service.LevelService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentChapterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;
    @MockBean
    private StudentChapterService studentChapterService;
    @MockBean
    private PersonService personService;

    @MockBean
    private TokenProvider tokenProvider;
    @Mock
    private Authentication authentication;
    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private LevelService levelService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})

    public void testGet() throws Exception {
        List<CourseEntity> courses = Arrays.asList(
                new CourseEntity("slug1", "ShortName1", "Course Name1", "svg1"),
                new CourseEntity("slug2", "ShortName2", "Course Name2", "svg2")
        );
        when(courseService.get()).thenReturn(courses);
        mockMvc.perform(get("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})

    public void testGetChapters() throws Exception {
        List<ChapterDto> chapters = Arrays.asList(
                new ChapterDto(1, "Chapter 1"),
                new ChapterDto(2, "Chapter 2")
        );
        when(courseService.getChapters("slug")).thenReturn(chapters);

        mockMvc.perform(get("/api/courses/slug/allchapters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(courseService.getChapters("slug").size(), 2);
        assertEquals(courseService.getChapters("slug"), chapters);


    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})

    public void testGetLevels() throws Exception {
        CourseEntity course = new CourseEntity("slug1", "ShortName1", "Course Name1", "svg1");
        LevelEntity level1 = new LevelEntity(1L, course, 1, Collections.emptyList());
        LevelEntity level2 = new LevelEntity(2L, course, 2, Collections.emptyList());

        when(courseRepository.findBySlug("slug1")).thenReturn(Optional.of(course));
        when(levelService.getAll(course)).thenReturn(List.of(level1, level2));
        mockMvc.perform(get("/api/courses/slug1/levels")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("practically.programming")));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})

    public void testGetPurpose() throws Exception {
        String expectedPurpose = "programming";

        when(courseService.getPurpose(1L)).thenReturn(Optional.of((expectedPurpose)));

        mockMvc.perform(get("/api/courses/1/purpose")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(expectedPurpose)));

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetDescription() throws Exception {
        String expectedDescription = "programming";
        when(courseService.getDescription(1L)).thenReturn(Optional.of(expectedDescription));
        mockMvc.perform(get("/api/courses/1/description")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(expectedDescription)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAdditional() throws Exception {
        Collection<AdditionalMaterialsEntity> expectedMaterials = new ArrayList<>();
        CourseEntity course1 = new CourseEntity("slug1", "ShortName1", "Course Name1", "svg1");
        CourseEntity course2 = new CourseEntity("slug2", "ShortName2", "Course Name2", "svg2");

        AdditionalMaterialsEntity material1 = new AdditionalMaterialsEntity(1L, course1, 1, "Material 1", null);
        AdditionalMaterialsEntity material2 = new AdditionalMaterialsEntity(2L, course2, 2, "Material 2", null);

        expectedMaterials.add(material1);
        expectedMaterials.add(material2);

        when(courseService.getAdditional("slug1")).thenReturn(new HashSet<>(expectedMaterials));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/slug1/additional")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("programming")));
        assertEquals(String.valueOf(courseService.getAdditional("slug1").size()), String.valueOf(2));
        assertTrue(Boolean.parseBoolean(String.valueOf(courseService.getAdditional("slug1").contains(material1))));

    }


    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testCreateCourseAsUser() throws Exception {
        mockMvc.perform(post("/NewCourseFromProperties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testCreateCourseAsAdmin() throws Exception {
//        CourseEntity courseEntity = new CourseEntity();
//        courseEntity.setName("Test Course");
//        courseEntity.setShortName("TestCourse");
//
//        String courseJson = objectMapper.writeValueAsString(courseEntity);
//
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
//                .post("/api/courses")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(courseJson));
//
//        result.andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testChangeShortNameAsUser() throws Exception {
        long courseId = 1L;
        String newShortName = "NewShortName";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/courses/" + courseId + "/change/shortNname")
                .param("shortName", newShortName)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})

    public void testGetBySlug() throws Exception {
        CourseEntity testCourse = new CourseEntity();
        testCourse.setSlug("test-course-slug");
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/courses/" + testCourse.getSlug())
                .contentType(MediaType.TEXT_HTML));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("programming")));
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetChapterByNumberAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/courses/test-course-slug/chapters/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
@Test
@WithMockUser(username = "admin", roles = {"ADMIN"})
public void testGetChapterByNumberAsAdmin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/courses/test-course-slug/chapters/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
}
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetChapterByNumberAsAdminOne() throws Exception {
        mockMvc.perform(get("/api/courses/test-course-slug/chapters/1"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})

    public void testGetChapterByNumberAsAdminTwo() throws Exception {
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(1L);
        when(courseService.getChapterByNumber("test-course-slug", 1))
                .thenReturn(Optional.of(chapter));

        mockMvc.perform(get("/api/courses/test-course-slug/chapters/1"))
                .andExpect(status().isOk());
    }

}