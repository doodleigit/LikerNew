package com.liker.android.Home.service;

import android.util.Log;

//import com.doodle.App;
//import com.doodle.Home.model.Headers;
//import com.doodle.Home.model.SetUser;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.PrefManager;
import com.google.gson.Gson;
import com.liker.android.App;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.model.SetUser;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.PrefManager;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;


public class SocketIOManager {
    private static final String TAG = SocketIOManager.class.getSimpleName();

    private static SocketIOManager mInstance;
    //Added @null
    public static Socket mSocket = null, wSocket = null, nSocket=null;
    private PrefManager manager;
    private String token, deviceId, userId, socketId, mSocketId;

    private void getMessageSocketClient() {
        try {
            String socketUrl = AppConstants.SOCKET_MESSAGE;
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            mSocket = IO.socket(socketUrl, opts);
            initializeMessageSocket();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void getWebSocketClient() {
        try {
            String socketUrl = AppConstants.SOCKET_WEB;
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            wSocket = IO.socket(socketUrl, opts);
            initializeWebSocket();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void getNewPostSocketClient() {
        try {
            String socketUrl = AppConstants.SOCKET_NEW_POST;
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            nSocket = IO.socket(socketUrl, opts);
            initializeNewPostSocket();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public Socket getMSocketInstance() {
        manager = new PrefManager(App.getInstance());
        // To ensure the SAME instance of socket can be called from any activity/ fragment in the app
        //IF this is not done as is, the methods will not be attached properly to the socket
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();
        if (mSocket == null) {
            getMessageSocketClient();
        }
        return mSocket;
    }

    public Socket getWSocketInstance() {
        manager = new PrefManager(App.getInstance());
        // To ensure the SAME instance of socket can be called from any activity/ fragment in the app
        //IF this is not done as is, the methods will not be attached properly to the socket

        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();
        if (wSocket == null) {
            getWebSocketClient();
        }
        return wSocket;
    }

    public Socket getNewPostSocketInstance() {
        manager = new PrefManager(App.getInstance());
        // To ensure the SAME instance of socket can be called from any activity/ fragment in the app
        //IF this is not done as is, the methods will not be attached properly to the socket

        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();
        if (nSocket == null) {
            getNewPostSocketClient();
        }
        return nSocket;
    }

    private void initializeWebSocket() {
        /**
         * In case you decide to add socket methods in different class, this can be ignored
         */
        wSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT connect");

                String id = wSocket.connect().id();
                App.setSocketId(id);
                setWUser();
                Log.d(TAG, "call: " + id);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, "Event disconnect, Socket is disconnected");
            }
        }).on("test", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        });

        wSocket.connect();
    }

    private void initializeNewPostSocket() {
        /**
         * In case you decide to add socket methods in different class, this can be ignored
         */
        nSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT connect");

                String id = nSocket.connect().id();
                App.setNewPostSocketId(id);
                setNPostUser();
                Log.d(TAG, "call: " + id);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e(TAG, "Event disconnect, Socket is disconnected");
            }
        }).on("test", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        });

        nSocket.connect();
    }


    private void initializeMessageSocket() {
        /**
         * In case you decide to add socket methods in different class, this can be ignored
         */
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT connect");

                String id = mSocket.connect().id();
                App.setmSocketId(id);
                setMUser();
                Log.d(TAG, "call: " + id);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("TAG", "Event disconnect, Socket is disconnected");
            }
        }).on("test", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        });

        mSocket.connect();
    }

    private void setWUser() {
        Headers headers = new Headers();
        SetUser setUser = new SetUser();
        socketId = App.getSocketId();
        if (socketId != null) {
            headers.setDeviceId(deviceId);
            headers.setIsApps(true);
            headers.setSecurityToken(token);
            setUser.setSocketId(App.getSocketId());
            setUser.setUserId(userId);
            setUser.setHeaders(headers);
            setUser.setApps(true);
            Gson gson = new Gson();
            String json = gson.toJson(setUser);
            wSocket.emit("set_user", json, new Ack() {
                @Override
                public void call(Object... args) {
                    if (args != null) {
                        Log.e(TAG, "Event error: " + args[0].toString());
                    }
                }
            });
            Log.d("SERIALIZATION DATA", json);
        }
    }
    private void setNPostUser() {
        Headers headers = new Headers();
        SetUser setUser = new SetUser();
        socketId = App.getNewPostSocketId();
        if (socketId != null) {
            headers.setDeviceId(deviceId);
            headers.setIsApps(true);
            headers.setSecurityToken(token);
            setUser.setSocketId(App.getNewPostSocketId());
            setUser.setUserId(userId);
            setUser.setHeaders(headers);
            setUser.setApps(true);
            Gson gson = new Gson();
            String json = gson.toJson(setUser);
            nSocket.emit("set_user", json, new Ack() {
                @Override
                public void call(Object... args) {
                    if (args != null) {
                        Log.e(TAG, "Event error: " + args[0].toString());
                    }
                }
            });
            Log.d("SERIALIZATION DATA", json);
        }
    }

    private void setMUser() {
        Headers headers = new Headers();
        SetUser setUser = new SetUser();
        mSocketId = App.getmSocketId();
        if (mSocketId != null) {
            headers.setDeviceId(deviceId);
            headers.setIsApps(true);
            headers.setSecurityToken(token);
            setUser.setSocketId(App.getmSocketId());
            setUser.setUserId(userId);
            setUser.setHeaders(headers);
            setUser.setApps(true);
            Gson gson = new Gson();
            String json = gson.toJson(setUser);
            mSocket.emit("set_user", json, new Ack() {
                @Override
                public void call(Object... args) {
                    if (args != null) {
                        Log.e(TAG, "Event error: " + args[0].toString());
                    }
                }
            });
            Log.d("SERIALIZATION DATA", json);
        }
    }

    public void stop() {
        Log.d(TAG, "stop socket...");

        if (mSocket != null) {
            mSocket.disconnect();
        }
    }
}