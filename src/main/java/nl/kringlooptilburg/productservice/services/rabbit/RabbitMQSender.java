package nl.kringlooptilburg.productservice.services.rabbit;

import nl.kringlooptilburg.productservice.domain.dto.ProductImageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQSender {

    @Value("${rabbitmq.exchange.product-image}")
    private String productImageExchangeName;

    @Value("${rabbitmq.routing-key.product-image}")
    private String productImageRoutingKeyName;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendImages(List<ProductImageDto> productImagesDto) {
        rabbitTemplate.convertAndSend(productImageExchangeName, productImageRoutingKeyName, productImagesDto);
    }
}
