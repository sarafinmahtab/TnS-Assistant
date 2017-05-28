package com.sarafinmahtab.tnsassistant;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Arafin on 5/27/2017.
 */

public class MySingleton {

    private static MySingleton myInstance;
    private static Context myContext;

    private RequestQueue requestQueue;

    private MySingleton(Context myCtx) {
        myContext = myCtx;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getMyInstance(Context context) {
        if(myInstance == null) {
            myInstance = new MySingleton(context);
        }
        return myInstance;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}
