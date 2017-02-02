package com.lecrec.lecrec.utils.helper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class PrimitiveConverterFactory extends Converter.Factory {

    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    private static PrimitiveConverterFactory sInstance;

    public static Converter.Factory create() {
        if (sInstance == null) {
            sInstance = new PrimitiveConverterFactory();
        }
        return sInstance;
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class && ((Class<?>) type).isEnum()) {
            return new Converter<Enum, RequestBody>() {
                @Override
                public RequestBody convert(Enum value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.toString());
                }
            };
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return new Converter<Integer, RequestBody>() {
                @Override
                public RequestBody convert(Integer value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Integer.toString(value));
                }
            };
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return new Converter<Boolean, RequestBody>() {
                @Override
                public RequestBody convert(Boolean value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Boolean.toString(value));
                }
            };
        } else if (Byte.class.equals(type) || byte.class.equals(type)) {
            return new Converter<Byte, RequestBody>() {
                @Override
                public RequestBody convert(Byte value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Byte.toString(value));
                }
            };
        } else if (Character.class.equals(type) || char.class.equals(type)) {
            return new Converter<Character, RequestBody>() {
                @Override
                public RequestBody convert(Character value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Character.toString(value));
                }
            };
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return new Converter<Long, RequestBody>() {
                @Override
                public RequestBody convert(Long value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Long.toString(value));
                }
            };
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return new Converter<Float, RequestBody>() {
                @Override
                public RequestBody convert(Float value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Float.toString(value));
                }
            };
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return new Converter<Double, RequestBody>() {
                @Override
                public RequestBody convert(Double value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Double.toString(value));
                }
            };
        } else if (Short.class.equals(type) || short.class.equals(type)) {
            return new Converter<Short, RequestBody>() {
                @Override
                public RequestBody convert(Short value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, Short.toString(value));
                }
            };
        } else if (String.class.equals(type)) {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        } else if (Void.class.equals(type) || void.class.equals(type)) {
            return new Converter<Void, RequestBody>() {
                @Override
                public RequestBody convert(Void value) throws IOException {
                    return null;
                }
            };
        }
        return null;
    }

}