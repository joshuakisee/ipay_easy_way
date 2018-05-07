package com.libs.ipay.ipayLibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



public class Channels extends Fragment {

    private LinearLayout layout_channel,layout_payments, layout_payment_mbonga, layout_payment_airtel,
            bonga_copy_paybill, bonga_copy_account, bonga_copy_amount, bonga_dial, airtel_copy_buss_name,
            airtel_copy_amount, airtel_copy_refference, equity_menu, layout_payment_eazzy,
            eazzy_copy_buss_number, eazzy_copy_account, eazzy_copy_amount;
    private Button pay_now, confirm_payment, confirm_payment_airtel, confirm_payment_eazzy;
    private ImageButton mpesa, airtel, lipa_mbonga, ezzy_pay, visa;
    private ImageView image_title, back, payment_back, airtel_back, ezzy_menu_back, ezzy_menu_home;
    private EditText phone, email, amount;
    private TextView title, bonga_account, bonga_amount, bonga_amount_confirm, airtel_amount, airtel_refference,
            eazzy_account, eazzy_amount;
    private String ScreenState;
    private ProgressDialog mAuthProgressDialog;

    //parameters to pass to ipay
    private static String ilive, ioid, iinv, iamount, itel, ieml, ivid, icurr,
            p1, p2, p3, p4, icbk, icst, icrl, ikey;

    String live, vid, cbk, key;

    private String sid, response_account, response_amount, generatedHex, hashed_value, toHash, data_string;

    public Channels() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channels, container, false);

        //get url value passed from main class
        Bundle bundle=getArguments();
        live    = bundle.getString("live", "0");
        vid     = bundle.getString("vid", "demo");
        cbk     = bundle.getString("cbk", "");
        key     = bundle.getString("key", "demo");

        ikey = key;


        layout_channel          = (LinearLayout) view.findViewById(R.id.layout_channel);
        layout_payments         = (LinearLayout) view.findViewById(R.id.layout_payments);
        layout_payment_mbonga   = (LinearLayout) view.findViewById(R.id.layout_payment_mbonga);
        layout_payment_airtel   = (LinearLayout) view.findViewById(R.id.layout_payment_airtel);
        bonga_copy_paybill      = (LinearLayout) view.findViewById(R.id.bonga_copy_paybill);
        bonga_copy_account      = (LinearLayout) view.findViewById(R.id.bonga_copy_account);
        bonga_copy_amount       = (LinearLayout) view.findViewById(R.id.bonga_copy_amount);
        bonga_dial              = (LinearLayout) view.findViewById(R.id.bonga_dial);
        airtel_copy_buss_name   = (LinearLayout) view.findViewById(R.id.airtel_copy_buss_name);
        airtel_copy_amount      = (LinearLayout) view.findViewById(R.id.airtel_copy_amount);
        airtel_copy_refference  = (LinearLayout) view.findViewById(R.id.airtel_copy_refference);
        equity_menu             = (LinearLayout) view.findViewById(R.id.equity_menu);
        layout_payment_eazzy    = (LinearLayout) view.findViewById(R.id.layout_payment_eazzy);
        eazzy_copy_buss_number  = (LinearLayout) view.findViewById(R.id.eazzy_copy_buss_number);
        eazzy_copy_account      = (LinearLayout) view.findViewById(R.id.eazzy_copy_account);
        eazzy_copy_amount       = (LinearLayout) view.findViewById(R.id.eazzy_copy_amount);



        phone = (EditText) view.findViewById(R.id.phone);
        email = (EditText) view.findViewById(R.id.email);
        amount = (EditText) view.findViewById(R.id.amount);


        title                   = (TextView) view.findViewById(R.id.title);
        bonga_account           = (TextView) view.findViewById(R.id.bonga_account);
        bonga_amount            = (TextView) view.findViewById(R.id.bonga_amount);
        bonga_amount_confirm    = (TextView) view.findViewById(R.id.bonga_confirm_amount);
        airtel_amount           = (TextView) view.findViewById(R.id.airtel_amount);
        airtel_refference       = (TextView) view.findViewById(R.id.airtel_refference);
        eazzy_account           = (TextView) view.findViewById(R.id.eazzy_account);
        eazzy_amount            = (TextView) view.findViewById(R.id.eazzy_amount);


        pay_now                 = (Button) view.findViewById(R.id.pay_now);
        confirm_payment         = (Button) view.findViewById(R.id.confirm_payment);
        confirm_payment_airtel  = (Button) view.findViewById(R.id.confirm_payment_airtel);
        confirm_payment_eazzy   = (Button) view.findViewById(R.id.confirm_payment_eazzy);


        image_title         = (ImageView) view.findViewById(R.id.image_title);
        mpesa               = (ImageButton) view.findViewById(R.id.mpesa);
        airtel              = (ImageButton) view.findViewById(R.id.airtel);
        lipa_mbonga         = (ImageButton) view.findViewById(R.id.mbonga);
        ezzy_pay            = (ImageButton) view.findViewById(R.id.ezzy);
        //eazzy_pay_action    = (ImageButton) view.findViewById(R.id.ezzy_pay);
        //equitel            = (ImageButton) view.findViewById(R.id.equitel);
        visa                = (ImageButton) view.findViewById(R.id.visa);
        back                = (ImageView) view.findViewById(R.id.back);
        payment_back        = (ImageView) view.findViewById(R.id.payment_back);
        airtel_back         = (ImageView) view.findViewById(R.id.airtel_back);
        ezzy_menu_back      = (ImageView) view.findViewById(R.id.ezzy_menu_back);
        ezzy_menu_home      = (ImageView) view.findViewById(R.id.eazzy_back_home);

        payment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_channel.setVisibility(View.VISIBLE);
                layout_payment_mbonga.setVisibility(View.GONE);
                layout_payment_airtel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.GONE);

            }
        });

        //initiating methods
        mpesa();
        airtel();
        visa();
        mbonga();
        payNow();
        ezzyPay();



        return view;
    }



    //mpesa payment channel code starts
    private void mpesa()
    {
      mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "mpesa";
                image_title.setImageResource(R.drawable.mpesa100);
                image_title.setBackgroundColor(Color.parseColor("#388E3C"));
                layout_channel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.VISIBLE);
                equity_menu.setVisibility(View.GONE);
                orderId(ScreenState);
            }
        });

    }
    //mpesa payment channel code ends

    //visa code starts
    private void visa()
    {
        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "visa";
                image_title.setImageResource(R.drawable.visa_mastercard100);
                image_title.setBackgroundColor(Color.parseColor("#2196F3"));
                layout_channel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.VISIBLE);
                equity_menu.setVisibility(View.GONE);
                orderId(ScreenState);
            }
        });
    }
    //visa cod ends

    //lipa na mbonga points payment channel code starts
    private void mbonga()
    {
        lipa_mbonga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "bonga";
                image_title.setImageResource(R.drawable.bongapoints100);
                image_title.setBackgroundColor(Color.parseColor("#388E3C"));
                layout_channel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.VISIBLE);
                equity_menu.setVisibility(View.GONE);
                orderId(ScreenState);

            }
        });

        confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPayment();
            }
        });

        //dial/copy actions
        bonga_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial();
            }
        });

        bonga_copy_paybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paybill = "510800";
                String title   = "Paybill";
                copy(paybill, title);
            }
        });
        bonga_copy_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Account Number";
                copy(response_account, title);
            }
        });
        bonga_copy_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Amount KSH.";
                copy(response_amount, title);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_channel.setVisibility(View.VISIBLE);
                layout_payment_mbonga.setVisibility(View.GONE);
                layout_payment_airtel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.GONE);
            }
        });

    }
    //lipa na mbonga points payment channel code ends

    //lipa na airtel payment channel code starts
    private void airtel()
    {
        airtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "airtel";
                image_title.setImageResource(R.drawable.airtel_money100);
                image_title.setBackgroundColor(Color.parseColor("#F44336"));
                layout_channel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.VISIBLE);
                equity_menu.setVisibility(View.GONE);
                orderId(ScreenState);

            }
        });

        confirm_payment_airtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPayment();

            }
        });

        airtel_copy_buss_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Business name";
                String paybill = "510800";

                copy(paybill, title);
            }
        });
        airtel_copy_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Amount KSH.";

                copy(response_amount, title);
            }
        });
        airtel_copy_refference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Reference";

                copy(response_account, title);
            }
        });

        airtel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_channel.setVisibility(View.VISIBLE);
                layout_payment_mbonga.setVisibility(View.GONE);
                layout_payment_airtel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.GONE);
            }
        });


    }
    //lipa na airtel payment channel code ends

    //ezzy_pay code starts
    private void ezzyPay()
    {
        ezzy_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout_channel.setVisibility(View.GONE);
//                layout_payments.setVisibility(View.GONE);
//                equity_menu.setVisibility(View.VISIBLE);

                ScreenState = "eazzy";
                image_title.setImageResource(R.drawable.eazzypay100);
                image_title.setBackgroundColor(Color.parseColor("#7c3933"));
                layout_channel.setVisibility(View.GONE);
                layout_payments.setVisibility(View.VISIBLE);
                equity_menu.setVisibility(View.GONE);
                orderId(ScreenState);
            }
        });

        confirm_payment_eazzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPayment();

            }
        });

//        eazzy_pay_action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ScreenState = "eazzy";
////                image_title.setImageResource(R.drawable.eazzypay100);
////                image_title.setBackgroundColor(Color.parseColor("#7c3933"));
////                layout_channel.setVisibility(View.GONE);
////                layout_payments.setVisibility(View.VISIBLE);
////                equity_menu.setVisibility(View.GONE);
////                orderId(ScreenState);
//            }
//        });


        eazzy_copy_buss_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Business Number";
                String paybill = "510800";

                copy(paybill, title);
            }
        });
        eazzy_copy_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Amount KSH.";

                copy(response_amount, title);
            }
        });
        eazzy_copy_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Account Number";

                copy(response_account, title);
            }
        });

//        equitel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ScreenState = "equitel";
//                dialog();
//            }
//        });



        ezzy_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_channel.setVisibility(View.VISIBLE);
                layout_payments.setVisibility(View.GONE);
                equity_menu.setVisibility(View.GONE);
            }
        });

        ezzy_menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_channel.setVisibility(View.VISIBLE);
                layout_payments.setVisibility(View.GONE);
                layout_payment_eazzy.setVisibility(View.GONE);
                equity_menu.setVisibility(View.GONE);
            }
        });

    }
    //ezzy_pay code ends

    //generate order id
    private void orderId(String channel)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        String data_string = currentDateandTime+vid+channel;

        ioid = hashsha1(data_string).substring(0, 5);

     }

    //sha1 hash string to send to ipay
    private String hashsha1(String data_string)
    {
        //get hash
        String secret = ikey; String message = data_string;
        try{
            Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
            sha1_HMAC.init(new SecretKeySpec(secret.getBytes(), "HmacSHA1"));

            byte[] hash = (sha1_HMAC.doFinal(message.getBytes()));
            generatedHex = bytesToHexString(hash);
            hashed_value = generatedHex;

            //getSeed();


        }catch (Exception e)
        {

        }
        return hashed_value;
    }

    //SHA256 hash string to send to ipay
    private String hashing(String data_string)
    {
        //get hash
        String secret = ikey; String message = data_string;
        try{
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));

            byte[] hash = (sha256_HMAC.doFinal(message.getBytes()));
            generatedHex = bytesToHexString(hash);
            hashed_value = generatedHex;


        }catch (Exception e)
        {

        }
        return hashed_value;
    }



    //network request to get sid
    private void getSeed() {

        final String hashed_value = hashing(data_string);

        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setMessage("loading please wait...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://apis.ipayafrica.com/payments/v2/transact",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          //Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();

                        mAuthProgressDialog.hide();
                        JSONObject oprator = null;

                        //Toast.makeText(PaymentActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                        try {
                            oprator = new JSONObject(response);
                            JSONObject data = oprator.getJSONObject("data");
                            sid     = data.getString("sid");
                            response_account = data.getString("account");
                            response_amount  = data.getString("amount");

                            if (sid != "") {

                                if (ScreenState == "mpesa")
                                {
                                    //trigger stk push
                                    initiateStkPush();

                                } else if(ScreenState == "bonga"){

                                    amount.setText("");
                                    email.setText("");
                                    phone.setText("");

                                    layout_payment_mbonga.setVisibility(View.VISIBLE);
                                    layout_payment_airtel.setVisibility(View.GONE);
                                    layout_channel.setVisibility(View.GONE);
                                    layout_payments.setVisibility(View.GONE);
                                    layout_payment_eazzy.setVisibility(View.GONE);

                                    bonga_account.setText("4. Enter Account Number ("+response_account+").");
                                    bonga_amount.setText("5. Enter the EXACT Amount KSh. "+response_amount+".");
                                    bonga_amount_confirm.setText("6. Please confirm payment of KSh. "+response_amount+" worth to iPay Ltd.");

                                }
                                else if(ScreenState == "airtel"){

                                    amount.setText("");
                                    email.setText("");
                                    phone.setText("");

                                    layout_payment_mbonga.setVisibility(View.GONE);
                                    layout_payment_airtel.setVisibility(View.VISIBLE);
                                    layout_channel.setVisibility(View.GONE);
                                    layout_payments.setVisibility(View.GONE);
                                    layout_payment_eazzy.setVisibility(View.GONE);

                                    airtel_amount.setText("6. Enter the EXACT amount(KSh. "+ response_amount +" ).");
                                    airtel_refference.setText("8. Enter "+ response_account +" as the Reference and then send the money");
                                }
                                else if(ScreenState == "eazzy"){

                                    amount.setText("");
                                    email.setText("");
                                    phone.setText("");

                                    layout_payment_eazzy.setVisibility(View.VISIBLE);
                                    layout_payment_mbonga.setVisibility(View.GONE);
                                    layout_payment_airtel.setVisibility(View.GONE);
                                    layout_channel.setVisibility(View.GONE);
                                    layout_payments.setVisibility(View.GONE);

                                    eazzy_account.setText("4. Enter "+response_account+" as the Account Number.");
                                    eazzy_amount.setText("5. Enter the EXACT amount (KSh. "+response_amount+" ).");
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAuthProgressDialog.hide();

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    //L.m(new String(response.data));
                    //parsing the error
                    String json = "";
                    JSONObject obj;

                    switch (response.statusCode) {

                        case 401:
                            //Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                            break;

                        case 403:
                            json = new String(response.data);

                            try {
                                obj = new JSONObject(json);

                                 // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 400:

                            json = new String(response.data);//string
                            Toast.makeText(getActivity(), "Missing value! Hash ID mismatch  please use the correct values", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "system error occurred! please try again", Toast.LENGTH_LONG).show();
                            break;

                    }
                }else {
                    volleyErrors(error);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("live", ilive);
                parameters.put("oid", ioid);
                parameters.put("inv", iinv);
                parameters.put("amount", iamount);
                parameters.put("tel", itel);
                parameters.put("eml", ieml);
                parameters.put("vid", ivid);
                parameters.put("curr", icurr);
                parameters.put("cst", icst);
                parameters.put("cbk", icbk);
                parameters.put("hash", hashed_value);
                return parameters;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    //network to initiate STK Push
    private void initiateStkPush() {

        String data_string = itel+ivid+sid;

        final String hashed_value = hashing(data_string);

           if (hashed_value != "")
           {
               mAuthProgressDialog = new ProgressDialog(getActivity());
               mAuthProgressDialog.setMessage("Sending request to "+itel+" please wait...");
               mAuthProgressDialog.setCancelable(false);
               mAuthProgressDialog.show();

               StringRequest request = new StringRequest(Request.Method.POST, "https://apis.ipayafrica.com/payments/v2/transact/push/mpesa",
                       new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                              // Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();

                               mAuthProgressDialog.hide();
                               JSONObject oprator = null;

                               try {
                                   oprator = new JSONObject(response);
                                   String txt = oprator.getString("text");

                                   layout_channel.setVisibility(View.VISIBLE);
                                   layout_payment_mbonga.setVisibility(View.GONE);
                                   layout_payments.setVisibility(View.GONE);

                                   amount.setText("");
                                   email.setText("");
                                   phone.setText("");

                                   Toast.makeText(getActivity(), " "+txt, Toast.LENGTH_SHORT).show();

                                   dialog();

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }


                           }
                       }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       mAuthProgressDialog.hide();


                       NetworkResponse response = error.networkResponse;

                       //all the error code are silent
                       if (response != null && response.data != null) {
                           //L.m(new String(response.data));
                           //parsing the error
                           String json = "";
                           JSONObject obj;

                           switch (response.statusCode) {

                               case 401:
                                   //Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                                   break;

                               case 403:
                                   json = new String(response.data);

                                   try {
                                       obj = new JSONObject(json);


                                       //  Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();

                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                                   break;
                               case 400:
                                   json = new String(response.data);//string

                                   break;
                               default:
                                  // Toast.makeText(getActivity(), "system error occurred! please try again", Toast.LENGTH_LONG).show();

                                   break;
                           }
                       } else {
                           volleyErrors(error);
                       }

                   }
               }) {
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> parameters = new HashMap<String, String>();
                       parameters.put("vid", ivid);
                       parameters.put("phone", itel);
                       parameters.put("sid", sid);
                       parameters.put("hash", hashed_value);
                       return parameters;
                   }

               };

               RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
               requestQueue.add(request);
           }
           else {
               Toast.makeText(getActivity(), "hash ID missing try again", Toast.LENGTH_SHORT).show();
           }

    }


    //pay now code starts
    private void payNow()
    {
        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide softkeyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pay_now.getWindowToken(), 0);

                //assign values to parameters
                ilive = live;
                ioid = ioid;
                iinv = ioid;
                iamount = amount.getText().toString().trim();
                itel = phone.getText().toString().trim();
                ieml = email.getText().toString().trim();
                ivid = vid;
                icurr = "KES";
                icbk = "";
                icst = "0";
                icrl = "2";
                ikey = key;

                if (validate() == false) {

                } else {


                if (ScreenState.toString().equals("mpesa")) {


                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + icst + icbk;

                    getSeed();


                } else if (ScreenState.toString().equals("bonga")) {

                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + icst + icbk;

                    getSeed();

                } else if (ScreenState.toString().equals("airtel")) {

                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + icst + icbk;

                        getSeed();

                } else if (ScreenState.toString().equals("eazzy")) {

                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + icst + icbk;

                        getSeed();

                } else if (ScreenState.toString().equals("visa")) {

                    icbk = cbk;
                    icst = "1";
                    icrl = "0";
                    p1   = vid;

                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + p1 + p2 + p3 + p4 + icbk + icst + icrl;

                        String hashed = hashsha1(data_string);

                        //url encode
                        String url = null;
                        try {
                            url = URLEncoder.encode(icbk, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String theUrl = "https://payments.ipayafrica.com/v3/ke?live=" + ilive +
                                "&mm=1&mb=1&dc=1&cc=1&mer=ipay" + "&mpesa=0&airtel=0&equity=0&creditcard=1&elipa=0&debitcard=0" +
                                "&oid=" + ioid + "&inv=" + iinv + "&ttl=" + iamount + "&tel=" + itel + "&eml=" + ieml +
                                "&vid=" + ivid + "&p1=" + p1 + "&p2=" + p2 + "&p3=" + p3 + "&p4=" + p4 + "&crl=" + icrl + "&cbk=" + url + "&cst=" + icst +
                                "&curr=" + icurr + "&hsh=" + hashed;

                        amount.setText("");
                        phone.setText("");
                        email.setText("");

                        layout_channel.setVisibility(View.VISIBLE);
                        layout_payments.setVisibility(View.GONE);

                        CardChannel nextFrag = new CardChannel();
                        Bundle data = new Bundle();
                        data.putString("url", theUrl);
                        data.putString("oid_text", ioid);
                        data.putString("amount_text", iamount);
                        nextFrag.setArguments(data);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();


                } else {
                    layout_channel.setVisibility(View.VISIBLE);
                    layout_payment_mbonga.setVisibility(View.GONE);
                    layout_payments.setVisibility(View.GONE);
                }
            }

            }
        });
    }
    //pay now code ends

    // utility function to covert string to Hex
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //dial method
    private void dial()
    {
        String dial = "*126#";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        startActivity(intent);

    }

    //copy method
    private void copy(String value_copied, String title)
    {

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(value_copied);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("", value_copied);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(getActivity(), title+" "+value_copied+" is copied...", Toast.LENGTH_SHORT).show();

    }


    private void dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage("After You get your receipt from MPESA and an SMS confirmation from iPay, \nClick COMPLETE BUTTON below.");

        builder.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                confirmPayment();
            }
        });
        builder.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                initiateStkPush();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

     private void seconDialog()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

            builder.setMessage("Your transaction has not been processed. Please try again, \nClick RETRY or COMPLETE BUTTON below.");

            builder.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    confirmPayment();
                }
            });
            builder.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    initiateStkPush();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }

    //confirm the payment
    private void confirmPayment(){

        String data_string = sid+ivid;
        final String hashed_value = hashing(data_string);

        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setMessage("loading please wait...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://apis.ipayafrica.com/payments/v2/transact/mobilemoney",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mAuthProgressDialog.hide();

                        try {

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.toString().trim().equals("aei7p7yrx4ae34"))
                            {
                                Toast.makeText(getActivity(), "successful transaction", Toast.LENGTH_LONG).show();
                            }
                            else if (status.toString().trim().equals("fe2707etr5s4wq"))
                            {
                                Toast.makeText(getActivity(), "Failed transaction", Toast.LENGTH_LONG).show();

                                if(ScreenState.toString().equals("mpesa"))
                                {
                                    seconDialog();
                                }
                            }
                            else if (status.toString().trim().equals("bdi6p2yy76etrs"))
                            {
                                Toast.makeText(getActivity(), "Transaction is Pending", Toast.LENGTH_LONG).show();

                                if(ScreenState.toString().equals("mpesa"))
                                {
                                    seconDialog();
                                }
                            }
                            else if (status.toString().trim().equals("cr5i3pgy9867e1"))
                            {
                                Toast.makeText(getActivity(), "Used transaction code", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "system error! try again", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAuthProgressDialog.hide();

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    //L.m(new String(response.data));
                    //parsing the error
                    String json = "";
                    JSONObject obj;

                    switch (response.statusCode) {

                        case 401:
                            //Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                            break;

                        case 403:
                            json = new String(response.data);

                            try {
                                obj = new JSONObject(json);

                                // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 400:

                            json = new String(response.data);//string
                            Toast.makeText(getActivity(), "hash id mismatch", Toast.LENGTH_SHORT).show();

                            // Toast.makeText(getActivity(), "error occurred! try again", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            //Toast.makeText(LoginActivity.this, "error occurred try again", Toast.LENGTH_LONG).show();
                            break;

                    }
                } else {
                    volleyErrors(error);

                   if(ScreenState.toString().equals("mpesa"))
                   {
                       dialog();
                   }
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("vid", ivid);
                parameters.put("sid", sid);
                parameters.put("hash", hashed_value);
                return parameters;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    //validations
    private void volleyErrors(VolleyError error)
    {
        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }

        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_LONG).show();

    }

    private boolean validate()
    {
        if (amount.getText().toString().trim().equals("") ||
                amount.getText().toString().trim().equals("0")) {
            amount.setError("enter valid amount");

            return false;

        } else if (phone.getText().toString().trim().length() != 10) {
            phone.setError("invalid phone number");

            return false;

        } else if (phone.getText().toString().trim().equals("") ||
                !phone.getText().toString().trim().substring(0, 2).equals("07")) {

            phone.setError("enter valid phone number");

            return false;

        } else if (email.getText().toString().trim().equals("") ||
                !email.getText().toString().trim().contains("@") ||
                !email.getText().toString().trim().contains(".")){

            email.setError("enter valid email");

            return false;

        } else {
            return true;
        }
    }

}
