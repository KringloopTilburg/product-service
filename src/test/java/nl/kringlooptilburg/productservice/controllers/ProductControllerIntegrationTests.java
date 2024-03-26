package nl.kringlooptilburg.productservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kringlooptilburg.productservice.TestDataUtil;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTests {

    private ProductService productService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public ProductControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, ProductService productService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @Test
    public void testThatCreatedProductSuccessfullyReturnsHttp201Created() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productEntityA.setProductId(null);
        String productJson = objectMapper.writeValueAsString(productEntityA);

        mockMvc.perform(MockMvcRequestBuilders.post("/product-service/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatedProductSuccessfullyReturnsSavedProduct() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productEntityA.setProductId(null);
        String productJson = objectMapper.writeValueAsString(productEntityA);

        mockMvc.perform(MockMvcRequestBuilders.post("/product-service/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productId").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("Good condition, size L, grey ripped jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("H&M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.color").value("Grey")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.audience").value("Male")
        );
    }

    @Test
    public void testThatListProductsSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("H&M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].color").value("Grey")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }

    @Test
    public void testThatGetProductSuccessfullyReturnsHttp200OkWhenProductExists() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetProductSuccessfullyReturnsHttp404NotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/99")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetProductSuccessfullyReturnsProduct() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productId").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("Good condition, size L, grey ripped jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("H&M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.color").value("Grey")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.audience").value("Male")
        );
    }

    @Test
    public void testThatDeleteProductSuccessfullyReturnsHttp204NoContent() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);

        mockMvc.perform(MockMvcRequestBuilders.delete("/product-service/products/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
