package com.yihao.pinterestdemo.httpservices;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.yihao.pinterestdemo.MainActivity;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.io.StringReader;
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
        private TypeAdapter<T> adapter;

        public PinResponseConverter(Type type) {
            if(null == type) {
                throw new IllegalArgumentException("type is null");
            }
            this.type = type;
            adapter = (TypeAdapter<T>) new Gson().getAdapter(TypeToken.get(type));
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String result = value.string();
            if(!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.has("data")){
                        Log.d(MainActivity.TAG, jsonObject.getString("data"));
                        return new Gson().fromJson(jsonObject.getString("data"), TypeToken.get(type).getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    value.close();
                }
            }
            return null;
        }
    }
}
