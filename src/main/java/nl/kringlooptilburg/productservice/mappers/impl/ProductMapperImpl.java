package nl.kringlooptilburg.productservice.mappers.impl;

import nl.kringlooptilburg.productservice.domain.dto.ProductDto;
import nl.kringlooptilburg.productservice.domain.entities.ColorEntity;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.Color;
import nl.kringlooptilburg.productservice.mappers.Mapper;
import nl.kringlooptilburg.productservice.services.ColorService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements Mapper<ProductEntity, ProductDto> {

    private ModelMapper modelMapper;
    private ColorService colorService;

    public ProductMapperImpl(ModelMapper modelMapper, ColorService colorService) {
        this.modelMapper = modelMapper;
        this.colorService = colorService;

        Converter<Set<Color>, Set<ColorEntity>> colorToEntityConverter = context -> context.getSource().stream()
                .map(colorService::findOneByColor)
                .collect(Collectors.toSet());

        Converter<Set<ColorEntity>, Set<Color>> entityToColorConverter = context -> context.getSource().stream()
                .map(ColorEntity::getColor)
                .collect(Collectors.toSet());

        modelMapper.typeMap(ProductDto.class, ProductEntity.class).addMappings(mapper -> mapper.using(colorToEntityConverter).map(ProductDto::getColors, ProductEntity::setColors));
        modelMapper.typeMap(ProductEntity.class, ProductDto.class).addMappings(mapper -> mapper.using(entityToColorConverter).map(ProductEntity::getColors, ProductDto::setColors));
    }

    @Override
    public ProductDto mapTo(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDto.class);
    }

    @Override
    public ProductEntity mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, ProductEntity.class);
    }
}
