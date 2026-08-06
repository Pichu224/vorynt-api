package com.vorynt.vorynt_api.controllers.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.ProductAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.ProductNotFoundException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.dtos.product.CreateProductRequest;
import com.vorynt.vorynt_api.dtos.product.ProductResponse;
import com.vorynt.vorynt_api.dtos.product.UpdateProductRequest;
import com.vorynt.vorynt_api.handlers.GlobalExceptionHandler;
import com.vorynt.vorynt_api.mappers.ProductMapper;
import com.vorynt.vorynt_api.security.JwtAuthenticationEntryPoint;
import com.vorynt.vorynt_api.security.JwtAuthenticationFilter;
import com.vorynt.vorynt_api.services.product.CreateProductUseCase;
import com.vorynt.vorynt_api.services.product.DeleteProductUseCase;
import com.vorynt.vorynt_api.services.product.GetAllProductsUseCase;
import com.vorynt.vorynt_api.services.product.GetProductByIdUseCase;
import com.vorynt.vorynt_api.services.product.UpdateProductUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockBean
    private UpdateProductUseCase updateProductUseCase;

    @MockBean
    private GetProductByIdUseCase getProductByIdUseCase;

    @MockBean
    private GetAllProductsUseCase getAllProductsUseCase;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    void shouldCreateProductSuccessfully() throws Exception {

        Category category = Category.create(
                "Components",
                "Gaming components",
                List.of()
        );

        CreateProductRequest request = new CreateProductRequest(
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        Product product = Product.create(
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        ProductResponse response = new ProductResponse(
                1L,
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        when(createProductUseCase.execute(
                anyString(),
                anyString(),
                any(BigDecimal.class),
                any(Category.class)
        )).thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(response);

        mockMvc.perform(post("/products")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mouse"))
                .andExpect(jsonPath("$.description").value("Gaming mouse"))
                .andExpect(jsonPath("$.price").value(50000))
                .andExpect(jsonPath("$.category.name").value(category.getName()));

        verify(createProductUseCase).execute(
                eq("Mouse"),
                eq("Gaming mouse"),
                eq(BigDecimal.valueOf(50000)),
                any(Category.class)
        );

        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldReturnConflictWhenCreatingDuplicatedProduct() throws Exception {

        Category category = Category.create(
                "Components",
                "Gaming components",
                List.of()
        );

        CreateProductRequest request = new CreateProductRequest(
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        when(createProductUseCase.execute(
                anyString(),
                anyString(),
                any(BigDecimal.class),
                any(Category.class)
        )).thenThrow(new ProductAlreadyExistsException("Mouse"));

        mockMvc.perform(post("/products")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isConflict());

        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {

        Category category = Category.create(
                "Components",
                "Gaming components",
                List.of()
        );

        UpdateProductRequest request = new UpdateProductRequest(
                "Keyboard",
                "Mechanical",
                BigDecimal.valueOf(100000),
                category
        );

        Product product = Product.create(
                "Keyboard",
                "Mechanical",
                BigDecimal.valueOf(10),
                category
        );

        ProductResponse response = new ProductResponse(
                1L,
                "Keyboard",
                "Mechanical",
                BigDecimal.valueOf(100000),
                category
        );

        when(updateProductUseCase.execute(
                anyLong(),
                anyString(),
                anyString(),
                any(BigDecimal.class),
                any(Category.class)
        )).thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(response);

        mockMvc.perform(put("/products/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.description").value("Mechanical"))
                .andExpect(jsonPath("$.price").value(100000))
                .andExpect(jsonPath("$.category.name").value(category.getName()));

        verify(updateProductUseCase).execute(
                eq(Long.valueOf(1)),
                eq("Keyboard"),
                eq("Mechanical"),
                eq(BigDecimal.valueOf(100000)),
                any(Category.class)
        );

        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingProduct() throws Exception {

        Category category = Category.create(
                "Components",
                "Gaming components",
                List.of()
        );

        UpdateProductRequest request = new UpdateProductRequest(
                "Keyboard",
                "Mechanical",
                BigDecimal.valueOf(100000),
                category
        );

        when(updateProductUseCase.execute(
                anyLong(),
                anyString(),
                anyString(),
                any(BigDecimal.class),
                any(Category.class)
        )).thenThrow(new ProductNotFoundException(1L));

        mockMvc.perform(put("/products/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isNotFound());

        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {

        mockMvc.perform(delete("/products/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))

                .andExpect(status().isNoContent());

        verify(deleteProductUseCase).execute(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingProduct() throws Exception {

        doThrow(new ProductNotFoundException(1L))
                .when(deleteProductUseCase)
                .execute(1L);

        mockMvc.perform(delete("/products/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))

                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetProductByIdSuccessfully() throws Exception {

        Category category = Category.create(
                "Components",
                "Gaming components",
                List.of()
        );

        Product product = Product.create(
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        ProductResponse response = new ProductResponse(
                1L,
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        when(getProductByIdUseCase.execute(1L))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(response);

        mockMvc.perform(get("/products/1"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mouse"))
                .andExpect(jsonPath("$.description").value("Gaming mouse"))
                .andExpect(jsonPath("$.price").value(50000))
                .andExpect(jsonPath("$.category.name").value(category.getName()));

        verify(getProductByIdUseCase).execute(
                eq(Long.valueOf(1))
        );

        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {

        when(getProductByIdUseCase.execute(1L))
                .thenThrow(new ProductNotFoundException(1L));

        mockMvc.perform(get("/products/1"))

                .andExpect(status().isNotFound());

        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {

        Category category = Category.create(
                "Components",
                "Gaming components",
                List.of()
        );

        Product product = Product.create(
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        ProductResponse response = new ProductResponse(
                1L,
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(50000),
                category
        );

        when(getAllProductsUseCase.execute())
                .thenReturn(List.of(product));

        when(productMapper.toResponseList(List.of(product)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/products"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Mouse"))
                .andExpect(jsonPath("$[0].price").value(50000))
                .andExpect(jsonPath("$[0].category.name").value(category.getName()));

        verify(getAllProductsUseCase).execute();

        verify(productMapper).toResponseList(List.of(product));
    }
}