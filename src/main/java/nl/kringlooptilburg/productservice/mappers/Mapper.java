package nl.kringlooptilburg.productservice.mappers;

public interface Mapper<A,B> {
    B mapTo(A a);

    A mapFrom(B b);

}
