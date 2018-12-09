package com.yihao.pinterestdemo.httpservices;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by 易昊 on 2018/12/7.
 */
public class PinConverterFactory extends Converter.Factory {
    public static PinConverterFactory create() {
        return new PinConverterFactory();
    }

    ScalarsConverterFactory gsonConverterFactory;
    private PinConverterFactory() {
        gsonConverterFactory = ScalarsConverterFactory.create();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new PinResponseConverter<>(TypeToken.get(type).getType());
    }

    private static class PinResponseConverter<T> implements Converter<ResponseBody, T> {

        private Type type;

        public PinResponseConverter(Type type) {
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String result = value.string();
            if(!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.has("data")){
                        T obj = new Gson().fromJson(jsonObject.getString("data"), type);
                        return obj;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
