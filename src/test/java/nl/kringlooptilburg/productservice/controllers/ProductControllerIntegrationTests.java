package nl.kringlooptilburg.productservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kringlooptilburg.productservice.TestDataUtil;
import nl.kringlooptilburg.productservice.config.RabbitMQConfig;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.services.ProductService;
import nl.kringlooptilburg.productservice.services.rabbit.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTests {

    private final ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final RabbitMQConfig rabbitMQConfig;

    @MockBean
    private RabbitMQSender rabbitMQSender;

    @Autowired
    public ProductControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, ProductService productService, RabbitMQConfig rabbitMQConfig) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productService = productService;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @BeforeEach
    public void setup() {
        Mockito.doNothing().when(rabbitMQSender).sendImages(anyList());
    }

    @Test
    public void testThatCreatedProductSuccessfullyReturnsHttp201Created() throws Exception {
        String productJson = TestDataUtil.createExampleProductJson();
        MockMultipartFile productJsonPart = new MockMultipartFile("productJson", "", "application/json", productJson.getBytes());
        MockMultipartFile imagesPart = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/product-service/products")
                .file(productJsonPart)
                .file(imagesPart)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatedProductSuccessfullyReturnsSavedProduct() throws Exception {
        String productJson = TestDataUtil.createExampleProductJson();
        MockMultipartFile productJsonPart = new MockMultipartFile("productJson", "", "application/json", productJson.getBytes());
        MockMultipartFile imagesPart = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/product-service/products")
                .file(productJsonPart)
                .file(imagesPart)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productId").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.audience").value("MALE")
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
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
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
                MockMvcResultMatchers.jsonPath("$.description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.audience").value("MALE")
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
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
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
                MockMvcResultMatchers.jsonPath("$[1].brand").value("ZARA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].category").value("Outerwear")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].size").value("M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].material").value("COTTON")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].productCondition").value("NEW")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].audience").value("FEMALE")
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
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ZARA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Outerwear")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("M")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("COTTON")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("NEW")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("FEMALE")
        );
    }

    @Test
    public void testThatListProductsByBrandSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/brand/ADIDAS")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/brand/ADIDAS")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
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
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
        );
    }

    @Test
    public void testThatListProductsByMaterialSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/material/ACRYLIC")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/material/ACRYLIC")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
        );
    }

    @Test
    public void testThatListProductsByProductConditionSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/product-condition/GOOD")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/product-condition/GOOD")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
        );
    }

    @Test
    public void testThatListProductsByAudienceSuccessfullyReturnsHttp200Ok() throws Exception {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productService.createProduct(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productService.createProduct(productEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/audience/MALE")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/product-service/products/audience/MALE")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Grey Ripped Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("Good condition, size L, grey ripped Jeans.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(30.0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].brand").value("ADIDAS")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].category").value("Jeans")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].size").value("L")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].material").value("ACRYLIC")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].productCondition").value("GOOD")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].audience").value("MALE")
        );
    }
}