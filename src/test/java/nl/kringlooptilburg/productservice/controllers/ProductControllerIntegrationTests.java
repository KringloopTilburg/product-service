package nl.kringlooptilburg.productservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kringlooptilburg.productservice.TestDataUtil;
import nl.kringlooptilburg.productservice.config.RabbitMQConfig;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.services.ProductService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
class ProductControllerIntegrationTests {

    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    public ProductControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, ProductService productService, RabbitMQConfig rabbitMQConfig) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productService = productService;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @Test
    @Disabled
    public void testThatCreatedProductSuccessfullyReturnsHttp201Created() throws Exception {
        String productJson = TestDataUtil.createExampleProductJson();

        mockMvc.perform(MockMvcRequestBuilders.post("/product-service/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @Disabled
    public void testThatCreatedProductSuccessfullyReturnsSavedProduct() throws Exception {
        String productJson = TestDataUtil.createExampleProductJson();

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
                MockMvcResultMatchers.jsonPath("$.brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCondition").value("Good")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
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
                MockMvcResultMatchers.jsonPath("$.brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCondition").value("Good")
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

    @Test
    public void testThatListProductsByCategorySuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/category/Jeans")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByCategorySuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/category/Jeans")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }

    @Test
    public void testThatListProductsByPriceBetweenSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/price/10.0/40.0")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByPriceBetweenSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/price/10.0/40.0")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].name").value("Colourful Jacket")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].description").value("New, size M, colourful jacket.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].price").value(20.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].brand").value("Zara")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].category").value("Outerwear")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].size").value("M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].material").value("Cotton")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].productCondition").value("New")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].audience").value("Female")
        );
    }

    @Test
    public void testThatListProductsByPriceLessThanSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/price/25.0")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByPriceLessThanSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/price/25.0")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Colourful Jacket")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("New, size M, colourful jacket.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(20.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("Zara")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Outerwear")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Cotton")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("New")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Female")
        );
    }

    @Test
    public void testThatListProductsByBrandSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/brand/adidas")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByBrandSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/brand/adidas")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }

    @Test
    public void testThatListProductsBySizeSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/size/L")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsBySizeSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/size/L")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }

    @Test
    public void testThatListProductsByMaterialSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/material/Acrylic")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByMaterialSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/material/Acrylic")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }

    @Test
    public void testThatListProductsByProductConditionSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/product-condition/Good")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByProductConditionSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/product-condition/Good")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }

    @Test
    public void testThatListProductsByAudienceSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/audience/Male")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsByAudienceSuccessfullyReturnsListOfProducts() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/audience/Male")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("adidas")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("Acrylic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("Good")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("Male")
        );
    }
}