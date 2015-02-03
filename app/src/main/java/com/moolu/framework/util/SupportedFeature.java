package com.moolu.framework.util;

import android.content.Context;
import android.util.Xml;

import com.moolu.R;
import com.moolu.framework.NananLog;
import com.moolu.util.IOUtils;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nanan on 2/3/2015.
 */
public class SupportedFeature {
    private final static Logger Log = new NananLog(SupportedFeature.class);
    private static HashMap<String,AppSupportFeature> features;

    private SupportedFeature(){};
    public static synchronized HashMap<String,AppSupportFeature> getInstance(Context context){
        if(features == null){
            InputStream inputStream = context.getResources().openRawResource(R.raw.app_supported_feature_config);
            XML2Feature xml2Feature = new XML2Feature();
            try{
                Xml.parse(inputStream,Xml.Encoding.UTF_8,xml2Feature);
                List<AppSupportFeature> featureList = xml2Feature.getFeatures();
                if(featureList != null && featureList.size()>0 ){
                    features = new HashMap<String,AppSupportFeature>();
                    for(AppSupportFeature appSupportFeature:featureList){
                        features.put(appSupportFeature.getName(),appSupportFeature);
                    }
                }
            } catch (IOException e){
                Log.error("Parse feature config error",e);
            } catch (SAXException e){
                Log.error("Parse feature config error",e);
            } finally {
                IOUtils.close(inputStream);
            }

        }
        return features;
    }

    public static boolean checkIsSupportedFeature(String featureName,Context context){
        HashMap<String,AppSupportFeature> featureEntity = SupportedFeature.getInstance(context);
        boolean isSupported = false;
        if(featureEntity != null && featureEntity.size()>0 ){
            AppSupportFeature appSupportFeature = featureEntity.get(featureName);
            isSupported = appSupportFeature.isSupported();
        }
        return isSupported;
    }

}
