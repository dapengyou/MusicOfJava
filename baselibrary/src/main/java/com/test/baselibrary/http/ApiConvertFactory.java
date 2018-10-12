package com.test.baselibrary.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ApiConvertFactory extends Converter.Factory {
    // 日志标识
    private final static String TAG = "http@ApiConvertFactory";

    public static ApiConvertFactory create() {
        return create(new Gson());
    }

    public static ApiConvertFactory create(Gson gson) {
        return new ApiConvertFactory(gson);
    }

    private ApiConvertFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson is null");
        }
    }

    /**
     * 重写responseBodyConverter
     *
     * @param type
     * @param annotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonResponseBodyConverter<>();
    }

    final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        @Override
        public T convert(ResponseBody value) throws IOException {
            String reString;
            try {
                reString = value.string();
                Log.i(TAG, "#body=" + reString);
                return (T) reString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
