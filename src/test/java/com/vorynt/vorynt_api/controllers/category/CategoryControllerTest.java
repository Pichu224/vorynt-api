package com.vorynt.vorynt_api.controllers.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.dtos.Category.CategoryResponse;
import com.vorynt.vorynt_api.dtos.Category.CreateCategoryRequest;
import com.vorynt.vorynt_api.dtos.Category.UpdateCategoryRequest;
import com.vorynt.vorynt_api.handlers.GlobalExceptionHandler;
import com.vorynt.vorynt_api.mappers.CategoryMapper;
import com.vorynt.vorynt_api.security.JwtAuthenticationEntryPoint;
import com.vorynt.vorynt_api.security.JwtAuthenticationFilter;
import com.vorynt.vorynt_api.services.category.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    void shouldCreateCategorySuccessfully() throws Exception {

        CreateCategoryRequest request = new CreateCategoryRequest(
                "Mouses",
                "Gaming mouses"
        );

        Category category = Category.create(
                "Mouses",
                "Gaming mouses"
        );

        CategoryResponse response = new CategoryResponse(
                1L,
                "Mouses",
                "Gaming mouses"
        );

        when(createCategoryUseCase.execute(
                anyString(),
                anyString()
        )).thenReturn(category);

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        mockMvc.perform(post("/categories")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mouses"))
                .andExpect(jsonPath("$.description").value("Gaming mouses"));

        verify(createCategoryUseCase).execute(
                "Mouses",
                "Gaming mouses"
        );

        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldReturnConflictWhenCreatingDuplicatedCategory() throws Exception {

        CreateCategoryRequest request = new CreateCategoryRequest(
                "Mouses",
                "Gaming mouses"
        );

        when(createCategoryUseCase.execute(
                anyString(),
                anyString()
        )).thenThrow(new CategoryAlreadyExistsException("Mouses"));

        mockMvc.perform(post("/categories")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isConflict());

        verifyNoInteractions(categoryMapper);
    }

    @Test
    void shouldUpdateCategorySuccessfully() throws Exception {

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Keyboards",
                "Mechanical Keyboards"
        );

        Category category = Category.create(
                "Keyboards",
                "Mechanical Keyboards"
        );

        CategoryResponse response = new CategoryResponse(
                1L,
                "Keyboards",
                "Mechanical Keyboards"
        );

        when(updateCategoryUseCase.execute(
                anyLong(),
                anyString(),
                anyString()
        )).thenReturn(category);

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        mockMvc.perform(put("/categories/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Keyboards"))
                .andExpect(jsonPath("$.description").value("Mechanical Keyboards"));

        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingCategory() throws Exception {

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Keyboards",
                "Mechanical Keyboards"
        );

        when(updateCategoryUseCase.execute(
                anyLong(),
                anyString(),
                anyString()
        )).thenThrow(new CategoryNotFoundException(1L));

        mockMvc.perform(put("/categories/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isNotFound());

        verifyNoInteractions(categoryMapper);
    }

    @Test
    void shouldDeleteCategorySuccessfully() throws Exception {

        mockMvc.perform(delete("/categories/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))

                .andExpect(status().isNoContent());

        verify(deleteCategoryUseCase).execute(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCategory() throws Exception {

        doThrow(new CategoryNotFoundException(1L))
                .when(deleteCategoryUseCase)
                .execute(1L);

        mockMvc.perform(delete("/categories/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))

                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetCategoryByIdSuccessfully() throws Exception {

        Category category = Category.create(
                "Mouses",
                "Gaming mouses"
        );

        CategoryResponse response = new CategoryResponse(
                1L,
                "Mouses",
                "Gaming mouses"
        );

        when(getCategoryByIdUseCase.execute(1L))
                .thenReturn(category);

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        mockMvc.perform(get("/categories/1"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mouses"))
                .andExpect(jsonPath("$.description").value("Gaming mouses"));

        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {

        when(getCategoryByIdUseCase.execute(1L))
                .thenThrow(new CategoryNotFoundException(1L));

        mockMvc.perform(get("/categories/1"))

                .andExpect(status().isNotFound());

        verifyNoInteractions(categoryMapper);
    }

    @Test
    void shouldReturnAllCategories() throws Exception {

        Category category = Category.create(
                "Mouses",
                "Gaming mouses"
        );

        CategoryResponse response = new CategoryResponse(
                1L,
                "Mouses",
                "Gaming mouses"
        );

        when(getAllCategoriesUseCase.execute())
                .thenReturn(List.of(category));

        when(categoryMapper.toResponseList(List.of(category)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/categories"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Mouses"))
                .andExpect(jsonPath("$[0].description").value("Gaming mouses"));

        verify(categoryMapper).toResponseList(List.of(category));
    }
}