package util;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<L,R>
{
    public static <L,R> Either<L,R> left(L value) {
        return new Either<L,R>() {
            @Override public <T> T map(Function<? super L, ? extends T> lFunc,
                                       Function<? super R, ? extends T> rFunc) {
                return lFunc.apply(value);
            }
        };
    }
    public static <L,R> Either<L,R> right(R value) {
        return new Either<L,R>() {
            @Override public <T> T map(Function<? super L, ? extends T> lFunc,
                                       Function<? super R, ? extends T> rFunc) {
                return rFunc.apply(value);
            }

        };
    }
    private Either() {}
    public abstract <T> T map(
            Function<? super L, ? extends T> lFunc, Function<? super R, ? extends T> rFunc);

    public <T> Either<T,R> mapLeft(Function<? super L, ? extends T> lFunc) {
        return this.<Either<T,R>>map(t -> left(lFunc.apply(t)), t -> (Either<T,R>)this);
    }
    public <T> Either<L,T> mapRight(Function<? super R, ? extends T> lFunc) {
        return this.<Either<L,T>>map(t -> (Either<L,T>)this, t -> right(lFunc.apply(t)));
    }
    public void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc) {
        map(consume(lFunc), consume(rFunc));
    }
    private <T> Function<T,Void> consume(Consumer<T> c) {
        return t -> { c.accept(t); return null; };
    }
}
